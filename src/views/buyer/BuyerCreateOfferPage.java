package views.buyer;

import controllers.OfferController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Item;
import models.User;
import models.Validation;

public class BuyerCreateOfferPage {
	private Stage stage;
	private Scene scene;
	private GridPane gridPane;
	private Label itemNameLabel, itemCategoryLabel, itemSizeLabel, itemPriceLabel, offerPriceLabel;
	private Text itemNameText, itemCategoryText, itemSizeText, itemPriceText;
	private TextField offerPriceField;
	
	private User loggedUser;
	private Item selectedItem;
	
	private OfferController offerController;
	
	public BuyerCreateOfferPage(Stage stage, User loggedUser, Item selectedItem) {
		this.stage = stage;
		this.loggedUser = loggedUser;
		this.selectedItem = selectedItem;
		offerController = new OfferController();
		
		initialize();
		setLayout();
		this.scene = new Scene(gridPane, 400, 300);
	}
	
	private void initialize() {
		gridPane = new GridPane();
		
		itemNameLabel = new Label("Item Name:");
		itemCategoryLabel = new Label("Item Category:");
		itemSizeLabel = new Label("Item Size:");
		itemPriceLabel = new Label("Current Item Price:");
		offerPriceLabel = new Label("Offer Price:");
		
		itemNameText = new Text(selectedItem.getName());
		itemCategoryText = new Text(selectedItem.getCategory());
		itemSizeText = new Text(selectedItem.getSize());
		itemPriceText = new Text(String.format("%.2f", selectedItem.getPrice()));
		
		offerPriceField = new TextField();
		
		Button createOfferButton = new Button("Create Offer");
		createOfferButton.setOnAction(event -> handleCreateOffer());
		
		Button backButton = new Button("Back");
		backButton.setOnAction(event -> handleBack());
		
		gridPane.add(itemNameLabel, 0, 0);
		gridPane.add(itemNameText, 1, 0);
		gridPane.add(itemCategoryLabel, 0, 1);
		gridPane.add(itemCategoryText, 1, 1);
		gridPane.add(itemSizeLabel, 0, 2);
		gridPane.add(itemSizeText, 1, 2);
		gridPane.add(itemPriceLabel, 0, 3);
		gridPane.add(itemPriceText, 1, 3);
		gridPane.add(offerPriceLabel, 0, 4);
		gridPane.add(offerPriceField, 1, 4);
		gridPane.add(createOfferButton, 0, 5);
		gridPane.add(backButton, 0, 6);
	}
	
	private void setLayout() {
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));
	}
	
	private void handleBack() {
		BuyerViewItemPage previousPage = new BuyerViewItemPage(stage, loggedUser);
		stage.setScene(previousPage.getScene());
	}
	
	private void handleCreateOffer() {
		String offerPriceText = offerPriceField.getText();
		Validation validate = offerController.createOffer(offerPriceText, loggedUser.getId(), selectedItem);
		validate.showAlert();
		if (validate.getStatus()) {
			clearFields();
		}
	}
	
	private void clearFields() {
		offerPriceField.clear();
	}

	public Scene getScene() {
		return this.scene;
	}
}
