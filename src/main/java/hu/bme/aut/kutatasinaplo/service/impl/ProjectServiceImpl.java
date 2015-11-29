package hu.bme.aut.kutatasinaplo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.inject.Inject;

import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ProjectService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

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
	public boolean addEveryParticipantsToExperiments(Integer projectId) {
		EntityManager em = null;
		try {
			Project project = loadById(projectId);
			HashSet<User> projectParticipants = Sets.newHashSet(project.getParticipants());
			List<Experiment> experiments = project.getExperiments();
			for (Experiment experiment : experiments) {
				List<User> participants = experiment.getParticipants();
				HashSet<User> experimentParticipantSet = Sets.newHashSet(participants);
				experimentParticipantSet.add(experiment.getOwner());
				ImmutableSet<User> onlyAddedToProject = Sets.difference(projectParticipants, experimentParticipantSet)
						.immutableCopy();
				Set<User> participantsToBeAddedWithoutAdmin = Sets.filter(onlyAddedToProject, member -> member.getRole() != Role.ADMIN);

				participants.addAll(participantsToBeAddedWithoutAdmin);
			}

			em = beginTransaction();

			em.persist(project);
			commitTransaction(em);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}
	}

	@Override
	public boolean shouldDisplayAdminWarning(ProjectVO projectVO) {
		List<UserVO> participants = projectVO.getParticipants();
		Set<UserVO> projectParticipantsSet = Sets.newHashSet(participants);
		List<ExperimentVO> experiments = projectVO.getExperiments();
		for (ExperimentVO experimentVO : experiments) {
			List<UserVO> participantsOfExperiment = experimentVO.getParticipants();
			if (participantsOfExperiment != null) {
				Set<UserVO> experimentParticipantsSet = Sets.newHashSet(participantsOfExperiment);
				experimentParticipantsSet.addAll(participantsOfExperiment);
				SetView<UserVO> onlyAddedToExperiment = Sets.difference(projectParticipantsSet,
						experimentParticipantsSet);
				if (!onlyAddedToExperiment.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected Class<Project> getEntityClass() {
		return Project.class;
	}

	@Override
	public Project loadById(int id) {
		Project project = super.loadById(id);
		Hibernate.initialize(project.getParticipants());
		return project;
	}

	@Override
	public boolean setParticipants(int projectId, List<Integer> participantsIds) {
		EntityManager em = null;
		try {
			em = beginTransaction();

			Project project = loadById(projectId);
			if (participantsIds != null && !participantsIds.isEmpty()) {
				List<User> users = userService.loadByIds(participantsIds);
				project.setParticipants(users);
			} else {
				project.setParticipants(Lists.<User> newArrayList());
			}

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
