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
	private ArrayList<User> users = new ArrayList<User>();
	
	private DatabaseConnection DB() {
		return new DatabaseConnection();
	}

	public UserController() {

	}

	public Boolean Register(User user) {
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
			users.add(user);
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

	public Boolean checkUserExists(User user) {
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

	public boolean isUsernameUnique(String username) {
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

	public ObservableList<User> getAllUsers() {
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
