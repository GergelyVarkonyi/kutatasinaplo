package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.AbstractEntity;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.service.AbstractEntityService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class AbstractEntityServiceImpl<T extends AbstractEntity> implements AbstractEntityService<T> {

	@Inject
	protected Provider<EntityManager> emProvider;
	@Inject
	protected DataViewMapper mapper;

	private Class<T> clazz = getEntityClass();

	@Override
	public List<T> loadAll() {
		EntityManager em = emProvider.get();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
		Root<T> root = criteriaQuery.from(clazz);
		criteriaQuery.select(root);

		TypedQuery<T> query = em.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	public T loadById(int id) {
		EntityManager em = emProvider.get();
		return em.find(clazz, id);
	}

	@Override
	public List<T> loadByIds(List<Integer> ids) {
		EntityManager em = emProvider.get();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
		Root<T> root = criteriaQuery.from(clazz);
		criteriaQuery.select(root).where(root.get("id").in(ids));

		TypedQuery<T> query = em.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	public boolean delete(int id) {
		EntityManager em = null;
		try {
			em = beginTransaction();
			em.remove(loadById(id));
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

	protected boolean delete(T entity) {
		EntityManager em = null;
		try {
			em = beginTransaction();
			em.remove(entity);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

	@Override
	public boolean save(int id) throws ValidateException {
		EntityManager em = null;
		try {
			em = beginTransaction();

			T entity = loadById(id);
			validate(entity);

			em.merge(entity);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

	@Override
	public boolean save(T entity) throws ValidateException {
		validate(entity);

		EntityManager em = null;
		try {
			em = beginTransaction();
			em.merge(entity);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
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

	protected void validate(T entity) throws ValidateException {

	}
}
