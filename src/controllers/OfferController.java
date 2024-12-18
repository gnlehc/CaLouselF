package controllers;

import models.Offer;
import models.Validation;

public class OfferController {
	public OfferController() {
		
	}
	
	public Validation createOffer(String offerPriceText, int userId, int itemId) {
		if (offerPriceText.isEmpty()) {
			return new Validation(false, "Validation Error", "Offer price field cannot be empty.");
		}
		
		double offerPrice = 0;
		try {
			offerPrice = Double.parseDouble(offerPriceText);
			if (offerPrice <= 0) {
				return new Validation(false, "Validation Error", "Offer price must be a positive number greater than 0.");
			}
		} catch (NumberFormatException e) {
			return new Validation(false, "Validation Error", "Offer price must be a valid number.");
		}
		
		Validation response = Offer.createOffer(offerPrice, userId, itemId);
		return new Validation(response.getStatus(), response.getTitle(), response.getMessage());
	}
}
