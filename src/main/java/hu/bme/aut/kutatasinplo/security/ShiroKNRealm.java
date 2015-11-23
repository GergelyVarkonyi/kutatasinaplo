package hu.bme.aut.kutatasinplo.security;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.UserService;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.inject.Inject;

public class ShiroKNRealm extends AuthorizingRealm {

	private static Logger logger = Logger.getLogger(ShiroKNRealm.class.getName());

	private KNSecurityManagerService securityManagerService;

	private UserService userService;

	@Inject
	public ShiroKNRealm(UserService userService) {
		// This is the thing that knows how to find user creds and roles
		this.userService = userService;
		this.securityManagerService = new KNSecurityManagerService(userService);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

		String username = (String) principalCollection.getPrimaryPrincipal();

		// Find the thing that stores your user's roles.
		User principal = securityManagerService.findMyPrincipalByUsername(username);
		if (principal == null) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Principal not found for authorizing user with username: " + username);
			}
			return null;
		} else {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine(String.format("Authoriziong user %s with roles: %s", username, principal.getRoles()));
			}
			SimpleAuthorizationInfo result = new SimpleAuthorizationInfo(principal.getRoles());
			return result;
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {

		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
		if (usernamePasswordToken.getUsername() == null || usernamePasswordToken.getUsername().isEmpty()) {
			throw new AuthenticationException("Authentication failed");
		}

		// Find the thing that stores your user's credentials. This may be the
		// same or different than
		// the thing that stores the roles.
		User principal = securityManagerService.findMyPrincipalByUsername(usernamePasswordToken.getUsername());
		if (principal == null) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Principal not found for user with username: " + usernamePasswordToken.getUsername());
			}
			return null;
		}

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Principal found for authenticating user with username: " + usernamePasswordToken.getUsername());
		}

		return new SimpleAccount(principal.getName(), principal.getPassword(), getName(),
				principal.getRoles(), new HashSet());
	}
}
