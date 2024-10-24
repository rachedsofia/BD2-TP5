package ar.unrn.tp.servicios;
import ar.unrn.tp.modelo.Venta;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Service
public class UltimasComprasServices {

    @Autowired
    private JedisPool pool;

    @Autowired
    private JPAVentasService ventasService;

    private static final String PREFIX_KEY = "cliente:";

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Venta> obtenerUltimasCompras(Long idCliente) throws Exception {
        String key = PREFIX_KEY + idCliente + ":ultimasCompras";

        try (Jedis jedis = pool.getResource()) {
            String comprasJson = jedis.get(key);

            System.out.println("Comrpas JSON" + comprasJson);
            if (comprasJson != null) {
                return objectMapper.readValue(comprasJson, List.class);
            }

            List<Venta> ultimasCompras = ventasService.obtenerUltimasVentasCliente(idCliente, 3);
            System.out.println("ultimas comoras" + ultimasCompras);
            jedis.set(key, objectMapper.writeValueAsString(ultimasCompras));

            return ultimasCompras;
        }
    }

    @Transactional
    public void registrarNuevaCompra(Venta nuevaVenta) throws Exception {
        ventasService.registrarVenta(nuevaVenta);

        String key = PREFIX_KEY + nuevaVenta.getCliente().getId() + ":ultimasCompras";

        try (Jedis jedis = pool.getResource()) {
            String comprasJson = jedis.get(key);
            List<Venta> ultimasCompras;

            if (comprasJson != null) {
                ultimasCompras = objectMapper.readValue(comprasJson, List.class);
            } else {
                ultimasCompras = ventasService.obtenerUltimasVentasCliente(nuevaVenta.getCliente().getId(), 3);
            }

            ultimasCompras.add(0, nuevaVenta);
            if (ultimasCompras.size() > 3) {
                ultimasCompras.remove(ultimasCompras.size() - 1);
            }
            jedis.set(key, objectMapper.writeValueAsString(ultimasCompras));
        }
    }
}
