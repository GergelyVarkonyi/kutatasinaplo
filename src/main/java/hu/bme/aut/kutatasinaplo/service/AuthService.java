package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.User;

public interface AuthService {
	public boolean login(String name, String pwd);

	public boolean logout(String name);

	public User getCurrentUser();

	public boolean logoutCurrentUser();

	public void checkCurrentUserIsAdmin();
}
