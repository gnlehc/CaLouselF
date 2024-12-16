package views;

import controllers.ItemController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Item;

public class AdminHomePage {
    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private GridPane gridPane;
    private ListView<Item> pendingItemsListView;
    private ItemController itemController;

    public AdminHomePage(Stage stage) {
        this.stage = stage;
        initialize();
        setupUI();
        setLayout();
        setAlignment();
        this.scene = new Scene(borderPane, 800, 600);
    }

    private void initialize() {
        itemController = new ItemController();
        borderPane = new BorderPane();
        gridPane = new GridPane();
        pendingItemsListView = new ListView<>();

        pendingItemsListView.setItems(itemController.getPendingItems());
        pendingItemsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Name: " + item.getName() + " | Category: " + item.getCategory()
                            + " | Size: " + item.getSize() + " | Price: $" + item.getPrice());
                }
            }
        });
    }

    private void setupUI() {
        Button approveButton = new Button("Approve");
        Button declineButton = new Button("Decline");
        Button logoutButton = new Button("Logout");

        approveButton.setOnAction(event -> handleApproveItem());
        declineButton.setOnAction(event -> handleDeclineItem());
        logoutButton.setOnAction(event -> handleLogout());

        HBox actionButtons = new HBox(10, approveButton, declineButton);
        actionButtons.setPadding(new Insets(10));

        Label pendingItemsLabel = new Label("Pending Items for Review:");
        VBox pendingItemsBox = new VBox(10, pendingItemsLabel, pendingItemsListView, actionButtons);

        VBox leftPane = new VBox(20, pendingItemsBox, logoutButton);
        leftPane.setPadding(new Insets(10));

        borderPane.setLeft(leftPane);

        VBox.setMargin(pendingItemsBox, new Insets(10));
        HBox.setMargin(approveButton, new Insets(5));
        HBox.setMargin(declineButton, new Insets(5));
    }

    private void setLayout() {
        gridPane.setHgap(10);
        gridPane.setVgap(10);
    }

    private void setAlignment() {
        BorderPane.setMargin(gridPane, new Insets(10));
    }

    private void handleApproveItem() {
        Item selectedItem = pendingItemsListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to approve.");
            return;
        }

        boolean success = false;
		try {
			success = itemController.approveItem(selectedItem.getItemId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (success) {
            showAlert("Success", "Item approved successfully.");
            pendingItemsListView.getItems().remove(selectedItem);
        } else {
            showAlert("Error", "Failed to approve item.");
        }
    }

    private void handleDeclineItem() {
        Item selectedItem = pendingItemsListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to decline.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Decline Item");
        dialog.setHeaderText("Reason for declining the item:");
        dialog.setContentText("Reason:");

        dialog.showAndWait().ifPresent(reason -> {
            if (reason.trim().isEmpty()) {
                showAlert("Error", "Reason cannot be empty.");
            } else {
                boolean success = itemController.declineItem(selectedItem.getItemId(), reason);
                if (success) {
                    showAlert("Success", "Item declined successfully.");
                    pendingItemsListView.getItems().remove(selectedItem);
                } else {
                    showAlert("Error", "Failed to decline item.");
                }
            }
        });
    }

    private void handleLogout() {
        LoginPage loginPage = new LoginPage(stage);
        stage.setScene(loginPage.getScene());
        stage.setTitle("Login");
    }

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
