package hu.bme.aut.kutatasinaplo.service.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
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

	@Inject
	private ExperimentService experimentService;

	@Inject
	private DataViewMapper mapper;

	@Override
	public boolean create(ProjectVO projectVO) {

		authService.checkCurrentUserIsAdmin();

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
		authService.checkCurrentUserIsAdmin();
		EntityManager em = null;
		try {
			Project project = loadById(projectId);
			HashSet<User> projectParticipants = Sets.newHashSet(project.getParticipants());
			List<Experiment> experiments = project.getExperiments();
			for (Experiment experiment : experiments) {
				List<User> participants = experiment.getParticipants();
				Set<User> participantsToBeAddedWithoutAdmin = participantsWhoArePartOfProjectButNotExperiment(
						projectParticipants, experiment);

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

	private Set<User> participantsWhoArePartOfProjectButNotExperiment(Set<User> projectParticipants,
			Experiment experiment) {
		List<User> participants = experiment.getParticipants();
		HashSet<User> experimentParticipantSet = Sets.newHashSet(participants);
		experimentParticipantSet.add(experiment.getOwner());
		ImmutableSet<User> onlyAddedToProject = Sets.difference(projectParticipants, experimentParticipantSet)
				.immutableCopy();
		Set<User> participantsToBeAddedWithoutAdmin = Sets.filter(onlyAddedToProject,
				member -> member.getRole() != Role.ADMIN);
		return participantsToBeAddedWithoutAdmin;
	}

	private boolean shouldDisplayAdminWarning(Project project) {
		Set<User> projectParticipantsSet = Sets.newHashSet();
		List<User> participants = project.getParticipants();
		if (participants != null) {
			projectParticipantsSet.addAll(participants);
		}
		List<Experiment> experiments = project.getExperiments();
		for (Experiment experimentVO : experiments) {
			List<User> participantsOfExperiment = experimentVO.getParticipants();
			if (participantsOfExperiment != null) {
				Set<User> onlyAddedToExperiment = participantsWhoArePartOfProjectButNotExperiment(
						projectParticipantsSet, experimentVO);
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
		authService.checkCurrentUserIsAdmin();
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
		authService.checkCurrentUserIsAdmin();
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

	@Override
	public List<ProjectVO> listAccessibleProjects() {
		List<Project> projects = loadAll();

		User currentUser = authService.getCurrentUser();

		List<Project> accessibleProjectsForUser = Lists
				.newArrayList(Iterables.filter(projects, proj -> canViewProject(proj, currentUser)));

		List<ProjectVO> result = Lists.transform(accessibleProjectsForUser, mapper::map);
		for (ProjectVO projectVO : result) {
			List<ExperimentVO> experiments = projectVO.getExperiments();
			if (experiments != null) {
				Iterator<ExperimentVO> iterator = experiments.iterator();
				while (iterator.hasNext()) {
					ExperimentVO experimentVO = iterator.next();
					if (!experimentService.hasReaderRight(experimentVO, mapper.map(currentUser))) {
						iterator.remove();
					}

				}
			}
		}

		return result;

	}

	private boolean canViewProject(Project project, User user) {
		if (user.getRole() == Role.ADMIN) {
			return true;
		}
		List<User> participants = project.getParticipants();
		if (participants != null && participants.contains(user)) {
			return true;
		}
		return false;
	}

	public ProjectVO loadProjectWithPermissionChecks(String id) {
		User currentUser = authService.getCurrentUser();
		UserVO currentUserVO = mapper.map(currentUser);
		Project project = loadById(Integer.valueOf(id));
		ProjectVO projectVO = mapper.map(project);
		if (currentUser.getRole() != Role.ADMIN) {
			List<ExperimentVO> experiments = projectVO.getExperiments();
			List<ExperimentVO> accessibleExperiments = removeNonAccessibleExperimentsFromList(currentUserVO,
					experiments);
			if (experiments.size() != accessibleExperiments.size()) {
				projectVO.setExperimentWithoutRight(true);
			}
		} else {
			boolean shouldDisplayAdminWarning = shouldDisplayAdminWarning(project);
			projectVO.setAdminAccessRightWarning(shouldDisplayAdminWarning);
		}
		return projectVO;
	}

	private List<ExperimentVO> removeNonAccessibleExperimentsFromList(UserVO currentUserVO,
			List<ExperimentVO> experiments) {
		List<ExperimentVO> accessibleExperiments = Lists
				.newArrayList(Collections2.filter(experiments,
						exp -> experimentService.hasReaderRight(exp, currentUserVO)));
		experiments.clear();
		experiments.addAll(accessibleExperiments);
		return accessibleExperiments;
	}

}
