package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	public Connection connection;
	public Statement statement;
	public ResultSet resultSet;
	public ResultSetMetaData resultSetMetaData;
	public PreparedStatement preparedStatement;

	public DatabaseConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calouselfdb", "root", "");
			statement = connection.createStatement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultSet execQuery(String query) {
		try {
			resultSet = statement.executeQuery(query);
			resultSetMetaData = resultSet.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public void exec(String query) {
		try {
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createUsersTable() {
		String query = "CREATE TABLE IF NOT EXISTS users (" + "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "username VARCHAR(50) UNIQUE NOT NULL, " + "password VARCHAR(255) NOT NULL, "
				+ "phone_number VARCHAR(20) NOT NULL, " + "address TEXT NOT NULL, "
				+ "role ENUM('Seller', 'Buyer', 'Admin') NOT NULL" + ")";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createItemsTable() {
		String query = "CREATE TABLE IF NOT EXISTS items (" + "itemId INT AUTO_INCREMENT PRIMARY KEY, "
				+ "name VARCHAR(255) NOT NULL, " + "category VARCHAR(255) NOT NULL, " + "size VARCHAR(50) NOT NULL, "
				+ "price DOUBLE NOT NULL, " + "status ENUM('Pending', 'Approved', 'Declined') DEFAULT 'Pending')";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void migrateTables() {
		createUsersTable();
		createItemsTable();
	}
}
