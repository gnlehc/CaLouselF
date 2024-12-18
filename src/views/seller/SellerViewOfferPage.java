package views.seller;

import java.util.Optional;

import controllers.OfferController;
import controllers.TransactionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Offer;
import models.Transaction;
import models.User;

public class SellerViewOfferPage {
	private Stage stage;
	private Scene scene;
	private User seller;
	private TableView<Offer> tableView;
	private ObservableList<Offer> offers;
	private OfferController offerController;
	private TransactionController transactionController;
	
	public SellerViewOfferPage(Stage stage, User seller, ObservableList<Offer> offers) {
		this.stage = stage;
		this.seller = seller;
		this.tableView = new TableView<>();
		this.offers = offers;
		this.offerController = new OfferController();
		this.transactionController = new TransactionController();
		initializeView();
	}
	
	private void initializeView() {
		BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Item Price Offers");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().add(titleLabel);
        
        borderPane.setTop(headerBox);
        borderPane.setCenter(createTableView());
        
        scene = new Scene(borderPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Item Price Offers");
        stage.show();
	}
	
	@SuppressWarnings("unchecked")
	private VBox createTableView() {
		VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        TableColumn<Offer, String> offerIdCol = new TableColumn<>("Offer Id");
        offerIdCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%d", data.getValue().getOfferId())));
        
        TableColumn<Offer, String> offerPriceCol = new TableColumn<>("Offer Price");
        offerPriceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%.2f", data.getValue().getOfferPrice())));
        
        TableColumn<Offer, String> offerStatusCol = new TableColumn<>("Offer Status");
        offerStatusCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getOfferStatus()));
        
        TableColumn<Offer, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getItem().getName()));
        
        TableColumn<Offer, String> itemCategoryCol = new TableColumn<>("Item Category");
        itemCategoryCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getItem().getCategory()));
        
        TableColumn<Offer, String> itemSizeCol = new TableColumn<>("Item Size");
        itemSizeCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getItem().getSize()));
        
        TableColumn<Offer, String> itemPriceCol = new TableColumn<>("Item Price");
        itemPriceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%.2f", data.getValue().getItem().getPrice())));
        
        tableView.getColumns().addAll(offerIdCol, offerPriceCol, offerStatusCol, itemNameCol, itemCategoryCol, itemSizeCol, itemPriceCol);
        
        tableView.setItems(offers);
        
        Button acceptOfferButton = new Button("Accept Offer");
		acceptOfferButton.setOnAction(event -> handleAcceptOffer());
		
		Button declineOfferButton = new Button("Decline Offer");
		declineOfferButton.setOnAction(event -> handleDeclineOffer());
        
        Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());
		
		vbox.getChildren().addAll(tableView, acceptOfferButton, declineOfferButton, backButton);
        return vbox;
	}
	
	private void handleAcceptOffer() {
		Offer selectedOffer = tableView.getSelectionModel().getSelectedItem();
		
		if (selectedOffer != null) {
			Optional<ButtonType> result = showConfirmation("Confirm Accept", "Are you sure you want to accept this offer?");
			
			if (result.isPresent()) {
				if (result.get() == ButtonType.YES) {
					boolean response =  transactionController.createTransaction(new Transaction(selectedOffer.getUserId(), selectedOffer.getItemId())) && offerController.acceptOffer(selectedOffer.getOfferId());
					if (response) {
						refreshTableData(selectedOffer);
						showAlert("Success", "Offer accepted successfully & the item has been successfully purchased by the user automatically.");
					} else {
						showAlert("Error", "An error occurred while accepting the offer. Please try again.");
					}
				}
			}
		} else {
			showAlert("No Item Selected", "Please select an offer to accept.");
		}
	}
	
	private void handleDeclineOffer() {
		Offer selectedOffer = tableView.getSelectionModel().getSelectedItem();
		
		if (selectedOffer != null) {
			Optional<ButtonType> result = showConfirmation("Confirm Decline", "Are you sure you want to decline this offer?");
			
			if (result.isPresent()) {
				if (result.get() == ButtonType.YES) {
					TextInputDialog dialog = new TextInputDialog();
			        dialog.setTitle("Decline Offer");
			        dialog.setHeaderText("Reason for declining the offer:");
			        dialog.setContentText("Reason:");
			        
			        dialog.showAndWait().ifPresent(reason -> {
			            if (reason.trim().isEmpty()) {
			                showAlert("Error", "Reason cannot be empty.");
			            } else {
			            	boolean response = offerController.declineOffer(selectedOffer.getOfferId(), reason);
			                if (response) {
			                	refreshTableData(selectedOffer);
			                	showAlert("Success", "Offer declined successfully.");
			                } else {
			                	showAlert("Error", "An error occurred while declining the offer. Please try again.");
			                }
			            }
			        });
				}
			}
		} else {
			showAlert("No Item Selected", "Please select an offer to decline.");
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
	
	private void handleBack() {
		SellerViewMyItemPage previousPage = new SellerViewMyItemPage(stage, seller);
		stage.setScene(previousPage.getScene());
	}
	
	private void refreshTableData(Offer selectedOffer) {
		tableView.getItems().remove(selectedOffer);
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
