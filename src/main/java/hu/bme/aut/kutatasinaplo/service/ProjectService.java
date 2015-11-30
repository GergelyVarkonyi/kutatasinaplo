package hu.bme.aut.kutatasinaplo.service;

import java.util.List;

import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;

public interface ProjectService extends AbstractEntityService<Project> {

	boolean create(ProjectVO project);

	boolean setParticipants(int experimentId, List<Integer> participantsIds);

	void save(ProjectVO projectVO);

	boolean addEveryParticipantsToExperiments(Integer projectId);

	List<ProjectVO> listAccessibleProjects();

	ProjectVO loadProjectWithPermissionChecks(String id);
}
