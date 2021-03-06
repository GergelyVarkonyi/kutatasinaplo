package hu.bme.aut.kutatasinaplo.view.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ProjectService;
import hu.bme.aut.kutatasinaplo.view.model.AddListToEntityVO;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;
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
	public List<ProjectVO> listAccessibleProjects() {
		return projectService.listAccessibleProjects();
	}

	@GET
	@Path("/{id}")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ProjectVO load(@PathParam(value = "id") String id) {
		log.info("Load project: " + id);
		try {
			return projectService.loadProjectWithPermissionChecks(id);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}

	@DELETE
	@Path("/delete/{id}")
	@Produces(value = MediaType.APPLICATION_JSON)

	public Response delete(@PathParam(value = "id") String id) {
		authService.checkCurrentUserIsAdmin();
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

	@POST
	@Path(value = "/add-everyone")
	public Response addEveryParticipantsToExperiments(String id) {
		try {
			if (projectService.addEveryParticipantsToExperiments(Integer.valueOf(id))) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@POST
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
