
package pe.edu.sabormarino.controlador;

import pe.edu.sabormarino.dao.ProductoDAO;
import pe.edu.sabormarino.modelo.Producto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet(name = "ReporteProductoServlet",
            urlPatterns = {"/reporteProductos"})
public class ReporteProductoServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Producto> productos = productoDAO.listarActivos();

            Workbook libro = new XSSFWorkbook();

            Sheet hoja = libro.createSheet("Inventario");

            Row encabezado = hoja.createRow(0);

            encabezado.createCell(0).setCellValue("ID");
            encabezado.createCell(1).setCellValue("Nombre");
            encabezado.createCell(2).setCellValue("Descripcion");
            encabezado.createCell(3).setCellValue("Precio");
            encabezado.createCell(4).setCellValue("Stock");
            encabezado.createCell(5).setCellValue("Categoria");

            int fila = 1;

            for (Producto producto : productos) {

                Row row = hoja.createRow(fila++);

                row.createCell(0).setCellValue(
                        producto.getIdProducto());

                row.createCell(1).setCellValue(
                        producto.getNombre());

                row.createCell(2).setCellValue(
                        producto.getDescripcion() == null
                        ? "" : producto.getDescripcion());

                row.createCell(3).setCellValue(
                        producto.getPrecio());

                row.createCell(4).setCellValue(
                        producto.getStock());

                row.createCell(5).setCellValue(
                        producto.getIdCategoria() == null
                        ? "" : producto.getIdCategoria().toString());
            }

            for (int i = 0; i < 6; i++) {
                hoja.autoSizeColumn(i);
            }

            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=inventario_productos.xlsx");

            libro.write(response.getOutputStream());
            libro.close();

        } catch (SQLException e) {
            throw new ServletException(
                    "Error al generar el reporte de productos", e);
        }
    }
}
