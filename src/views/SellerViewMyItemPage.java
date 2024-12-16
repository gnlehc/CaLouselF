package views;

import controllers.ItemController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

	private void initializeUI(int sellerId) {
		tableView = new TableView<>();

		TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
		categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

		TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
		sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Size"));

		TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

		TableColumn<Item, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

		tableView.getColumns().addAll(nameColumn, categoryColumn, sizeColumn, priceColumn, statusColumn);

		ObservableList<Item> items = itemController.getItemsBySellerId(sellerId);
		tableView.setItems(items);

		Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());

		Button editButton = new Button("Edit Item");
		editButton.setOnAction(event -> handleEdit());

		VBox layout = new VBox(10, tableView, backButton, editButton);
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
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Item Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select an item to edit.");
			alert.showAndWait();
		}
	}

	public Scene getScene() {
		return this.scene;
	}
}
