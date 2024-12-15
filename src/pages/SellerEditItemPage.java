package pages;

import controllers.ItemController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Item;
import models.User;

public class SellerEditItemPage {
	private Stage stage;
	private Scene scene;
	private ItemController itemController;
	private Item itemToEdit;
	private User seller;

	private TextField nameField;
	private TextField categoryField;
	private TextField sizeField;
	private TextField priceField;
	private Button saveButton;
	private Button cancelButton;
	private Label validationLabel;

	public SellerEditItemPage(Stage stage, Item item, User seller) {
		this.stage = stage;
		this.seller = seller;
		this.itemController = new ItemController();
		this.itemToEdit = item;
		initializeUI();
	}

	private void initializeUI() {
		nameField = new TextField(itemToEdit.getName());
		categoryField = new TextField(itemToEdit.getCategory());
		sizeField = new TextField(itemToEdit.getSize());
		priceField = new TextField(String.valueOf(itemToEdit.getPrice()));

		saveButton = new Button("Save");
		cancelButton = new Button("Cancel");
		validationLabel = new Label();
		validationLabel.setStyle("-fx-text-fill: red;");

		saveButton.setOnAction(event -> handleSave());

		cancelButton.setOnAction(event -> handleCancel());

		VBox layout = new VBox(10, nameField, categoryField, sizeField, priceField, saveButton, cancelButton,
				validationLabel);
		layout.setPadding(new Insets(20));
		scene = new Scene(layout, 400, 300);
	}

	private void handleSave() {
		String name = nameField.getText();
		String category = categoryField.getText();
		String size = sizeField.getText();
		String priceText = priceField.getText();

		if (name.isEmpty() || name.length() < 3) {
			validationLabel.setText("Item Name cannot be empty and must be at least 3 characters.");
			return;
		}

		if (category.isEmpty() || category.length() < 3) {
			validationLabel.setText("Item Category cannot be empty and must be at least 3 characters.");
			return;
		}

		if (size.isEmpty()) {
			validationLabel.setText("Item Size cannot be empty.");
			return;
		}

		double price = 0;
		try {
			price = Double.parseDouble(priceText);
			if (price <= 0) {
				validationLabel.setText("Item Price must be greater than 0.");
				return;
			}
		} catch (NumberFormatException e) {
			validationLabel.setText("Item Price must be a valid number.");
			return;
		}

		itemToEdit.setName(name);
		itemToEdit.setCategory(category);
		itemToEdit.setSize(size);
		itemToEdit.setPrice(price);

		boolean success = itemController.updateItem(itemToEdit);
		if (success) {
			SellerViewMyItemPage previousPage = new SellerViewMyItemPage(stage, seller);
			stage.setScene(previousPage.getScene());
		} else {
			validationLabel.setText("Failed to save the item. Please try again.");
		}
	}

	private void handleCancel() {
		SellerViewMyItemPage previousPage = new SellerViewMyItemPage(stage, seller);
		stage.setScene(previousPage.getScene());
	}

	public Scene getScene() {
		return this.scene;
	}
}
