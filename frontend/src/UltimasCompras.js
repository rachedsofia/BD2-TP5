import React, { useEffect, useState } from 'react';
import './UltimasCompras.css';

function UltimasCompras({ api, idCliente }) {
    const [compras, setCompras] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchUltimasCompras = async () => {
            try {
                const response = await fetch(`${api}/api/compras/ultimas/${idCliente}`);
                
                if (!response.ok) {
                    throw new Error('Error al obtener las últimas compras');
                }
                
                const data = await response.json();
                console.log(data); // Verifica la respuesta
                setCompras(data);
            } catch (err) {
                setError('No se pudieron cargar las últimas compras.');
            }
        };

        fetchUltimasCompras();
    }, [api, idCliente]);

    return (
        <div className="compras-container">
            <h2 className="titulo">Últimas Compras</h2>
            {error && <p className="error">{error}</p>}
            {compras.length === 0 ? (
                <p className="no-compras">No hay compras recientes</p>
            ) : (
                <ul className="compras-lista">
                    {compras.map((compra) => (
                        <li key={compra.id} className="compra-item">
                            <p><strong>Fecha:</strong> {new Date(compra.fecha).toLocaleDateString()}</p>
                            <p><strong>Total:</strong> ${compra.total}</p>
                            <p><strong>Productos:</strong> {compra.productos.map(prod => prod.nombre).join(', ')}</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default UltimasCompras;
