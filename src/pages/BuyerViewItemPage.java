package pages;

import controllers.ItemController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Item;

public class BuyerViewItemPage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private ListView<Item> itemListView;
	private ItemController itemController;

	public BuyerViewItemPage(Stage stage) {
		this.stage = stage;
		initialize();
		setLayout();
		setAlignment();
		this.scene = new Scene(borderPane, 500, 500);
	}

	private void initialize() {
		itemController = new ItemController();
		borderPane = new BorderPane();
		gridPane = new GridPane();

		itemListView = new ListView<>();
		itemListView.setItems(FXCollections.observableArrayList(itemController.getApprovedItems()));

		Button purchaseButton = new Button("Purchase");
		purchaseButton.setOnAction(event -> handlePurchase());

		Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());

		gridPane.add(itemListView, 0, 0);
		gridPane.add(purchaseButton, 0, 1);
		gridPane.add(backButton, 0, 2);
		borderPane.setCenter(gridPane);
	}

	private void setLayout() {
		gridPane.setHgap(10);
		gridPane.setVgap(10);
	}

	private void setAlignment() {
		BorderPane.setMargin(gridPane, new Insets(10));
	}

	private void handlePurchase() {
		Item selectedItem = itemListView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			showAlert("Confirm Purchase", "Are you sure you want to purchase this item?");
		}
	}

	private void handleBack() {
		BuyerHomePage previousPage = new BuyerHomePage(stage);
		stage.setScene(previousPage.getScene());
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
		alert.setTitle(title);
		alert.showAndWait();
	}

	public Scene getScene() {
		return this.scene;
	}
}
