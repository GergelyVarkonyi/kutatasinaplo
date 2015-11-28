package hu.bme.aut.kutatasinaplo.view.resource;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.ExperimentType;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.view.model.AddListToEntityVO;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;
import hu.bme.aut.kutatasinaplo.view.model.IdVO;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lombok.extern.java.Log;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

@Path("/experiment")
@Log
public class ExperimentResource {

	private ExperimentService experimentService;
	private AuthService authService;
	private DataViewMapper mapper;

	@Inject
	public ExperimentResource(ExperimentService experimentService, AuthService authService, DataViewMapper mapper) {
		this.experimentService = experimentService;
		this.authService = authService;
		this.mapper = mapper;
	}

	@POST
	@Path("/create")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response create(ExperimentVO view) {
		log.info("Create experiment: " + view.getName());
		try {
			if (experimentService.create(view)) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (ValidateException e) {
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/load")
	@Produces(value = MediaType.APPLICATION_JSON)
	public ExperimentVO load(String id) {
		log.info("Load experiment: " + id);
		try {
			Experiment experiment = experimentService.loadById(Integer.valueOf(id));
			if (experiment != null) {
				return mapper.map(experiment);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/list/experiment")
	public List<ExperimentVO> listExperiments() {
		User currentUser = authService.getCurrentUser();

		ArrayList<ExperimentVO> views = Lists.newArrayList();
		for (Experiment experiment : experimentService.loadAll()) {
			// If its public than add to list
			if (experiment.getType() == ExperimentType.PUBLIC) {
				views.add(mapper.map(experiment));
				continue;
			} else {
				if (currentUser != null) {
					// If current use is an admin
					if (currentUser.getRole() == Role.ADMIN) {
						views.add(mapper.map(experiment));
						continue;
					}

					int id = currentUser.getId();
					// If the owner is the logged in user
					if (id == experiment.getOwner().getId()) {
						views.add(mapper.map(experiment));
						continue;
					} else {
						// If the logged in user participate in the experiment
						List<User> participants = experiment.getParticipants();
						if (participants != null) {
							List<Integer> participantIds = Lists.transform(participants, new Function<User, Integer>() {
								@Override
								public Integer apply(User input) {
									return input.getId();
								}
							});
							if (participantIds.contains(id)) {
								views.add(mapper.map(experiment));
								continue;
							}
						}
					}
				}
			}
		}
		return views;
	}

	@POST
	@Path("/delete")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response delete(IdVO view) {
		int id = view.getId();
		log.info("Delete experiment: " + id);
		try {
			if (experimentService.delete(id)) {
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
	@Path("/set/participants")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response setParticipants(AddListToEntityVO view) {
		log.info("Add participants " + view.getIds());
		try {
			if (experimentService.setParticipants(view.getEntityId(), view.getIds())) {
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
	@Path("/save")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response setParticipants(ExperimentVO view) {
		log.info("Save experiment " + view.getId());
		try {
			if (experimentService.save(view)) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (ValidateException e) {
			return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}