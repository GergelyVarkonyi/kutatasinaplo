package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.AbstractEntity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class AbstractEntityService<T extends AbstractEntity> {

	@Inject
	protected Provider<EntityManager> emProvider;

	private Class<T> clazz = getEntityClass();

	public List<T> loadAll() {
		EntityManager em = emProvider.get();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
		Root<T> root = criteriaQuery.from(clazz);
		criteriaQuery.select(root);

		TypedQuery<T> query = em.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public T loadById(int id) {
		EntityManager em = emProvider.get();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
		Root<T> root = criteriaQuery.from(clazz);
		Predicate condition = builder.equal(root.get("id"), id);
		criteriaQuery.where(condition);

		TypedQuery<T> query = em.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	protected abstract Class<T> getEntityClass();

	protected EntityManager beginTransaction() {
		EntityManager em = emProvider.get();
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		return em;
	}

	protected void commitTransaction(EntityManager em) {
		em.flush();
		em.getTransaction().commit();
	}
}
