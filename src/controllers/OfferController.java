package controllers;

import javafx.collections.ObservableList;
import models.Item;
import models.Offer;
import models.Validation;

public class OfferController {
	public OfferController() {
		
	}
	
	public Validation createOffer(String offerPriceText, int userId, Item selectedItem) {
		if (offerPriceText.isEmpty()) {
			return new Validation(false, "Validation Error", "Offer price field cannot be empty.");
		}
		
		double offerPrice = 0;
		try {
			offerPrice = Double.parseDouble(offerPriceText);
			if (offerPrice <= 0) {
				return new Validation(false, "Validation Error", "Offer price must be a positive number greater than 0.");
			}
			double currentItemPrice = selectedItem.getPrice();
			if (offerPrice >= currentItemPrice) {
				return new Validation(false, "Validation Error", String.format("Offer price must be less than current item price: %.2f", currentItemPrice));
			}
		} catch (NumberFormatException e) {
			return new Validation(false, "Validation Error", "Offer price must be a valid number.");
		}
		
		Validation response = Offer.createOffer(offerPrice, userId, selectedItem.getItemId());
		return new Validation(response.getStatus(), response.getTitle(), response.getMessage());
	}
	
	public boolean acceptOffer(int offerId) {
		return Offer.acceptOffer(offerId);
	}
	
	public boolean declineOffer(int offerId, String reason) {
		return Offer.declineOffer(offerId, reason);
	}
	
	public ObservableList<Offer> viewOfferItemForSeller(int itemId) {
		return Offer.viewOfferItemForSeller(itemId);
	}
}
