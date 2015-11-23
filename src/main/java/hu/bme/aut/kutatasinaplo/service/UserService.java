package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.User;

import java.util.List;

public interface UserService {
	public List<User> loadAll();

	public User loadById(int id);

	public User loadByName(String name);

	public boolean createUser(User user, String pwd);

	public boolean saveUser(User user);

	public boolean deleteUser(User user);

	public String loadKey(int userId);
}
