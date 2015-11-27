package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

public class ExperimentServiceImpl extends AbstractEntityService<Experiment> implements ExperimentService {

	@Inject
	private AuthService authService;

	@Override
	public boolean create(ExperimentVO view) {
		view.setOwner(mapper.map(authService.getCurrentUser()));
		Experiment experiment = mapper.map(view);
		EntityManager em = null;
		try {
			em = beginTransaction();
			em.persist(experiment);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

	@Override
	protected Class<Experiment> getEntityClass() {
		return Experiment.class;
	}
}
