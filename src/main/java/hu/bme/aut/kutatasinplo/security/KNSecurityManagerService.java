package hu.bme.aut.kutatasinplo.security;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.UserService;

public class KNSecurityManagerService {

	UserService userService;

	public KNSecurityManagerService(UserService userService) {
		this.userService = userService;
	}

	public User findMyPrincipalByUsername(String username) {
		return userService.loadByName(username);
	}

}
