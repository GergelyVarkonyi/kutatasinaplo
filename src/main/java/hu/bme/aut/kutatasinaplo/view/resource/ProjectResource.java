package hu.bme.aut.kutatasinaplo.view.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ProjectService;
import hu.bme.aut.kutatasinaplo.view.model.AddListToEntityVO;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;
import lombok.extern.java.Log;

@Path("/project")
@Log
public class ProjectResource {

	private ProjectService projectService;
	private DataViewMapper mapper;
	private AuthService authService;

	@Inject
	public ProjectResource(ProjectService projectService, DataViewMapper mapper, AuthService authService) {
		this.projectService = projectService;
		this.mapper = mapper;
		this.authService = authService;
	}

	@POST
	@Path("/create")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ProjectVO create(ProjectVO projectVO) {
		boolean success = projectService.create(projectVO);

		return success ? projectVO : null;
	}

	@GET
	@Path("/list")
	public List<ProjectVO> loadProjects() {
		return Lists.transform(projectService.loadAll(), mapper::map);
	}

	@GET
	@Path("/{id}")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ProjectVO load(@PathParam(value = "id") String id) {
		log.info("Load project: " + id);
		try {
			User currentUser = authService.getCurrentUser();
			UserVO currentUserVO = mapper.map(currentUser);
			Project project = projectService.loadById(Integer.valueOf(id));
			ProjectVO projectVO = mapper.map(project);
			if (currentUser.getRole() != Role.ADMIN) {
				List<ExperimentVO> experiments = projectVO.getExperiments();
				List<ExperimentVO> accessibleExperiments = Lists
						.newArrayList(Collections2.filter(experiments, exp -> hasReaderRight(exp, currentUserVO)));
				projectVO.setExperiments(accessibleExperiments);
				if (experiments.size() != accessibleExperiments.size()) {
					projectVO.setExperimentWithoutRight(true);
				}
			}
			return projectVO;
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

	private boolean hasReaderRight(ExperimentVO experiment, UserVO currentUser) {
		if (experiment.getOwner().equals(currentUser)) {
			return true;
		}
		if (experiment.getParticipants().contains(currentUser)) {
			return true;
		}
		return false;
	}

	@DELETE
	@Path("/delete/{id}")
	@Produces(value = MediaType.APPLICATION_JSON)

	public Response delete(@PathParam(value = "id") String id) {
		boolean success = projectService.delete(Integer.valueOf(id));
		if (success) {
			return Response.ok().build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/set/participants")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response setParticipants(AddListToEntityVO view) {
		log.info("Add participants " + view.getIds());
		try {
			if (projectService.setParticipants(view.getEntityId(), view.getIds())) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@PUT
	public Response save(ProjectVO projectVO) {
		log.info("Saving project: " + projectVO.getName());
		try {
			projectService.save(projectVO);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

}
