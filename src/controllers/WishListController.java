package controllers;

import java.util.ArrayList;

import models.Item;
import models.WishList;

public class WishListController {
	public WishListController() {
		
	}

	public boolean addToWishlist(Item item, int userId) {
		if (item == null) {
			return false;
		}

		return WishList.addToWishlist(item, userId);
	}

	public boolean isItemInWishlist(int userId, int itemId) {
		return WishList.isItemInWishlist(userId, itemId);
	}

	public ArrayList<Item> getWishlistItems(int userId) {
		return WishList.getWishlistItems(userId);
	}

	public boolean removeFromWishlist(int userId, int itemId) {
	    return WishList.removeFromWishlist(userId, itemId);
	}

}