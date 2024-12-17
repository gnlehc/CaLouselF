package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseConnection;

public class WishList {
	private String wishlistId;
    private String userId;
    private String ItemId;

    public WishList(String wishlistId, String userId, String ItemId) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.ItemId = ItemId;
    }

	public String getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(String wishlistId) {
		this.wishlistId = wishlistId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItemId() {
		return ItemId;
	}

	public void setItemId(String ItemId) {
		this.ItemId = ItemId;
	}

	@Override
	public String toString() {
		return "WishList [wishlistId=" + wishlistId + ", userId=" + userId + ", ItemId=" + ItemId + "]";
	}
	
	private static DatabaseConnection DB() {
		return new DatabaseConnection();
	}
	
	public static boolean addToWishlist(Item item, int userId) {
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
	
	public static boolean isItemInWishlist(int userId, int itemId) {
		String query = "SELECT * FROM wishlists WHERE user_id = ? AND item_id = ?";
		try {
			PreparedStatement ps = DB().connection.prepareStatement(query);
			ps.setInt(1, userId);
			ps.setInt(2, itemId);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Item> getWishlistItems(int userId) {
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
	
	public static boolean removeFromWishlist(int userId, int itemId) {
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
