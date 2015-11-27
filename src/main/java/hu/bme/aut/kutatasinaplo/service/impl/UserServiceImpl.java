package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.base.Strings;

public class UserServiceImpl extends AbstractEntityService<User> implements UserService {

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
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public boolean createUser(UserVO view) {
		view.setRole(Role.USER);
		User user = mapper.map(view);
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
	public boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

}
