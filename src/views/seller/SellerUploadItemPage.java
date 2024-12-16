package views.seller;

import controllers.ItemController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Item;
import models.User;

public class SellerUploadItemPage {
	private Stage stage;
	private Scene scene;
	private GridPane gridPane;
	private TextField itemNameField, itemCategoryField, itemSizeField, itemPriceField;
	private ItemController itemController;
	private User seller;

	public SellerUploadItemPage(Stage stage, User seller) {
		this.stage = stage;
		this.seller = seller;
		itemController = new ItemController();
		initialize();
		setLayout();
		this.scene = new Scene(gridPane, 400, 300);
	}

	private void initialize() {
		gridPane = new GridPane();

		Label itemNameLabel = new Label("Item Name:");
		Label itemCategoryLabel = new Label("Item Category:");
		Label itemSizeLabel = new Label("Item Size:");
		Label itemPriceLabel = new Label("Item Price:");

		itemNameField = new TextField();
		itemCategoryField = new TextField();
		itemSizeField = new TextField();
		itemPriceField = new TextField();

		Button submitButton = new Button("Upload Item");
		submitButton.setOnAction(event -> handleUploadItem());
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());

		gridPane.add(itemNameLabel, 0, 0);
		gridPane.add(itemNameField, 1, 0);
		gridPane.add(itemCategoryLabel, 0, 1);
		gridPane.add(itemCategoryField, 1, 1);
		gridPane.add(itemSizeLabel, 0, 2);
		gridPane.add(itemSizeField, 1, 2);
		gridPane.add(itemPriceLabel, 0, 3);
		gridPane.add(itemPriceField, 1, 3);
		gridPane.add(submitButton, 1, 4);
		gridPane.add(backButton, 1, 5);
	}

	private void handleBack() {
		SellerHomePage previousPage = new SellerHomePage(stage, seller);
		stage.setScene(previousPage.getScene());
	}

	private void setLayout() {
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));
	}

	private void handleUploadItem() {
		String itemName = itemNameField.getText();
		String itemCategory = itemCategoryField.getText();
		String itemSize = itemSizeField.getText();
		String itemPriceText = itemPriceField.getText();

		if (itemName.isEmpty() || itemName.length() < 3) {
			showAlert("Validation Error", "Item name cannot be empty and must be at least 3 characters long.");
			return;
		}

		if (itemCategory.isEmpty() || itemCategory.length() < 3) {
			showAlert("Validation Error", "Item category cannot be empty and must be at least 3 characters long.");
			return;
		}

		if (itemSize.isEmpty()) {
			showAlert("Validation Error", "Item size cannot be empty.");
			return;
		}

		double itemPrice = 0;
		try {
			itemPrice = Double.parseDouble(itemPriceText);
			if (itemPrice <= 0) {
				showAlert("Validation Error", "Item price must be a positive number greater than 0.");
				return;
			}
		} catch (NumberFormatException e) {
			showAlert("Validation Error", "Item price must be a valid number.");
			return;
		}

		Item item = new Item(itemName, itemCategory, itemSize, itemPrice, "Pending", seller.getId());

		if (itemController.uploadItem(item)) {
			showAlert("Success", "Item uploaded successfully and sent to admin for approval.");
			clearFields();
		} else {
			showAlert("Error", "An error occurred while uploading the item. Please try again.");
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void clearFields() {
		itemNameField.clear();
		itemCategoryField.clear();
		itemSizeField.clear();
		itemPriceField.clear();
	}

	public Scene getScene() {
		return this.scene;
	}
}
