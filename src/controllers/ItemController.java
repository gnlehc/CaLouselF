package controllers;

import javafx.collections.ObservableList;
import models.Item;

public class ItemController {
	public ItemController() {

	}

	public boolean uploadItem(Item item) {
		return Item.uploadItem(item);
	}

	public boolean validateItem(Item item) {
		if (item.getName().isEmpty() || item.getName().length() < 3) {
			return false;
		}
		if (item.getCategory().isEmpty() || item.getCategory().length() < 3) {
			return false;
		}
		if (item.getSize().isEmpty()) {
			return false;
		}
		if (item.getPrice() <= 0) {
			return false;
		}
		return true;
	}

	public boolean updateItem(Item item) {
		if (!validateItem(item)) {
			return false;
		}

		return Item.updateItem(item);
	}

	public ObservableList<Item> getItemsBySellerId(int sellerId) {
		return Item.getItemsBySellerId(sellerId);
	}

	public ObservableList<Item> getApprovedItems() {
		return Item.getApprovedItems();
	}

	public void insertDefaultItems() {
		uploadItem(new Item("Jeans", "Bottom", "Medium", 50.0, "Approved", 1));
		uploadItem(new Item("Shirt", "Top", "Large", 100.0, "Approved", 1));
		uploadItem(new Item("T-Shirt", "Top", "Small", 20.0, "Pending", 1));
		uploadItem(new Item("Airism", "Top", "Medium", 150.0, "Approved", 2));
		uploadItem(new Item("UV Jacket", "Outer", "Large", 75.0, "Pending", 2));
	}

	public ObservableList<Item> getPendingItems() {
		return Item.getPendingItems();
	}

	public boolean approveItem(int itemId) {
		return Item.approveItem(itemId);
	}
	
	public boolean deleteItem(int itemId) {
		return Item.deleteItem(itemId);
	}

	public boolean declineItem(int itemId, String reason) {
		return Item.logDeclineReason(itemId, reason) && deleteItem(itemId);
	}
}
