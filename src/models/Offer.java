package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Offer {
	private int offerId;
	private double offerPrice;
	private String offerStatus;
	private int userId;
	private int itemId;
	private Item item;
	
	public Offer(int offerId, double offerPrice, String offerStatus, int userId, int itemId, Item item) {
		this.offerId = offerId;
		this.offerPrice = offerPrice;
		this.offerStatus = offerStatus;
		this.userId = userId;
		this.itemId = itemId;
		this.item = item;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(double offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getOfferStatus() {
		return offerStatus;
	}

	public void setOfferStatus(String offerStatus) {
		this.offerStatus = offerStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Offer [offerId=" + offerId + ", offerPrice=" + offerPrice + ", offerStatus=" + offerStatus + ", userId="
				+ userId + ", itemId=" + itemId + ", item=" + item + "]";
	}
	
	private static DatabaseConnection DB() {
		return new DatabaseConnection();
	}
	
	public static Validation createOffer(double offerPrice, int userId, int itemId) {
		String query = "SELECT MAX(offer_price) AS max_offer_price FROM offers WHERE item_id = ?";
		try (PreparedStatement preparedStatement = DB().connection.prepareStatement(query)) {
			preparedStatement.setInt(1, itemId);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				double maxOfferPrice = rs.getDouble("max_offer_price");
				if (!rs.wasNull() && maxOfferPrice >= offerPrice) {
					return new Validation(false, "Validation Error", String.format("Offer price must be greater than current maximum offer price: %.2f", maxOfferPrice));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new Validation(false, "Error", "An error occurred while creating the offer. Please try again.");
		}
		
		query = "INSERT INTO offers (offer_price, user_id, item_id) VALUES (?, ?, ?)";
		try (PreparedStatement preparedStatement = DB().connection.prepareStatement(query)) {
			preparedStatement.setDouble(1, offerPrice);
			preparedStatement.setInt(2, userId);
			preparedStatement.setInt(3, itemId);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				return new Validation(true, "Success", "Offer created successfully and moved to offer list.");	
			} else {
				return new Validation(false, "Error", "An error occurred while creating the offer. Please try again.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new Validation(false, "Error", "An error occurred while creating the offer. Please try again.");
		}
	}
	
	public static boolean acceptOffer(int offerId) {
		// create purchase item
		return true;
	}
	
	public static boolean declineOffer(int offerId) {
		// create offer decline logs
		return true;
	}
	
	public static ObservableList<Offer> viewOfferItemForSeller(int userId, int itemId) {
		ObservableList<Offer> offers = FXCollections.observableArrayList();
		return offers;
	}
	
	public static ObservableList<Offer> viewOfferItemForBuyer(int userId) {
		ObservableList<Offer> offers = FXCollections.observableArrayList();
		return offers;
	}
}
