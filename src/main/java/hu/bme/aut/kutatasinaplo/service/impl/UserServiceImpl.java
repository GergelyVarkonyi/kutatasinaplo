package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class UserServiceImpl extends AbstractEntityService<User> implements UserService {

	@Inject
	private Provider<EntityManager> emProvider;

	@Override
	public User loadByName(String name) {
		EntityManager em = emProvider.get();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
		Root<User> root = criteriaQuery.from(User.class);
		Predicate condition = builder.equal(root.get("name"), "admin");
		criteriaQuery.where(condition);

		TypedQuery<User> query = em.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	@Override
	public boolean createUser(User user, String pwd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

}
