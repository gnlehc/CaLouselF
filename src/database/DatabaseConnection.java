package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
	public Connection connection;
	public Statement statement;
	public PreparedStatement preparedStatement;

	public DatabaseConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calouselfdb", "root", "");
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exec(String query) {
		try {
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createUsersTable() {
		String query = "CREATE TABLE IF NOT EXISTS users (" + "user_id INT AUTO_INCREMENT PRIMARY KEY, "
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
		String query = "CREATE TABLE IF NOT EXISTS items (" + "item_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "name VARCHAR(255) NOT NULL, " + "category VARCHAR(255) NOT NULL, " + "size VARCHAR(50) NOT NULL, "
				+ "price DOUBLE NOT NULL, " + "status ENUM('Pending', 'Approved', 'Declined') DEFAULT 'Pending', "
				+ "sellerId int NOT NULL)";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createWishlistTable() {
		String query = "CREATE TABLE IF NOT EXISTS wishlists ("
		        + "wishlist_id INT AUTO_INCREMENT PRIMARY KEY, "
		        + "user_id INT NOT NULL, "
		        + "item_id INT NOT NULL, "
		        + "FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE, "
		        + "FOREIGN KEY (item_id) REFERENCES items(item_id) ON UPDATE CASCADE ON DELETE CASCADE)";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createItemDeclineLogTable() {
		String query = "CREATE TABLE IF NOT EXISTS item_decline_logs (\n"
				+ "		    log_id INT AUTO_INCREMENT PRIMARY KEY,\n" + "		    item_id INT NOT NULL,\n"
				+ "		    reason TEXT NOT NULL)";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createTransactionTable() {
		String query = "CREATE TABLE IF NOT EXISTS transactions (" + "transaction_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "user_id INT NOT NULL, " + "item_id INT NOT NULL, "
				+ "FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE, "
				+ "FOREIGN KEY (item_id) REFERENCES items(item_id) ON UPDATE CASCADE ON DELETE CASCADE)";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createOfferTable() {
		String query = "CREATE TABLE IF NOT EXISTS offers (" + "offer_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "offer_price DOUBLE NOT NULL, " + "offer_status ENUM('Pending', 'Accepted', 'Declined') DEFAULT 'Pending', " + "user_id INT NOT NULL, " + "item_id INT NOT NULL, "
				+ "FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE, "
				+ "FOREIGN KEY (item_id) REFERENCES items(item_id) ON UPDATE CASCADE ON DELETE CASCADE)";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createOfferDeclineLogTable() {
		String query = "CREATE TABLE IF NOT EXISTS offer_decline_logs (\n"
				+ "		    log_id INT AUTO_INCREMENT PRIMARY KEY,\n" + "		    offer_id INT NOT NULL,\n"
				+ "		    reason TEXT NOT NULL)";
		try {
			exec(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void migrateTables() {
		createUsersTable();
		createItemsTable();
		createWishlistTable();
		createItemDeclineLogTable();
		createTransactionTable();
		createOfferTable();
		createOfferDeclineLogTable();
	}

}
