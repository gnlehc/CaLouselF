package pages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BuyerHomePage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private Button viewItemsButton, viewWishlistButton, viewPurchaseHistoryButton;

	public BuyerHomePage(Stage stage) {
		this.stage = stage;
		this.stage.setTitle("Buyer Home");
		initialize();
		setLayout();
		setAlignment();
		this.scene = new Scene(borderPane, 500, 500);
	}

	private void initialize() {
		borderPane = new BorderPane();
		gridPane = new GridPane();

		viewItemsButton = new Button("View Items");
		viewWishlistButton = new Button("View Wishlist");
		viewPurchaseHistoryButton = new Button("View Purchase History");

		viewItemsButton.setOnAction(event -> {
			ViewItemPage viewItemPage = new ViewItemPage(stage);
			stage.setScene(viewItemPage.getScene());
		});

//		viewWishlistButton.setOnAction(event -> {
//			// Open the page for viewing wishlist
//			new ViewWishlistPage(stage);
//		});
//
//		viewPurchaseHistoryButton.setOnAction(event -> {
//			// Open the page for viewing purchase history
//			new ViewPurchaseHistoryPage(stage);
//		});

		gridPane.add(viewItemsButton, 0, 0);
		gridPane.add(viewWishlistButton, 0, 1);
		gridPane.add(viewPurchaseHistoryButton, 0, 2);
		borderPane.setCenter(gridPane);
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
