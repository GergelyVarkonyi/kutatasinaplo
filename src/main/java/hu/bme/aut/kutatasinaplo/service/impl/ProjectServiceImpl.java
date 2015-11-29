package hu.bme.aut.kutatasinaplo.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ProjectService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;

public class ProjectServiceImpl extends AbstractEntityServiceImpl<Project> implements ProjectService {

	@Inject
	private AuthService authService;

	@Inject
	private UserService userService;

	@Override
	public boolean create(ProjectVO projectVO) {

		EntityManager em = null;
		try {
			Project project = mapper.map(projectVO);
			em = beginTransaction();
			em.persist(project);
			commitTransaction(em);
			projectVO.setId(project.getId());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}

	}

	@Override
	protected Class<Project> getEntityClass() {
		return Project.class;
	}

	@Override
	public boolean setParticipants(int experimentId, List<Integer> participantsIds) {
		EntityManager em = null;
		try {
			Project project = loadById(experimentId);
			if (participantsIds != null && !participantsIds.isEmpty()) {
				List<User> users = userService.loadByIds(participantsIds);
				project.setParticipants(users);
			} else {
				project.setParticipants(Lists.<User> newArrayList());
			}

			em = beginTransaction();
			em.persist(project);
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
	public void save(ProjectVO projectVO) {
		EntityManager em = null;
		try {
			Project project = mapper.map(projectVO);

			em = beginTransaction();
			em.merge(project);
			commitTransaction(em);
		} catch (Exception e) {
			e.printStackTrace();
			if (em != null) {
				em.getTransaction().rollback();
			}
		}
	}

}
