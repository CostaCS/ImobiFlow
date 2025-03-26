import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class teste_conexao {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/usuario";
        String user = "postgres";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
