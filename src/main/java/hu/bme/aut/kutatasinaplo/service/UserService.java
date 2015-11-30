package hu.bme.aut.kutatasinaplo.service;

import java.util.List;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

public interface UserService extends AbstractEntityService<User> {

	public User loadByName(String name);

	public boolean createUser(UserVO user) throws ValidateException;

	public boolean saveRoles(List<UserVO> users);

	@Override
	public List<User> loadByIds(List<Integer> ids);

}
