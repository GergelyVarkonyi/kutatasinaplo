package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.BlobFile;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.BlobFileVO;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class ExperimentServiceImpl extends AbstractEntityServiceImpl<Experiment> implements ExperimentService {

	@Inject
	private AuthService authService;
	@Inject
	private UserService userService;

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

	@Override
	public boolean setParticipants(int experimentId, List<Integer> participantsIds) {
		EntityManager em = null;
		try {
			Experiment experiment = loadById(experimentId);
			if (participantsIds != null && !participantsIds.isEmpty()) {
				List<User> users = userService.loadByIds(participantsIds);
				experiment.setParticipants(users);
			} else {
				experiment.setParticipants(Lists.<User> newArrayList());
			}

			em = beginTransaction();
			em.persist(experiment);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (em != null) {
				em.getTransaction().rollback();
			}
			return false;
		}
	}

	@Override
	public Experiment loadById(int id) {
		Experiment experiment = super.loadById(id);
		Hibernate.initialize(experiment.getParticipants());
		Hibernate.initialize(experiment.getAttachments());
		Hibernate.initialize(experiment.getImages());
		Hibernate.initialize(experiment.getParticipants());
		return experiment;
	}

	@Override
	public boolean attachFiles(int experimentId, List<BlobFileVO> files) {
		EntityManager em = null;
		try {
			Experiment experiment = loadById(experimentId);
			experiment.getAttachments().addAll(Lists.transform(files, new Function<BlobFileVO, BlobFile>() {

				@Override
				public BlobFile apply(BlobFileVO input) {
					return mapper.map(input);
				}

			}));

			em = beginTransaction();
			em.persist(experiment);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (em != null) {
				em.getTransaction().rollback();
			}
			return false;
		}
	}

	@Override
	public boolean attachImages(int experimentId, List<BlobFileVO> images) {
		EntityManager em = null;
		try {
			Experiment experiment = loadById(experimentId);
			experiment.getImages().addAll(Lists.transform(images, new Function<BlobFileVO, BlobFile>() {

				@Override
				public BlobFile apply(BlobFileVO input) {
					return mapper.map(input);
				}

			}));

			em = beginTransaction();
			em.persist(experiment);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (em != null) {
				em.getTransaction().rollback();
			}
			return false;
		}
	}

	@Override
	public boolean save(ExperimentVO view) {
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
}
