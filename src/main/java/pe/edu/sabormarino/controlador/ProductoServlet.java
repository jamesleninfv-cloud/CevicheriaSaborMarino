
package pe.edu.sabormarino.controlador;

import pe.edu.sabormarino.dao.ProductoDAO;
import pe.edu.sabormarino.modelo.Producto;

import org.apache.commons.lang3.StringUtils;
import com.google.common.primitives.Ints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductoServlet.class);

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("nuevo".equals(accion)) {
                request.getRequestDispatcher("/formularioProducto.jsp")
                       .forward(request, response);
                return;
            }

            if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Producto producto = productoDAO.buscarPorId(id);

                if (producto == null || !producto.isActivo()) {
                    response.sendError(404, "Producto no encontrado");
                    return;
                }

                request.setAttribute("producto", producto);
                request.getRequestDispatcher("/formularioProducto.jsp")
                       .forward(request, response);
                return;
            }

            List<Producto> productos = productoDAO.listarActivos();
            request.setAttribute("productos", productos);

            request.getRequestDispatcher("/productos.jsp")
                   .forward(request, response);

        } catch (SQLException | NumberFormatException e) {
            logger.error("Error al consultar productos", e);
            throw new ServletException(
                    "Error al consultar productos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("registrar".equals(accion)
                    || "actualizar".equals(accion)) {

                String nombre = StringUtils.trimToEmpty(
                        request.getParameter("nombre"));

                String descripcion = StringUtils.trimToEmpty(
                        request.getParameter("descripcion"));

                if (StringUtils.isBlank(nombre)) {
                    response.sendError(400, "El nombre es obligatorio");
                    return;
                }

                if (nombre.length() > 100) {
                    response.sendError(400, "Nombre demasiado largo");
                    return;
                }

                String precioTexto = request.getParameter("precio");
                String stockTexto = request.getParameter("stock");

                if (StringUtils.isBlank(precioTexto)
                        || StringUtils.isBlank(stockTexto)) {
                    response.sendError(400,
                            "Precio y stock son obligatorios");
                    return;
                }

                double precio = Double.parseDouble(precioTexto.trim());
                int stock = Integer.parseInt(stockTexto.trim());

                if (!Double.isFinite(precio)
                        || precio <= 0 || stock < 0) {
                    response.sendError(400,
                            "Precio o stock inválido");
                    return;
                }

                Producto producto = new Producto();
                producto.setNombre(nombre);
                producto.setDescripcion(descripcion);
                producto.setPrecio(precio);
                producto.setStock(stock);

                String categoria =
                        request.getParameter("idCategoria");

                if (StringUtils.isBlank(categoria)) {
                    producto.setIdCategoria(null);
                } else {
                    Integer idCategoria =
                            Ints.tryParse(categoria.trim());

                    if (idCategoria == null || idCategoria <= 0) {
                        response.sendError(400,
                                "ID de categoría inválido");
                        return;
                    }

                    producto.setIdCategoria(idCategoria);
                }

                if ("registrar".equals(accion)) {
                    productoDAO.registrar(producto);

                    logger.info("Producto registrado: {}", nombre);

                } else {
                    int idProducto = Integer.parseInt(
                            request.getParameter("idProducto"));

                    if (idProducto <= 0) {
                        response.sendError(400,
                                "ID de producto inválido");
                        return;
                    }

                    producto.setIdProducto(idProducto);
                    productoDAO.actualizar(producto);

                    logger.info("Producto actualizado: ID {}, nombre {}",
                            idProducto, nombre);
                }

            } else if ("desactivar".equals(accion)) {

                int id = Integer.parseInt(
                        request.getParameter("idProducto"));

                if (id <= 0) {
                    response.sendError(400,
                            "ID de producto inválido");
                    return;
                }

                productoDAO.desactivar(id);

                logger.info("Producto desactivado: ID {}", id);

            } else {
                response.sendError(400, "Acción no válida");
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/productos");

        } catch (SQLException | NumberFormatException e) {
            logger.error("Error al procesar producto. Acción: "
                    + accion, e);

            throw new ServletException(
                    "Error al procesar el producto", e);
        }
    }
}
