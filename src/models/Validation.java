package models;

import javafx.scene.control.Alert;

public class Validation {
	private boolean status;
	private String title;
	private String message;
	
	public Validation(boolean status, String title, String message) {
		this.status = status;
		this.title = title;
		this.message = message;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Validation [status=" + status + ", title=" + title + ", message=" + message + "]";
	}
	
	public void showAlert() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
