package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.UserService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class UserServiceImpl implements UserService {

	@Inject
	private Provider<EntityManager> emProvider;

	@Override
	public List<User> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User loadById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User loadByName(String name) {
		EntityManager em = emProvider.get();
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<User> c = qb.createQuery(User.class);
		Root<User> p = c.from(User.class);
		Predicate condition = qb.equal(p.get("name"), "admin");
		c.where(condition);
		TypedQuery<User> q = em.createQuery(c);
		return q.getSingleResult();
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
	public String loadKey(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
