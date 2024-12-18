package views.seller;

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
import models.Offer;
import models.User;

public class SellerViewOfferPage {
	private Stage stage;
	private Scene scene;
	private User seller;
	private TableView<Offer> tableView;
	private ObservableList<Offer> offers;
	
	public SellerViewOfferPage(Stage stage, User seller, ObservableList<Offer> offers) {
		this.stage = stage;
		this.seller = seller;
		this.tableView = new TableView<>();
		this.offers = offers;
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
        
        Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());
		
		vbox.getChildren().addAll(tableView, backButton);
        return vbox;
	}
	
	private void handleBack() {
		SellerViewMyItemPage previousPage = new SellerViewMyItemPage(stage, seller);
		stage.setScene(previousPage.getScene());
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
