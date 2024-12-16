package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.Item;

public class ItemController {
	private DatabaseConnection db;
	private List<Item> items;

	public ItemController() {
		this.db = new DatabaseConnection();
		items = new ArrayList<>();
	}

	public boolean uploadItem(Item item) {
		String query = "INSERT INTO items (name, category, size, price, status, sellerId) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = db.connection.prepareStatement(query)) {
			preparedStatement.setString(1, item.getName());
			preparedStatement.setString(2, item.getCategory());
			preparedStatement.setString(3, item.getSize());
			preparedStatement.setDouble(4, item.getPrice());
			preparedStatement.setString(5, item.getStatus());
			preparedStatement.setInt(6, item.getSellerId());

			int rowsAffected = preparedStatement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean validateItem(Item item) {
		if (item.getName().isEmpty() || item.getName().length() < 3) {
			return false;
		}
		if (item.getCategory().isEmpty() || item.getCategory().length() < 3) {
			return false;
		}
		if (item.getSize().isEmpty()) {
			return false;
		}
		if (item.getPrice() <= 0) {
			return false;
		}
		return true;
	}

	public boolean updateItem(Item item) {
		if (!validateItem(item)) {
			return false;
		}

		String query = "UPDATE items SET name = ?, category = ?, size = ?, price = ?, status = ? WHERE item_id = ?";
		try (PreparedStatement stmt = db.connection.prepareStatement(query)) {
			stmt.setString(1, item.getName());
			stmt.setString(2, item.getCategory());
			stmt.setString(3, item.getSize());
			stmt.setDouble(4, item.getPrice());
			stmt.setString(5, item.getStatus());
			stmt.setInt(6, item.getItemId());

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public ObservableList<Item> getItemsBySellerId(int sellerId) {
		ObservableList<Item> items = FXCollections.observableArrayList();
		String query = "SELECT * FROM items WHERE sellerId = ?";

		try (Connection conn = db.connection; PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, sellerId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Item item = new Item(rs.getString("name"), rs.getString("category"), rs.getString("size"),
						rs.getDouble("price"), rs.getString("status"), rs.getInt("sellerId"));
				item.setItemId(rs.getInt("item_id"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public ObservableList<Item> getApprovedItems() {
		ObservableList<Item> approvedItems = FXCollections.observableArrayList();
		String query = "SELECT * FROM items WHERE status = 'Approved'";

		try (Statement stmt = db.connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Item item = new Item(rs.getString("name"), rs.getString("category"), rs.getString("size"),
						rs.getDouble("price"), rs.getString("status"), rs.getInt("sellerId"));
				item.setItemId(rs.getInt("item_id"));
				approvedItems.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return approvedItems;
	}

	public void insertDefaultItems() {
		items.add(new Item("Jeans", "Bottom", "Medium", 50.0, "Approved", 1));
		items.add(new Item("Shirt", "Top", "Large", 100.0, "Approved", 1));
		items.add(new Item("T-Shirt", "Top", "Small", 20.0, "Pending", 1));
		items.add(new Item("Airism", "Top", "Medium", 150.0, "Approved", 2));
		items.add(new Item("UV Jacket", "Outer", "Large", 75.0, "Pending", 2));

		for (Item i : items) {
			uploadItem(i);
		}
	}

	public ObservableList<Item> getPendingItems() {
		ObservableList<Item> items = FXCollections.observableArrayList();
		String query = "SELECT * FROM items WHERE status = 'Pending'";

		try (Statement stmt = db.connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Item item = new Item(rs.getString("name"), rs.getString("category"), rs.getString("size"),
						rs.getDouble("price"), rs.getString("status"), rs.getInt("sellerId"));
				item.setItemId(rs.getInt("item_id"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	public boolean approveItem(int itemId) {
		String query = "UPDATE items SET status = 'approved' WHERE item_id = ?";
		try (PreparedStatement stmt = db.connection.prepareStatement(query)) {
			stmt.setInt(1, itemId);
			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean declineItem(int itemId, String reason) {
		if (!doesItemExist(itemId)) {
			showAlert("Error", "Item not found.");
			return false;
		}

		logDeclineReason(itemId, reason);

		String query = "DELETE FROM items WHERE item_id = ?";
		try (PreparedStatement stmt = db.connection.prepareStatement(query)) {
			stmt.setInt(1, itemId);
			int rowsDeleted = stmt.executeUpdate();
			return rowsDeleted > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean doesItemExist(int itemId) {
		String query = "SELECT COUNT(*) FROM items WHERE item_id = ?";
		try (PreparedStatement stmt = db.connection.prepareStatement(query)) {
			stmt.setInt(1, itemId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void logDeclineReason(int itemId, String reason) {
		String query = "INSERT INTO item_decline_logs (item_id, reason) VALUES (?, ?)";
		try (PreparedStatement stmt = db.connection.prepareStatement(query)) {
			stmt.setInt(1, itemId);
			stmt.setString(2, reason);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteItem(int itemId) {
		
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
