package views;

import controllers.UserController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.User;

public class LoginPage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private Label usernameLabel, passwordLabel, registerLabel;
	private TextField usernameTF;
	private PasswordField passwordfield;
	private Button loginButton;
	private UserController userController;

	public LoginPage(Stage stage) {
		this.stage = stage;
		this.userController = new UserController();
		this.stage.setTitle("Login");
		initialize();
		setLayout();
		setAlignment();
		this.scene = new Scene(borderPane, 500, 500);
	}

	private void initialize() {
		borderPane = new BorderPane();
		gridPane = new GridPane();
		usernameLabel = new Label("Username");
		passwordLabel = new Label("Password");
		usernameTF = new TextField();
		passwordfield = new PasswordField();
		loginButton = new Button("Login");

		// New register link label
		registerLabel = new Label("Don't have an account yet? Register here");

		EventHandler<MouseEvent> loginEvent = event -> handleLogin();
		loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, loginEvent);

		// Event for register label
		EventHandler<MouseEvent> registerEvent = event -> openRegisterPage();
		registerLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, registerEvent);
		registerLabel.setStyle("-fx-text-fill: blue; -fx-cursor: hand;");
	}

	private void setLayout() {
		gridPane.add(usernameLabel, 0, 0);
		gridPane.add(usernameTF, 0, 1);
		gridPane.add(passwordLabel, 0, 2);
		gridPane.add(passwordfield, 0, 3);
		gridPane.add(loginButton, 0, 4);
		gridPane.add(registerLabel, 0, 5);

		borderPane.setCenter(gridPane);
	}

	private void setAlignment() {
		BorderPane.setMargin(gridPane, new Insets(10));
	}

	private void handleLogin() {
		String username = usernameTF.getText();
		String password = passwordfield.getText();

		if (username.isEmpty() || password.isEmpty()) {
			showAlert("Error", "Please fill in both username and password.");
			return;
		}

		User user = userController.Login(username, password);
		if (user != null) {
			if (user.getRole().equals("Buyer")) {
				BuyerHomePage buyerHomePage = new BuyerHomePage(stage, user);
				stage.setScene(buyerHomePage.getScene());
			} else if (user.getRole().equals("Seller")) {
				SellerHomePage sellerHomePage = new SellerHomePage(stage, user);
				stage.setScene(sellerHomePage.getScene());
			} else if (user.getRole().equals("Admin")) {
				AdminHomePage adminHomePage = new AdminHomePage(stage);
				stage.setScene(adminHomePage.getScene());
			}
		} else {
			showAlert("Error", "Invalid username or password.");
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.setTitle(title);
		alert.showAndWait();
	}

	private void openRegisterPage() {
		RegisterPage registerPage = new RegisterPage(stage);
		stage.setScene(registerPage.getScene());
	}

	public Scene getScene() {
		return this.scene;
	}
}
