package controllers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DatabaseConnection;
import models.Transaction;

public class TransactionController {
	private DatabaseConnection db;
	
	public TransactionController() {
		this.db = new DatabaseConnection();
	}

	public boolean purchaseItem(int userId, int itemId) {
		return createTransaction(new Transaction(userId, itemId));
	}
	
	public void viewHistory(int userId) {
		
	}
	
	public boolean createTransaction(Transaction newTransaction) {
		String query = "INSERT INTO transactions (user_id, item_id) VALUES (?, ?)";
		try (PreparedStatement preparedStatement = db.connection.prepareStatement(query)) {
			preparedStatement.setInt(1, newTransaction.getUserId());
			preparedStatement.setInt(2, newTransaction.getItemId());
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
