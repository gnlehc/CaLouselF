package models;

public class Transaction {
	private int transactionId;
	private int userId;
	private int itemId;
	
	public Transaction(int userId, int itemId) {
		this.userId = userId;
		this.itemId = itemId;
	}
	
	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
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

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", userId=" + userId + ", itemId=" + itemId + "]";
	}
}
