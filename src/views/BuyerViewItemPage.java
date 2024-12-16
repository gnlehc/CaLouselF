package views;

import controllers.ItemController;
import controllers.WishListController;
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
import models.User;

public class BuyerViewItemPage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private ListView<Item> itemListView;
	private ItemController itemController;
	private WishListController wishListController;
	private User loggedUser;
	
	public BuyerViewItemPage(Stage stage, User loggedUser) {
		this.stage = stage;
		this.loggedUser = loggedUser;
		initialize();
		setLayout();
		setAlignment();
		this.scene = new Scene(borderPane, 500, 500);
	}

	private void initialize() {
		itemController = new ItemController();
		wishListController = new WishListController();
		borderPane = new BorderPane();
		gridPane = new GridPane();

		itemListView = new ListView<>();
		itemListView.setItems(FXCollections.observableArrayList(itemController.getApprovedItems()));

		Button purchaseButton = new Button("Purchase");
		purchaseButton.setOnAction(event -> handlePurchase());
		
		Button addToWishlistButton = new Button("Add to Wishlist");
	    addToWishlistButton.setOnAction(event -> handleAddToWishlist());
	        
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());

		gridPane.add(itemListView, 0, 0);
		gridPane.add(purchaseButton, 0, 1);
		gridPane.add(addToWishlistButton, 0, 2);
		gridPane.add(backButton, 0, 3);
		borderPane.setCenter(gridPane);
	}
	 private void handleAddToWishlist() {
	    Item selectedItem = itemListView.getSelectionModel().getSelectedItem();
	      if (selectedItem != null) {
	         boolean success = wishListController.addToWishlist(selectedItem, loggedUser.getId());
	         if (success) {
	            showAlert("Wishlist Updated", "Item added to your wishlist.");
	         } else {
	            showAlert("Error", "Failed to add the item to your wishlist. Please try again.");
	         }
	     } else {
	        showAlert("No Item Selected", "Please select an item to add to your wishlist.");
	     }
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
		BuyerHomePage previousPage = new BuyerHomePage(stage, loggedUser);
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
