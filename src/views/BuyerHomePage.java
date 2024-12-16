package views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;

public class BuyerHomePage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private Button viewItemsButton, viewWishlistButton, viewPurchaseHistoryButton;
	private User loggedInUser;
	
	public BuyerHomePage(Stage stage, User loggedInUser) {
		this.stage = stage;
		this.loggedInUser = loggedInUser;
		this.stage.setTitle("Buyer Home");
		initialize();
		setLayout();
		setAlignment();
		this.scene = new Scene(borderPane, 500, 500);
	}

	private void initialize() {
		borderPane = new BorderPane();
		gridPane = new GridPane();
		Button logoutButton = new Button("Logout");
		viewItemsButton = new Button("View Items");
		viewWishlistButton = new Button("View Wishlist");
		viewPurchaseHistoryButton = new Button("View Purchase History");

		viewItemsButton.setOnAction(event -> {
			BuyerViewItemPage viewItemPage = new BuyerViewItemPage(stage, loggedInUser);
			stage.setScene(viewItemPage.getScene());
		});
		logoutButton.setOnAction(event -> handleLogout());
		viewWishlistButton.setOnAction(event -> {
			BuyerViewWishListPage wishlistPage = new BuyerViewWishListPage(stage, loggedInUser);
			stage.setScene(wishlistPage.getScene());
		});
//
//		viewPurchaseHistoryButton.setOnAction(event -> {
//			// Open the page for viewing purchase history
//			new ViewPurchaseHistoryPage(stage);
//		});

		gridPane.add(viewItemsButton, 0, 0);
		gridPane.add(viewWishlistButton, 0, 1);
		gridPane.add(viewPurchaseHistoryButton, 0, 2);
		gridPane.add(logoutButton, 0, 3);
		borderPane.setCenter(gridPane);
	}

	private void handleLogout() {
		LoginPage loginPage = new LoginPage(stage);
		stage.setScene(loginPage.getScene());
		stage.setTitle("Login - Sustainable Fashion Marketplace");
	}

	private void setLayout() {
		gridPane.setHgap(10);
		gridPane.setVgap(10);
	}

	private void setAlignment() {
		BorderPane.setMargin(gridPane, new Insets(10));
	}

	public Scene getScene() {
		return this.scene;
	}
}
