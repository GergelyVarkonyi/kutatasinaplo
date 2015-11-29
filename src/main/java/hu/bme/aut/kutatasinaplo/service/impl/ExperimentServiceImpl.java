package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.BlobFile;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.BlobFileVO;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;
import hu.bme.aut.kutatasinplo.security.SecurityUtils;
import hu.bme.aut.kutatasinplo.security.SecurityUtils.Action;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class ExperimentServiceImpl extends AbstractEntityServiceImpl<Experiment> implements ExperimentService {

	@Inject
	private AuthService authService;
	@Inject
	private UserService userService;

	@Override
	public boolean create(ExperimentVO view) throws ValidateException {
		User currentUser = authService.getCurrentUser();
		if (!SecurityUtils.hasPermission(Action.CREATE_EXPERIMENT, currentUser, null)) {
			return false;
		}

		view.setOwner(mapper.map(currentUser));
		Experiment experiment = mapper.map(view);

		validate(experiment);
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
			em = beginTransaction();

			User currentUser = authService.getCurrentUser();
			Experiment experiment = loadById(experimentId);
			if (!SecurityUtils.hasPermission(Action.ADD_PARTICIPANT, currentUser, experiment)) {
				return false;
			}

			if (participantsIds != null && !participantsIds.isEmpty()) {
				List<User> users = userService.loadByIds(participantsIds);
				experiment.setParticipants(users);
			} else {
				experiment.setParticipants(Lists.<User> newArrayList());
			}

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
			em = beginTransaction();

			User currentUser = authService.getCurrentUser();
			Experiment experiment = loadById(experimentId);
			if (!SecurityUtils.hasPermission(Action.UPLOAD_FILE, currentUser, experiment)) {
				return false;
			}
			List<BlobFile> transformedFiles = Lists.transform(files, new Function<BlobFileVO, BlobFile>() {

				@Override
				public BlobFile apply(BlobFileVO input) {
					return mapper.map(input);
				}

			});

			List<BlobFile> attachments = experiment.getAttachments();
			if (attachments != null) {
				attachments.addAll(transformedFiles);
			} else {
				experiment.setAttachments(transformedFiles);
			}

			em.merge(experiment);
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
	public boolean attachImages(int experimentId, List<BlobFileVO> files) {
		EntityManager em = null;
		try {
			em = beginTransaction();
			User currentUser = authService.getCurrentUser();
			Experiment experiment = loadById(experimentId);
			if (!SecurityUtils.hasPermission(Action.UPLOAD_FILE, currentUser, experiment)) {
				return false;
			}

			List<BlobFile> transformedImages = Lists.transform(files, new Function<BlobFileVO, BlobFile>() {

				@Override
				public BlobFile apply(BlobFileVO input) {
					return mapper.map(input);
				}

			});

			List<BlobFile> images = experiment.getImages();
			if (images != null) {
				images.addAll(transformedImages);
			} else {
				experiment.setImages(transformedImages);
			}

			em.merge(experiment);
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
	public boolean save(ExperimentVO view) throws ValidateException {
		Experiment experiment = mapper.map(view);
		User owner = userService.loadById(view.getOwner().getId());
		experiment.setOwner(owner);

		User currentUser = authService.getCurrentUser();
		if (!SecurityUtils.hasPermission(Action.UPLOAD_FILE, currentUser, experiment)) {
			return false;
		}

		validate(experiment);
		EntityManager em = null;
		try {
			em = beginTransaction();
			em.merge(experiment);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

	@Override
	public boolean delete(int id) {
		Experiment experiment = loadById(id);
		User currentUser = authService.getCurrentUser();
		if (!SecurityUtils.hasPermission(Action.DELETE_EXPERIMENT, currentUser, experiment)) {
			return false;
		}

		return super.delete(experiment);
	};

	@Override
	protected void validate(Experiment experiment) throws ValidateException {
		if (Strings.isNullOrEmpty(experiment.getName())) {
			throw new ValidateException("Name can not be empty.");
		}
		if (Strings.isNullOrEmpty(experiment.getDescription())) {
			throw new ValidateException("Description can not be empty.");
		}
		if (experiment.getOwner() == null) {
			throw new ValidateException("Owner can not be empty.");
		}
		if (experiment.getType() == null) {
			throw new ValidateException("Type can not be empty.");
		}
	}
}
