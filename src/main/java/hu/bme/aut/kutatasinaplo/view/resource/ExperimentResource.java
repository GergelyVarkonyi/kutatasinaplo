package hu.bme.aut.kutatasinaplo.view.resource;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

@Path("/experiment")
public class ExperimentResource {

	private ExperimentService experimentService;
	private DataViewMapper mapper;

	@Inject
	public ExperimentResource(ExperimentService experimentService, DataViewMapper mapper) {
		this.experimentService = experimentService;
		this.mapper = mapper;
	}

	@POST
	@Path("/create")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response create(ExperimentVO view) {
		try {
			if (experimentService.create(view)) {
				return Response.ok().build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/list/experiment")
	public List<ExperimentVO> listExperiments() {
		return Lists.transform(experimentService.loadAll(), new Function<Experiment, ExperimentVO>() {

			@Override
			public ExperimentVO apply(Experiment input) {
				return mapper.map(input);
			}
		});
	}
}