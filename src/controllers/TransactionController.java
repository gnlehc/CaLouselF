package controllers;

import javafx.collections.ObservableList;
import models.Transaction;

public class TransactionController {
	public TransactionController() {

	}

	public boolean purchaseItem(int userId, int itemId) {
		return createTransaction(new Transaction(userId, itemId));
	}
	
	public ObservableList<Transaction> viewHistory(int userId) {
		return Transaction.viewHistory(userId);
	}
	
	public boolean createTransaction(Transaction newTransaction) {
		return Transaction.createTransaction(newTransaction);
	}
}
