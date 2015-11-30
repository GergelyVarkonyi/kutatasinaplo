package hu.bme.aut.kutatasinaplo.service.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.crypto.hash.Sha512Hash;
import org.hibernate.Hibernate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import hu.bme.aut.kutatasinaplo.model.KeyValuePair;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

public class UserServiceImpl extends AbstractEntityServiceImpl<User> implements UserService {

	@Override
	public User loadByName(String name) {
		EntityManager em = emProvider.get();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> root = criteriaQuery.from(User.class);
		Predicate condition = builder.equal(root.get("name"), Strings.nullToEmpty(name));
		criteriaQuery.where(condition);

		TypedQuery<User> query = em.createQuery(criteriaQuery);
		try {
			User user = query.getSingleResult();
			Hibernate.initialize(user.getKnowledge());
			return user;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public boolean createUser(UserVO view) throws ValidateException {
		view.setRole(Role.USER);
		User user = mapper.map(view);

		String salt = UUID.randomUUID().toString();
		Sha512Hash sha512Hash = new Sha512Hash(user.getPassword(), salt, 10);
		user.setSalt(salt);
		user.setPassword(sha512Hash.toBase64());

		validate(user);

		EntityManager em = null;
		try {
			em = beginTransaction();
			em.persist(user);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

	@Override
	protected void validate(User user) throws ValidateException {
		String name = user.getName();
		if (Strings.isNullOrEmpty(name)) {
			throw new ValidateException("Name can not be empty.");
		}
		if (loadByName(name) != null) {
			throw new ValidateException("There is already a user with the same name.");
		}
		String email = user.getEmail();
		if (Strings.isNullOrEmpty(email)) {
			throw new ValidateException("Email can not be empty.");
		}
		String password = user.getPassword();
		if (Strings.isNullOrEmpty(password)) {
			throw new ValidateException("Password can not be empty.");
		}

		List<KeyValuePair> knowledge = user.getKnowledge();
		if (knowledge != null) {
			for (KeyValuePair pair : knowledge) {
				String key = pair.getKeyOfInstance();
				if (Strings.isNullOrEmpty(key)) {
					throw new ValidateException("Topic can not be empty.");
				}
			}
		}
	}

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	public boolean saveRoles(List<UserVO> users) {

		EntityManager em = null;
		try {
			em = beginTransaction();
			List<User> managedUsers = loadByIds(Lists.transform(users, user -> user.getId()));
			for (int i = 0; i < managedUsers.size(); i++) {
				User managedUser = managedUsers.get(i);
				managedUser.setRole(users.get(i).getRole());
				em.persist(managedUser);
			}
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

}
