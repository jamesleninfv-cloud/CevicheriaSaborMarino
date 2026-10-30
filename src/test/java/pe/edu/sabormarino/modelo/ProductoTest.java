
package pe.edu.sabormarino.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    @Test
    public void testCrearProducto() {

        Producto producto = new Producto();

        producto.setIdProducto(1);
        producto.setNombre("Jalea mixta");
        producto.setDescripcion("Pescado y mariscos fritos");
        producto.setPrecio(35.00);
        producto.setStock(10);
        producto.setIdCategoria(1);
        producto.setActivo(true);

        assertEquals(1, producto.getIdProducto());
        assertEquals("Jalea mixta", producto.getNombre());
        assertEquals("Pescado y mariscos fritos",
                     producto.getDescripcion());
        assertEquals(35.00, producto.getPrecio(), 0.001);
        assertEquals(10, producto.getStock());
        assertEquals(1, producto.getIdCategoria());
        assertTrue(producto.isActivo());
    }

    @Test
    public void testActualizarPrecio() {

        Producto producto = new Producto();

        producto.setPrecio(35.00);
        producto.setPrecio(38.00);

        assertEquals(38.00, producto.getPrecio(), 0.001);
    }

    @Test
    public void testProductoInactivo() {

        Producto producto = new Producto();

        producto.setActivo(false);

        assertFalse(producto.isActivo());
    }
}
