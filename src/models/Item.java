package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Item {
	private int itemId;
	private String name;
	private String category;
	private String size;
	private double price;
	private String status;
	private int sellerId;

	public Item(String name, String category, String size, double price, String status, int sellerId) {
		this.name = name;
		this.category = category;
		this.size = size;
		this.price = price;
		this.status = status;
		this.sellerId = sellerId;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", name=" + name + ", category=" + category + ", size=" + size + ", price="
				+ price + ", status=" + status + ", sellerId=" + sellerId + "]";
	}
	
	private static DatabaseConnection DB() {
		return new DatabaseConnection();
	}
	
	public static boolean uploadItem(Item item) {
		String query = "INSERT INTO items (name, category, size, price, status, sellerId) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = DB().connection.prepareStatement(query)) {
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
	
	public static boolean updateItem(Item item) {
		String query = "UPDATE items SET name = ?, category = ?, size = ?, price = ?, status = ? WHERE item_id = ?";
		try (PreparedStatement stmt = DB().connection.prepareStatement(query)) {
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
	
	public static ObservableList<Item> getItemsBySellerId(int sellerId) {
		ObservableList<Item> items = FXCollections.observableArrayList();
		String query = "SELECT * FROM items WHERE sellerId = ?";

		try (Connection conn = DB().connection; PreparedStatement stmt = conn.prepareStatement(query)) {
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
	
	public static ObservableList<Item> getApprovedItems() {
		ObservableList<Item> approvedItems = FXCollections.observableArrayList();
		String query = "SELECT * FROM items WHERE status = 'Approved'";

		try (Statement stmt = DB().connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
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
	
	public static ObservableList<Item> getPendingItems() {
		ObservableList<Item> items = FXCollections.observableArrayList();
		String query = "SELECT * FROM items WHERE status = 'Pending'";

		try (Statement stmt = DB().connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
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
	
	public static boolean approveItem(int itemId) {
		String query = "UPDATE items SET status = 'approved' WHERE item_id = ?";
		try (PreparedStatement stmt = DB().connection.prepareStatement(query)) {
			stmt.setInt(1, itemId);
			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteItem(int itemId) {
		String query = "DELETE FROM items WHERE item_id = ?";
		try (PreparedStatement stmt = DB().connection.prepareStatement(query)) {
			stmt.setInt(1, itemId);
			int rowsDeleted = stmt.executeUpdate();
			return rowsDeleted > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean logDeclineReason(int itemId, String reason) {
		String query = "INSERT INTO item_decline_logs (item_id, reason) VALUES (?, ?)";
		try (PreparedStatement stmt = DB().connection.prepareStatement(query)) {
			stmt.setInt(1, itemId);
			stmt.setString(2, reason);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
