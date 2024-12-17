package views.auth;

import controllers.UserController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;

public class RegisterPage {
	private Stage stage;
	private Scene scene;
	private BorderPane borderPane;
	private GridPane gridPane;
	private Label usernameLabel, passwordLabel, phoneLabel, addressLabel, roleLabel;
	private TextField usernameTF, phoneTF, addressTF;
	private PasswordField passwordField;
	private Button registerButton;
	private ComboBox<String> roleComboBox;
	private UserController userController;

	public RegisterPage(Stage stage) {
		this.stage = stage;
		this.userController = new UserController();
		this.stage.setTitle("Register");
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
		phoneLabel = new Label("Phone Number");
		addressLabel = new Label("Address");
		roleLabel = new Label("Role");

		usernameTF = new TextField();
		phoneTF = new TextField();
		addressTF = new TextField();
		passwordField = new PasswordField();
		roleComboBox = new ComboBox<>();
		roleComboBox.getItems().addAll("Seller", "Buyer");

		registerButton = new Button("Register");

		EventHandler<MouseEvent> registerEvent = event -> handleRegister();
		registerButton.addEventHandler(MouseEvent.MOUSE_CLICKED, registerEvent);
	}

	private void setLayout() {
		gridPane.add(usernameLabel, 0, 0);
		gridPane.add(usernameTF, 0, 1);
		gridPane.add(passwordLabel, 0, 2);
		gridPane.add(passwordField, 0, 3);
		gridPane.add(phoneLabel, 0, 4);
		gridPane.add(phoneTF, 0, 5);
		gridPane.add(addressLabel, 0, 6);
		gridPane.add(addressTF, 0, 7);
		gridPane.add(roleLabel, 0, 8);
		gridPane.add(roleComboBox, 0, 9);
		gridPane.add(registerButton, 0, 10);

		Text loginText = new Text("Already have an account? Login here");
		loginText.setStyle("-fx-font-size: 12px; -fx-fill: blue;");
		loginText.setOnMouseClicked(event -> goToLoginPage());
		gridPane.add(loginText, 0, 11);

		borderPane.setCenter(gridPane);
	}

	private void setAlignment() {
		BorderPane.setMargin(gridPane, new Insets(10));
	}

	private void handleRegister() {
		String username = usernameTF.getText().trim();
		String password = passwordField.getText().trim();
		String phone = phoneTF.getText().trim();
		String address = addressTF.getText().trim();
		String role = roleComboBox.getValue();

		if (username.isEmpty() || username.length() < 3) {
			showAlert("Error", "Username must be at least 3 characters long and cannot be empty.");
			return;
		}

		if (!userController.isUsernameUnique(username)) {
			showAlert("Error", "Username already exists. Please choose another.");
			return;
		}

		if (password.isEmpty() || password.length() < 8 || !password.matches(".*[!@#$%^&*].*")) {
			showAlert("Error",
					"Password must be at least 8 characters long and include at least one special character (!, @, #, $, %, ^, &, *).");
			return;
		}

		if (!phone.matches("\\+62\\d{9,10}")) {
			showAlert("Error", "Phone number must start with +62 and contain at least 10 digits (e.g., +62123456789).");
			return;
		}

		if (address.isEmpty()) {
			showAlert("Error", "Address cannot be empty.");
			return;
		}

		if (role == null) {
			showAlert("Error", "Please select a role (Seller or Buyer).");
			return;
		}

		User newUser = new User(username, password, phone, address, role);
		boolean isRegistered = userController.Register(newUser);
		if (isRegistered) {
			showAlert("Success", "Registration successful! Redirecting to login page.");
			goToLoginPage();
		} else {
			showAlert("Error", "User registration failed. Please try again.");
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
		alert.setTitle(title);
		alert.showAndWait();
	}

	private void goToLoginPage() {
		LoginPage loginPage = new LoginPage(stage);
		stage.setScene(loginPage.getScene());
	}

	public Scene getScene() {
		return this.scene;
	}
}
