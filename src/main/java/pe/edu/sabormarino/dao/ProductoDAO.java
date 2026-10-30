
package pe.edu.sabormarino.dao;

import pe.edu.sabormarino.config.Conexion;
import pe.edu.sabormarino.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // LISTAR PRODUCTOS ACTIVOS
    public List<Producto> listarActivos() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM productos "
                   + "WHERE activo = 1 ORDER BY nombre";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }

        return productos;
    }

    // REGISTRAR PRODUCTO
    public boolean registrar(Producto producto) throws SQLException {

        String sql = "INSERT INTO productos "
                   + "(nombre, descripcion, precio, stock, id_categoria, activo) "
                   + "VALUES (?, ?, ?, ?, ?, 1)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());

            if (producto.getIdCategoria() == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, producto.getIdCategoria());
            }

            return ps.executeUpdate() > 0;
        }
    }

    // BUSCAR PRODUCTO POR ID
    public Producto buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM productos WHERE id_producto = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        }

        return null;
    }

    // ACTUALIZAR PRODUCTO
    public boolean actualizar(Producto producto) throws SQLException {

        String sql = "UPDATE productos SET nombre = ?, "
                   + "descripcion = ?, precio = ?, stock = ?, "
                   + "id_categoria = ? WHERE id_producto = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());

            if (producto.getIdCategoria() == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, producto.getIdCategoria());
            }

            ps.setInt(6, producto.getIdProducto());

            return ps.executeUpdate() > 0;
        }
    }

    // DESACTIVAR PRODUCTO (BAJA LÓGICA)
    public boolean desactivar(int id) throws SQLException {

        String sql = "UPDATE productos SET activo = 0 "
                   + "WHERE id_producto = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    // CONVERTIR RESULTADO SQL EN OBJETO PRODUCTO
    private Producto mapearProducto(ResultSet rs) throws SQLException {

        Producto producto = new Producto();

        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setStock(rs.getInt("stock"));

        int idCategoria = rs.getInt("id_categoria");

        if (rs.wasNull()) {
            producto.setIdCategoria(null);
        } else {
            producto.setIdCategoria(idCategoria);
        }

        producto.setActivo(rs.getBoolean("activo"));

        return producto;
    }
}
