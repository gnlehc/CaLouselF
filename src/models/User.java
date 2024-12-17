package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	private int id;
	private String username;
	private String password;
	private String phoneNumber;
	private String address;
	private String role;

	public User(String username, String password, String phoneNumber, String address, String role) {
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", phoneNumber=" + phoneNumber
				+ ", address=" + address + ", role=" + role + "]";
	}
	
	private static DatabaseConnection DB() {
		return new DatabaseConnection();
	}
	
	public static boolean Register(User user) {
		if (checkUserExists(user)) {
			return false;
		}

		String query = "INSERT INTO users(username, password, phone_number, address, role)" + "VALUES (?, ?, ?, ?, ?)";

		try {
			PreparedStatement stmt = DB().connection.prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getPhoneNumber());
			stmt.setString(4, user.getAddress());
			stmt.setString(5, user.getRole());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static User Login(String username, String password) {
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";

		try (Connection conn = DB().connection; PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				User user = new User(rs.getString("username"), rs.getString("password"), rs.getString("phone_number"),
						rs.getString("address"), rs.getString("role"));
				user.setId(rs.getInt("user_id"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Boolean checkUserExists(User user) {
		String query = "SELECT COUNT(*) FROM users WHERE username=?";
		try {
			PreparedStatement stmt = DB().connection.prepareStatement(query);
			stmt.setString(1, user.getUsername());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return count > 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isUsernameUnique(String username) {
		String query = "SELECT COUNT(*) FROM users WHERE username = ?";
		try (PreparedStatement stmt = DB().connection.prepareStatement(query)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static ObservableList<User> getAllUsers() {
		ObservableList<User> users = FXCollections.observableArrayList();
		String query = "SELECT * FROM users";

		try (Statement stmt = DB().connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				User user = new User(rs.getString("username"), rs.getString("password"), rs.getString("phone_number"),
						rs.getString("address"), rs.getString("role"));
				user.setId(rs.getInt("user_id"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}
}
