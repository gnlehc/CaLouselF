package controllers;

import javafx.collections.ObservableList;
import models.User;

public class UserController {
	public UserController() {

	}

	public Boolean Register(User user) {
		return User.Register(user);
	}

	public void insertDefaultUsers() {
		try {
			Register(new User("admin", "admin", "08111111111", "Jl. Admin", "Admin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User Login(String username, String password) {
		return User.Login(username, password);
	}

	public Boolean checkUserExists(User user) {
		return User.checkUserExists(user);
	}

	public boolean isUsernameUnique(String username) {
		return User.isUsernameUnique(username);
	}

	public ObservableList<User> getAllUsers() {
		return User.getAllUsers();
	}
}
