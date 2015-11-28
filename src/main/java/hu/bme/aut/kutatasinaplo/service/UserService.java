package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

import java.util.List;

public interface UserService extends AbstractEntityService<User> {

	public User loadByName(String name);

	public boolean createUser(UserVO user);

	@Override
	public List<User> loadByIds(List<Integer> ids);

}
