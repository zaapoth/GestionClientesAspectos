import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClienteServiceTest {

    private static ClienteService clienteService;

    @BeforeAll
    public static void setup() {
        ConexionDB.crearTablaSiNoExiste();

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
            stmt.execute("TRUNCATE TABLE clientes;");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            System.out.println("--- Base de datos limpiada para pruebas ---");

        } catch (SQLException e) {
            System.err.println("Error limpiando la base de datos: " + e.getMessage());
        }

        clienteService = new ClienteService();
    }

    @Test
    @Order(1)
    void prueba1_RegistrarClienteNuevo() {
        System.out.println("Prueba 1: Registrar...");
        Cliente c = new Cliente(0, "Cliente de Prueba", "prueba@test.com", "123456", true);
        String resultado = clienteService.registrarCliente(c);
        assertEquals("Cliente registrado exitosamente.", resultado);
    }

    @Test
    @Order(2)
    void prueba2_EvitarDuplicidad() {
        System.out.println("Prueba 2: Evitar duplicados...");
        Cliente c = new Cliente(0, "Otro Cliente", "prueba@test.com", "987654", true);
        String resultado = clienteService.registrarCliente(c);
        assertTrue(resultado.contains("Error: El email"));
    }

    @Test
    @Order(3)
    void prueba3_ConsultarCliente() {
        System.out.println("Prueba 3: Consultar...");
        List<Cliente> clientes = clienteService.buscarClientes("Prueba");
        assertFalse(clientes.isEmpty());
        assertEquals("prueba@test.com", clientes.get(0).getEmail());
    }

    @Test
    @Order(4)
    void prueba4_EliminacionLogica() {
        System.out.println("Prueba 4: Eliminar lógicamente...");
        int idClientePrueba = clienteService.buscarClientes("prueba@test.com").get(0).getId();

        String resultado = clienteService.eliminarCliente(idClientePrueba);
        assertEquals("Cliente eliminado (lógicamente).", resultado);

        List<Cliente> clientes = clienteService.buscarClientes("prueba@test.com");
        assertTrue(clientes.isEmpty());
    }

    @Test
    @Order(5)
    void prueba5_ValidacionNombreVacio() {
        System.out.println("Prueba 5: Validación de nombre vacío...");
        Cliente c = new Cliente(0, "", "vacio@test.com", "11111", true);
        String resultado = clienteService.registrarCliente(c);
        assertEquals("Error: El nombre no puede estar vacío.", resultado);
    }

    @Test
    @Order(6)
    void prueba6_GenerarReportes() {
        System.out.println("--- EJECUTANDO PRUEBA DE REPORTES ---");
        ClienteService servicio = new ClienteService();

        servicio.generarReporteClientesActivos();
        servicio.generarReporteEstadisticas();
        System.out.println("--- FIN DE REPORTES ---");
    }
}