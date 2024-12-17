package models;

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
	
	public static boolean createOffer(double offerPrice, int userId, int itemId) {
		return true;
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
