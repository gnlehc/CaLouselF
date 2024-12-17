package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseConnection;
import models.Item;

public class WishListController {
	private DatabaseConnection DB() {
		return new DatabaseConnection();
	}

	public WishListController() {
		
	}

	public boolean addToWishlist(Item item, int userId) {
		if (item == null) {
			return false;
		}

		String query = "INSERT INTO wishlists (user_id, item_id) VALUES (?, ?)";
		try (PreparedStatement stmt = DB().connection.prepareStatement(query)) {
			stmt.setInt(1, userId);
			stmt.setInt(2, item.getItemId());

			int rowsInserted = stmt.executeUpdate();

			if (rowsInserted > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isItemInWishlist(String userId, String itemId) {
		String query = "SELECT * FROM wishlists WHERE user_id = ? AND item_id = ?";
		try {
			PreparedStatement ps = DB().connection.prepareStatement(query);
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
			PreparedStatement ps = DB().connection.prepareStatement(query);
			ps.setLong(1, userId);
			ResultSet rs = ps.executeQuery();

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

	public boolean removeFromWishlist(int userId, int itemId) {
	    String query = "DELETE FROM wishlists WHERE user_id = ? AND item_id = ?";
	    try {
	        PreparedStatement ps = DB().connection.prepareStatement(query);
	        ps.setInt(1, userId);
	        ps.setInt(2,itemId);
	        int result = ps.executeUpdate();
	        return result > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}