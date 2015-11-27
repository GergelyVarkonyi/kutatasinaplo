package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.UserService;

import javax.enterprise.context.RequestScoped;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;

@RequestScoped
public class AuthServiceImpl implements AuthService {

	@Inject
	private UserService userService;

	@Override
	public boolean login(String name, String pwd) {
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			// collect user principals and credentials in a gui specific manner
			// such as username/password html form, X509 certificate, OpenID,
			// etc.
			// We'll use the username/password example here since it is the most
			// common.
			UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);

			// this is all you have to do to support 'remember me' (no config -
			// built in!):
			token.setRememberMe(true);

			try {
				currentUser.login(token);
			} catch (UnknownAccountException uae) {
				// username wasn't in the system, show them an error message?
				return false;
			} catch (IncorrectCredentialsException ice) {
				// password didn't match, try again?
				return false;
			} catch (LockedAccountException lae) {
				// account for that username is locked - can't login. Show them
				// a message?
				return false;
			} catch (AuthenticationException ae) {
				// unexpected condition - error?
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean logout(String name) {
		// TODO
		return false;
	}

	@Override
	public boolean logoutCurrentUser() {
		try {
			SecurityUtils.getSubject().logout();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		return userService.loadByName((String) subject.getPrincipal());
	}
}
