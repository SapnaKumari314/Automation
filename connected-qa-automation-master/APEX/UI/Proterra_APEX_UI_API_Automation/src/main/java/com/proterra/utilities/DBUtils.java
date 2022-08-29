package main.java.com.proterra.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {


	private static Connection connection = null;


	@SuppressWarnings("deprecation")
	public static Connection connectDB(String url, String username, String password) {
		try {
//			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}


	public static ResultSet executeQuery(String queryString) {
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = connection.createStatement();
			rs = stm.executeQuery(queryString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	
	public static boolean execute(String queryString) {
		Statement stm = null;
		boolean result = false;
		try {
			stm = connection.createStatement();
			result = stm.execute(queryString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	


	public static void closeDatabaseConnection() {
		try {
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

}
