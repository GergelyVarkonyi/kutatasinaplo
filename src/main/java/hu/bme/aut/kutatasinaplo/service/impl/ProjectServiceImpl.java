package hu.bme.aut.kutatasinaplo.service.impl;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ProjectService;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;

public class ProjectServiceImpl extends AbstractEntityServiceImpl<Project> implements ProjectService {

	@Inject
	private AuthService authService;

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

}
