package views.buyer;
import java.util.Optional;

import controllers.TransactionController;
import controllers.WishListController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Item;
import models.User;

public class BuyerViewWishListPage {

	private Stage stage;
    private Scene scene;
    private User buyer;
    private WishListController wishlistController;
    private TransactionController transactionController;
    private TableView<Item> tableView;
    
    public BuyerViewWishListPage(Stage stage, User buyer) {
        this.stage = stage;
        this.buyer = buyer;
        this.wishlistController = new WishListController();
        this.transactionController = new TransactionController();
        this.tableView = new TableView<>();
        initializeView();
    }
    
    private void initializeView() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        
        Label titleLabel = new Label("My Wishlist");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().add(titleLabel);
        
        borderPane.setTop(headerBox);
        borderPane.setCenter(createTableView());
        
        scene = new Scene(borderPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("My Wishlist");
        stage.show();
    }
    private void handleBack() {
		BuyerHomePage previousPage = new BuyerHomePage(stage, buyer);
		stage.setScene(previousPage.getScene());
	}
    @SuppressWarnings("unchecked")
	private VBox createTableView() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        TableColumn<Item, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Item, String> itemCategoryCol = new TableColumn<>("Item Category");
        itemCategoryCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getCategory()));
        
        TableColumn<Item, String> itemSizeCol = new TableColumn<>("Item Size");
        itemSizeCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getSize()));
        
        TableColumn<Item, String> itemPriceCol = new TableColumn<>("Item Price");
        itemPriceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));
        
        TableColumn<Item, Void> removeItemCol = new TableColumn<>("Remove Item");
        removeItemCol.setCellFactory(col -> {
            TableCell<Item, Void> cell = new TableCell<>() {
                private final Button removeBtn = new Button("Remove");
                {
                    removeBtn.setOnAction(e -> {
                        Item item = getTableView().getItems().get(getIndex());
                        handleRemoveFromWishlist(item);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : removeBtn);
                }
            };
            return cell;
        });
        
        TableColumn<Item, Void> purchaseItemCol = new TableColumn<>("Purchase Item");
        purchaseItemCol.setCellFactory(col -> {
            TableCell<Item, Void> cell = new TableCell<>() {
                private final Button purchaseBtn = new Button("Purchase");
                {
                	purchaseBtn.setOnAction(e -> {
                        Item item = getTableView().getItems().get(getIndex());
                        handlePurchaseFromWishlist(item);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : purchaseBtn);
                }
            };
            return cell;
        });
        
        tableView.getColumns().addAll(itemNameCol, itemCategoryCol, itemSizeCol, itemPriceCol, removeItemCol, purchaseItemCol);
        refreshTableData();
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshTableData());
        
        Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());
		
        vbox.getChildren().addAll(tableView, refreshBtn, backButton);
        return vbox;
    }
    
    private void handleRemoveFromWishlist(Item item) {
        if(wishlistController.removeFromWishlist(buyer.getId(), item.getItemId())) {
            refreshTableData();
            showAlert("Success", "Item removed from wishlist!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to remove item!", Alert.AlertType.ERROR);
        }
    }
    
    private void handlePurchaseFromWishlist(Item item) {
		Optional<ButtonType> result = showConfirmation("Confirm Purchase", "Are you sure you want to purchase this item?");
		
		if (result.isPresent()) {
			if (result.get() == ButtonType.YES) {
				boolean removeWishlist = wishlistController.removeFromWishlist(buyer.getId(), item.getItemId());
				
				if (removeWishlist) {
					boolean response = transactionController.purchaseItem(buyer.getId(), item.getItemId());
					
					if (response == true) {
						refreshTableData();
						showAlert("Success", "Item purchased successfully and moved to purchase history.", Alert.AlertType.INFORMATION);
					} else {
						showAlert("Error", "An error occurred while purchasing the item. Please try again.", Alert.AlertType.ERROR);
					}
				} else {
					showAlert("Error", "An error occurred while purchasing the item. Please try again.", Alert.AlertType.ERROR);
				}
			}
		}
    }
    
    private void refreshTableData() {
        tableView.getItems().clear();
        tableView.getItems().addAll(wishlistController.getWishlistItems(buyer.getId()));
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private Optional<ButtonType> showConfirmation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
		alert.setTitle(title);
		return alert.showAndWait();
	}

	public Scene getScene() {
		return this.scene;
	}
}