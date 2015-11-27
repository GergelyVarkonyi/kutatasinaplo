package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

import java.util.List;

public interface UserService {
	public List<User> loadAll();

	public User loadById(int id);

	public User loadByName(String name);

	public boolean createUser(UserVO user);

	public boolean deleteUser(User user);

}
