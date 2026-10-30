
package pe.edu.sabormarino.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sabor_marino"
            + "?useSSL=false"
            + "&serverTimezone=America/Lima"
            + "&allowPublicKeyRetrieval=true";

    private static final String USER = "root";

    // Contraseña configurada en tu MySQL local
    private static final String PASSWORD = "Fedaikin15@";

    public static Connection conectar() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "No se encontró el controlador JDBC de MySQL. "
                    + "Verifica que mysql-connector-j esté disponible "
                    + "en WEB-INF/lib.",
                    e
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
