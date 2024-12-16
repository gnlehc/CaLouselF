package controllers;

import models.Transaction;

public class TransactionController {
	public boolean purchaseItem(int userId, int itemId) {
		return Transaction.purchaseItem(userId, itemId);
	}
	
	public void viewHistory(int userId) {
		
	}
	
	public void createTransaction(int transactionId) {
		
	}
}
