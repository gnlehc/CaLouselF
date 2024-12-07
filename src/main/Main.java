package main;

import controllers.DatabaseConnection;
import controllers.ItemController;
import controllers.UserController;
import javafx.application.Application;
import javafx.stage.Stage;
import pages.LoginPage;

public class Main extends Application {
	private DatabaseConnection db = new DatabaseConnection();
	private UserController userController = new UserController();
	private ItemController itemController = new ItemController();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public Main() {
		db.migrateTables();
		userController.insertDefaultUsers();
		itemController.insertDefaultItems();
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		LoginPage loginPage = new LoginPage(arg0);
		arg0.setTitle("CaLouselF - Sustainable Fashion Marketplace");
		arg0.setScene(loginPage.getScene());
		arg0.show();
	}

}
