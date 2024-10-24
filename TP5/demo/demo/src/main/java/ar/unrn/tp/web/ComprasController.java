package ar.unrn.tp.web;

import ar.unrn.tp.modelo.Venta;
import ar.unrn.tp.servicios.UltimasComprasServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/compras")
public class ComprasController {
    @Autowired
    private UltimasComprasServices ultimasComprasService;

    @GetMapping("/ultimas/{idCliente}")
    public ResponseEntity<List<Venta>> obtenerUltimasCompras(@PathVariable Long idCliente) {
        System.out.println("ID CLIENTE" + idCliente);
        try {
            List<Venta> ultimasCompras = ultimasComprasService.obtenerUltimasCompras(idCliente);
            return ResponseEntity.ok(ultimasCompras);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
