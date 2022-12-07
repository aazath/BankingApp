package BankingApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtil {
	JdbcUtil()
	{
		
	}
	
	public static Connection getConnection() throws IOException, SQLException
	{
		//resource used in the app
		Connection connection = null;
		
		//create a properties object
		Properties prop = new Properties();
		
		//create an input file stream to read properties file
		FileInputStream fis = new FileInputStream("/Users/apple/eclipse-workspace/BankingApp/BankingApp/Jdbc.properties");
		//load the properties from the file
		prop.load(fis);
		
		//Establish the connection
		String url = prop.getProperty("url");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		
		connection = DriverManager.getConnection(url,user,password);
		if(connection != null)
		{
			return connection;
		}
		
		return connection;
		
		
	}
	
	public static void closeConnection(ResultSet resultSet, Statement statement, Connection connection)
			throws SQLException {

		// 6. closing the resources used
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();

		}
		if (connection != null) {
			connection.close();
		}

	}

}
