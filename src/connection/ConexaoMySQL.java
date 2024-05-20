package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {

	public static String Status = "Não conectou...";
	public ConexaoMySQL() {
		
	}
	
	public static java.sql.Connection getInstance(){
		Connection connection = null;
		
		try {
			String driverName = "com.mysql.cj.jdbc.Driver";
			
			Class.forName(driverName);
			
			
			String serverName = "localhost:3306";
			String myDataBase = "projetoa3";
			String  url = "jdbc:mysql://"+ serverName + "/" + myDataBase;
			String username = "root";
			String password = "1234";
			
			connection = DriverManager.getConnection(url, username, password);
			
			if (connection != null) {
			Status = ("Status ---> Conectado com sucesso!.");
			}else {
				Status = ("Status ---> Não foi possivel realizar conexão.");
			}
			return connection;
		} catch (ClassNotFoundException e) {
			System.out.println("o driver especificado não foi encontrado.");
			return null;
		}catch (SQLException e) {
			System.out.println("Não foi possível conectar ao Banco de Dados");
			return null;
		}	
	}
	
	public static String statusConection() {
		return Status;
	}
	
	public static boolean FecharConexao() {
		try {
			ConexaoMySQL.getInstance().close();
			return true;
		}catch (SQLException e) {
			return false;
		}
	}
	
	public static java.sql.Connection reiniciarConnection() {
		FecharConexao();
		
		return ConexaoMySQL.getInstance();
	}
}












