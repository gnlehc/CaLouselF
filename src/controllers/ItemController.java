package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Item;

public class ItemController {
	private DatabaseConnection db;
	private List<Item> items;

	public ItemController() {
		this.db = new DatabaseConnection();
		items = new ArrayList<>();
	}

	public boolean uploadItem(Item item) {
		String query = "INSERT INTO items (name, category, size, price, status) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = db.connection.prepareStatement(query)) {
			preparedStatement.setString(1, item.getName());
			preparedStatement.setString(2, item.getCategory());
			preparedStatement.setString(3, item.getSize());
			preparedStatement.setDouble(4, item.getPrice());
			preparedStatement.setString(5, item.getStatus());

			int rowsAffected = preparedStatement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Item> getApprovedItems() {
		List<Item> approvedItems = new ArrayList<>();
		for (Item item : items) {
			if (item.getStatus().equalsIgnoreCase("Approved")) {
				approvedItems.add(item);
			}
		}
		return approvedItems;
	}

	public void insertDefaultItems() {
		items.add(new Item("Jeans", "Bottom", "Medium", 50.0, "Approved"));
		items.add(new Item("Shirt", "Top", "Large", 100.0, "Approved"));
		items.add(new Item("T-Shirt", "Top", "Small", 20.0, "Pending"));
		items.add(new Item("Airism", "Top", "Medium", 150.0, "Approved"));
		items.add(new Item("UV Jacket", "Outer", "Large", 75.0, "Pending"));

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
						rs.getDouble("price"), rs.getString("status"));
				item.setItemId(rs.getInt("itemId"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	public void approveItem(int itemId) {
		String query = "UPDATE items SET status = 'Approved' WHERE itemId = ?";

		try (PreparedStatement pstmt = db.connection.prepareStatement(query)) {
			pstmt.setInt(1, itemId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void declineItem(int itemId, String reason) {
		String query = "UPDATE items SET status = 'Declined', decline_reason = ? WHERE itemId = ?";

		try (PreparedStatement pstmt = db.connection.prepareStatement(query)) {
			pstmt.setString(1, reason);
			pstmt.setInt(2, itemId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
