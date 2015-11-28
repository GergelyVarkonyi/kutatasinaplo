package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;

public interface ProjectService extends AbstractEntityService<Project> {

	boolean create(ProjectVO project);

}
