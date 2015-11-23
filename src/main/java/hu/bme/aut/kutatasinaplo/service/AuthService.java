package hu.bme.aut.kutatasinaplo.service;

public interface AuthService {
	public boolean login(String name, String pwd);

	public boolean logout(String name);
}
