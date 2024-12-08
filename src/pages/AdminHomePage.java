package pages;

import controllers.ItemController;
import controllers.UserController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Item;
import models.User;

public class AdminHomePage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private ListView<Item> pendingItemsListView;
	private ListView<User> usersListView;
	private ItemController itemController;
	private UserController userController;

	public AdminHomePage(Stage stage) {
		this.stage = stage;
		initialize();
		setLayout();
		setAlignment();
		this.scene = new Scene(borderPane, 600, 600);
	}

	private void initialize() {
		itemController = new ItemController();
		userController = new UserController();
		borderPane = new BorderPane();
		gridPane = new GridPane();

		pendingItemsListView = new ListView<>();
		usersListView = new ListView<>();

		pendingItemsListView.setItems(itemController.getPendingItems());
		usersListView.setItems(userController.getAllUsers());
		pendingItemsListView.setCellFactory(param -> new javafx.scene.control.ListCell<Item>() {
			@Override
			protected void updateItem(Item item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.toString());
				}
			}
		});
		usersListView.setCellFactory(param -> new javafx.scene.control.ListCell<User>() {
			@Override
			protected void updateItem(User user, boolean empty) {
				super.updateItem(user, empty);
				if (empty || user == null) {
					setText(null);
				} else {
					setText(user.toString());
				}
			}
		});

		Button approveButton = new Button("Approve Item");
		Button declineButton = new Button("Decline Item");
		Button viewOffersButton = new Button("View Offers");
		Button viewUsersButton = new Button("View Users");
		Button logoutButton = new Button("Logout");

//		approveButton.setOnAction(event -> handleApproveItem());
//		declineButton.setOnAction(event -> handleDeclineItem());
//		viewOffersButton.setOnAction(event -> handleViewOffers());
//		viewUsersButton.setOnAction(event -> handleViewUsers());
		logoutButton.setOnAction(event -> handleLogout());

		gridPane.add(new Label("Pending Items"), 0, 0);
		gridPane.add(pendingItemsListView, 0, 1);
		gridPane.add(approveButton, 0, 2);
		gridPane.add(declineButton, 1, 2);
		gridPane.add(viewOffersButton, 0, 3);
		gridPane.add(viewUsersButton, 0, 4);
		gridPane.add(new Label("Registered Users"), 0, 5);
		gridPane.add(usersListView, 0, 6);
		gridPane.add(logoutButton, 0, 7);

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

//	private void handleApproveItem() {
//		Item selectedItem = pendingItemsListView.getSelectionModel().getSelectedItem();
//		if (selectedItem != null) {
//			itemController.approveItem(selectedItem.getItemId());
//			showAlert("Item Approved", "The item has been approved.");
//		}
//	}
//
//	private void handleDeclineItem() {
//		Item selectedItem = pendingItemsListView.getSelectionModel().getSelectedItem();
//		if (selectedItem != null) {
//			// Show prompt for a reason
//			TextInputDialog dialog = new TextInputDialog();
//			dialog.setTitle("Reason for Decline");
//			dialog.setHeaderText("Enter reason for declining the item:");
//			dialog.setContentText("Reason:");
//
//			dialog.showAndWait().ifPresent(reason -> {
//				itemController.declineItem(selectedItem.getItemId(), reason);
//				showAlert("Item Declined", "The item has been declined.");
//			});
//		}
//	}

//	private void handleViewOffers() {
//		// Open the offers page for the admin to view offers
//		new AdminViewOffersPage(stage);
//	}
//
//	private void handleViewUsers() {
//		// Open the users page for the admin to manage users
//		new AdminViewUsersPage(stage);
//	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public Scene getScene() {
		return this.scene;
	}
}
