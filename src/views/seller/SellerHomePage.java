package views.seller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;
import views.auth.LoginPage;

public class SellerHomePage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private Button uploadItemButton, viewItemsButton, editItemButton;
	private User seller;

	public SellerHomePage(Stage stage, User seller) {
		this.stage = stage;
		this.seller = seller;
		this.stage.setTitle("Seller Home");
		initialize();
		setLayout();
		setAlignment();
		this.scene = new Scene(borderPane, 500, 500);
	}

	private void initialize() {
		borderPane = new BorderPane();
		gridPane = new GridPane();

		uploadItemButton = new Button("Upload Item");
		viewItemsButton = new Button("View My Items");
		editItemButton = new Button("edit Item");

		Button logoutButton = new Button("Logout");

		uploadItemButton.setOnAction(event -> {
			SellerUploadItemPage uploadPage = new SellerUploadItemPage(stage, seller);
			stage.setScene(uploadPage.getScene());
			stage.setTitle("Seller Upload Item Page");
		});

		viewItemsButton.setOnAction(event -> {
			SellerViewMyItemPage sellerViewMyItemPage = new SellerViewMyItemPage(stage, seller);
			stage.setScene(sellerViewMyItemPage.getScene());

		});

		editItemButton.setOnAction(event -> {

		});

		logoutButton.setOnAction(event -> handleLogout());
		gridPane.add(uploadItemButton, 0, 0);
		gridPane.add(viewItemsButton, 0, 1);
		gridPane.add(logoutButton, 0, 2);
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
