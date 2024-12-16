package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

public class UserController {
	private DatabaseConnection db;
	private ArrayList<User> users = new ArrayList<User>();

	public UserController() {
		this.db = new DatabaseConnection();
	}

	public Boolean Register(User user) {
		// validate if user already exists
		if (checkUserExists(user)) {
			return false;
		}

		String query = "INSERT INTO users(username, password, phone_number, address, role)" + "VALUES (?, ?, ?, ?, ?)";

		try {
			PreparedStatement stmt = db.connection.prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getPhoneNumber());
			stmt.setString(4, user.getAddress());
			stmt.setString(5, user.getRole());
			int rowsAffected = stmt.executeUpdate();
			users.add(user);
			System.out.println(rowsAffected);
			return rowsAffected > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void insertDefaultUsers() {
		try {
			Register(new User("admin", "admin", "08111111111", "Jl. Admin", "Admin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User Login(String username, String password) {
		String query = "SELECT * FROM users WHERE username = ? AND password = ?";

		try (Connection conn = db.connection; PreparedStatement pstmt = conn.prepareStatement(query)) {

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
			System.err.println("Error authenticating user: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public Boolean checkUserExists(User user) {
		String query = "SELECT COUNT(*) FROM users WHERE username=?";
		try {
			PreparedStatement stmt = db.connection.prepareStatement(query);
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

	public boolean isUsernameUnique(String username) {
		String query = "SELECT COUNT(*) FROM users WHERE username = ?";
		try (PreparedStatement stmt = db.connection.prepareStatement(query)) {
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

	public ObservableList<User> getAllUsers() {
		ObservableList<User> users = FXCollections.observableArrayList();
		String query = "SELECT * FROM users";

		try (Statement stmt = db.connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				// Creating User object from DB result
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
