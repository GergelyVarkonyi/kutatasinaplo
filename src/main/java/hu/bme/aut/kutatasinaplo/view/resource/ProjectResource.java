package hu.bme.aut.kutatasinaplo.view.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.service.ProjectService;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;
import lombok.extern.java.Log;

@Path("/project")
@Log
public class ProjectResource {

	private ProjectService projectService;
	private DataViewMapper mapper;

	@Inject
	public ProjectResource(ProjectService projectService, DataViewMapper mapper) {
		this.projectService = projectService;
		this.mapper = mapper;
	}

	@POST
	@Path("/create")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response create(ProjectVO projectVO) {
		boolean success = projectService.create(projectVO);

		return success ? Response.ok().build() : Response.status(Status.BAD_REQUEST).build();
	}

	@GET
	@Path("/list")
	public List<ProjectVO> loadProjects() {
		return Lists.transform(projectService.loadAll(), mapper::map);
	}

	@POST
	@Path("/load")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ProjectVO load(String id) {
		log.info("Load project: " + id);
		try {
			Project project = projectService.loadById(Integer.valueOf(id));
			if (project != null) {
				return mapper.map(project);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
