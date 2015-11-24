package hu.bme.aut.kutatasinaplo.view.resource;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

@Path("/auth")
public class AuthResource {

	private AuthService authService;
	private UserService userService;
	private DataViewMapper mapper;

	@Inject
	public AuthResource(AuthService authService, UserService userService, DataViewMapper mapper) {
		this.authService = authService;
		this.userService = userService;
		this.mapper = mapper;
	}

	@POST
	@Path("/login")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response login(@Context UriInfo uriInfo, UserVO user) {
		try {
			if (authService.login(user.getName(), user.getPwd())) {
				return Response.seeOther(uriInfo.getBaseUriBuilder().path("../auth/index.html").build()).build();
			} else {
				return Response.status(Status.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/list/roles")
	public List<Role> listRoles() {
		return Lists.newArrayList(Role.values());
	}

	@GET
	@Path("/list/users")
	public List<User> listUsers() {
		return userService.loadAll();
	}

	@GET
	@Path("/current")
	public UserVO getCurrentUser() {
		return mapper.map(authService.getCurrentUser());
	}
}