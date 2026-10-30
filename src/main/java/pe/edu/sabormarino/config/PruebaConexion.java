
package pe.edu.sabormarino.config;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {
        try (Connection conexion = Conexion.conectar()) {
            if (conexion != null && conexion.isValid(3)) {
                System.out.println(
                    "CONEXION EXITOSA A sabor_marino"
                );
            }
        } catch (SQLException e) {
            System.err.println("ERROR DE CONEXION:");
            e.printStackTrace();
        }
    }
}
