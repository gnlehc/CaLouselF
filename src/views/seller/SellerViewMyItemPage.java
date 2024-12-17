package views.seller;

import java.util.Optional;

import controllers.ItemController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Item;
import models.User;

public class SellerViewMyItemPage {
	private Stage stage;
	private Scene scene;
	private TableView<Item> tableView;
	private ItemController itemController;
	private User seller;

	public SellerViewMyItemPage(Stage stage, User seller) {
		this.stage = stage;
		this.seller = seller;
		itemController = new ItemController();
		initializeUI(seller.getId());
	}

	@SuppressWarnings("unchecked")
	private void initializeUI(int sellerId) {
		tableView = new TableView<>();

		TableColumn<Item, String> itemNameCol = new TableColumn<>("Item Name");
		itemNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Item, String> itemCategoryCol = new TableColumn<>("Item Category");
		itemCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

		TableColumn<Item, String> itemSizeCol = new TableColumn<>("Item Size");
		itemSizeCol.setCellValueFactory(new PropertyValueFactory<>("Size"));

		TableColumn<Item, String> itemPriceCol = new TableColumn<>("Item Price");
		itemPriceCol.setCellValueFactory(data -> 
        new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));

		TableColumn<Item, String> itemStatusCol = new TableColumn<>("Item Status");
		itemStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

		tableView.getColumns().addAll(itemNameCol, itemCategoryCol, itemSizeCol, itemPriceCol, itemStatusCol);

		refreshTableData();

		Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());

		Button editButton = new Button("Edit Item");
		editButton.setOnAction(event -> handleEdit());
		
		Button deleteButton = new Button("Delete Item");
		deleteButton.setOnAction(event -> handleDelete());

		VBox layout = new VBox(10, tableView, backButton, editButton, deleteButton);
		layout.setPadding(new Insets(20));
		scene = new Scene(layout, 600, 400);
	}

	private void handleBack() {
		SellerHomePage previousPage = new SellerHomePage(stage, seller);
		stage.setScene(previousPage.getScene());
	}

	private void handleEdit() {
		Item selectedItem = tableView.getSelectionModel().getSelectedItem();

		if (selectedItem != null) {
			SellerEditItemPage sellerEditItemPage = new SellerEditItemPage(stage, selectedItem, seller);
			stage.setScene(sellerEditItemPage.getScene());
		} else {
			showAlert("No Item Selected", "Please select an item to edit.");
		}
	}
	
	private void handleDelete() {
		Item selectedItem = tableView.getSelectionModel().getSelectedItem();
		
		if (selectedItem != null) {
			Optional<ButtonType> result = showConfirmation("Confirm Delete", "Are you sure you want to delete this item?");
			
			if (result.isPresent()) {
				if (result.get() == ButtonType.YES) {
					boolean response = itemController.deleteItem(selectedItem.getItemId());
					if (response == true) {
						refreshTableData();
						showAlert("Success", "Item deleted successfully.");
					} else {
						showAlert("Error", "An error occurred while deleting the item. Please try again.");
					}
				}
			}
		} else {
			showAlert("No Item Selected", "Please select an item to delete.");
		}
	}
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	private Optional<ButtonType> showConfirmation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
		alert.setTitle(title);
		return alert.showAndWait();
	}
	
	private void refreshTableData() {
		tableView.getItems().clear();
		ObservableList<Item> items = itemController.getItemsBySellerId(seller.getId());
		tableView.setItems(items);
	}

	public Scene getScene() {
		return this.scene;
	}
}
