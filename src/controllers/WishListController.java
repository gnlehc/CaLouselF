package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseConnection;
import models.Item;

public class WishListController {

	private DatabaseConnection db;

	public WishListController() {
		this.db = new DatabaseConnection();
	}

	public boolean addToWishlist(Item item, int userId) {
		if (item == null) {
			System.err.println("Selected item is null.");
			return false;
		}

		String query = "INSERT INTO wishlists (user_id, item_id) VALUES (?, ?)";
		try (PreparedStatement stmt = db.connection.prepareStatement(query)) {
			stmt.setInt(1, userId);
			stmt.setInt(2, item.getItemId());

			int rowsInserted = stmt.executeUpdate();

			if (rowsInserted > 0) {
				System.out.println("Item added to wishlist successfully.");
				return true;
			} else {
				System.err.println("Failed to insert item into wishlist.");
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Error while adding to wishlist: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean isItemInWishlist(String userId, String itemId) {
		String query = "SELECT * FROM wishlists WHERE user_id = ? AND item_id = ?";
		try {
			PreparedStatement ps = db.connection.prepareStatement(query);
			ps.setString(1, userId);
			ps.setString(2, itemId);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<Item> getWishlistItems(int userId) {
		ArrayList<Item> items = new ArrayList<>();
		String query = "SELECT i.* FROM items i JOIN wishlists w ON i.item_id = w.item_id WHERE w.user_id = ?";

		try {
			PreparedStatement ps = db.connection.prepareStatement(query);
			ps.setLong(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Item item = new Item(rs.getString("name"), rs.getString("category"), rs.getString("size"),
						rs.getDouble("price"), rs.getString("status"), rs.getInt("sellerId"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public boolean removeFromWishlist(int UserId, int itemId) {
		String query = "DELETE FROM wishlists WHERE user_id = ? AND item_id = ?";
		try {
			PreparedStatement ps = db.connection.prepareStatement(query);
			ps.setLong(1, UserId);
			ps.setLong(2, itemId);
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}