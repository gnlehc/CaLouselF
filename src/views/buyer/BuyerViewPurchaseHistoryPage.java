package views.buyer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Transaction;
import models.User;

public class BuyerViewPurchaseHistoryPage {
	private Stage stage;
    private Scene scene;
    private User buyer;
    private TableView<Transaction> tableView;
    private ObservableList<Transaction> transactions;
    
	public BuyerViewPurchaseHistoryPage(Stage stage, User buyer, ObservableList<Transaction> transactions) {
		this.stage = stage;
		this.buyer = buyer;
		this.tableView = new TableView<>();
		this.transactions = transactions;
		initializeView();
	}
	
	private void initializeView() {
		BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        
        Label titleLabel = new Label("My Purchase History");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().add(titleLabel);
        
        borderPane.setTop(headerBox);
        borderPane.setCenter(createTableView());
        
        scene = new Scene(borderPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("My Purchase History");
        stage.show();
	}
    
	private VBox createTableView() {
		VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        TableColumn<Transaction, String> transactionIdCol = new TableColumn<>("Transaction Id");
        transactionIdCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%d", data.getValue().getTransactionId())));
        
        TableColumn<Transaction, String> userIdCol = new TableColumn<>("User Id");
        userIdCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%d", data.getValue().getUserId())));
        
        TableColumn<Transaction, String> itemIdCol = new TableColumn<>("Item Id");
        itemIdCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%d", data.getValue().getItemId())));
        
        TableColumn<Transaction, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getItem().getName()));
        
        TableColumn<Transaction, String> itemCategoryCol = new TableColumn<>("Item Category");
        itemCategoryCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getItem().getCategory()));
        
        TableColumn<Transaction, String> itemSizeCol = new TableColumn<>("Item Size");
        itemSizeCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getItem().getSize()));
        
        TableColumn<Transaction, String> itemPriceCol = new TableColumn<>("Item Price");
        itemPriceCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%.2f", data.getValue().getItem().getPrice())));
        
        TableColumn<Transaction, String> itemStatusCol = new TableColumn<>("Item Status");
        itemStatusCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getItem().getStatus()));
        
        TableColumn<Transaction, String> sellerIdCol = new TableColumn<>("Seller Id");
        sellerIdCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.format("%d", data.getValue().getItem().getSellerId())));
        
        tableView.getColumns().addAll(transactionIdCol, userIdCol, itemIdCol, itemNameCol, itemCategoryCol, itemSizeCol, itemPriceCol, itemStatusCol, sellerIdCol);
        
        tableView.setItems(transactions);
        
        Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());
        
		vbox.getChildren().addAll(tableView, backButton);
        return vbox;
	}
	
	private void handleBack() {
		BuyerHomePage previousPage = new BuyerHomePage(stage, buyer);
		stage.setScene(previousPage.getScene());
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
