package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Item;
import models.Transaction;

public class TransactionController {
	private DatabaseConnection db;
	
	public TransactionController() {
		this.db = new DatabaseConnection();
	}

	public boolean purchaseItem(int userId, int itemId) {
		return createTransaction(new Transaction(userId, itemId));
	}
	
	public ObservableList<Transaction> viewHistory(int userId) {
		ObservableList<Transaction> transactions = FXCollections.observableArrayList();
		
		String query = "SELECT * FROM transactions AS T JOIN items AS I ON T.item_id = I.item_id WHERE T.user_id = ?";
		
		try (Connection conn = db.connection; PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Transaction transaction = new Transaction(rs.getInt("user_id"), rs.getInt("item_id"));
				transaction.setTransactionId(rs.getInt("transaction_id"));
				
				Item item = new Item(rs.getString("name"), rs.getString("category"), rs.getString("size"),
						rs.getDouble("price"), rs.getString("status"), rs.getInt("sellerId"));
				item.setItemId(rs.getInt("item_id"));
				transaction.setItem(item);
				
				transactions.add(transaction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return transactions;
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
