import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/bd";
    private static final String USUARIO = "";
    private static final String CONTRASENA = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: Driver de MySQL no encontrado.", e);
        }
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public static void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL UNIQUE," +
                "telefono VARCHAR(20)," +
                "activo BOOLEAN DEFAULT TRUE" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'clientes' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }
}