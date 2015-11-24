package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

public class ExperimentServiceImpl extends AbstractEntityService<Experiment> implements ExperimentService {

	@Inject
	private DataViewMapper mapper;
	@Inject
	private AuthService authService;

	@Override
	public Experiment loadById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean create(ExperimentVO view) {
		view.setOwner(mapper.map(authService.getCurrentUser()));
		Experiment experiment = mapper.map(view);
		try {
			EntityManager em = emProvider.get();
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			em.persist(experiment);
			em.flush();
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected Class<Experiment> getEntityClass() {
		return Experiment.class;
	}
}
