package hu.bme.aut.kutatasinaplo.view.resource;

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

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;
import hu.bme.aut.kutatasinaplo.view.model.UsersVO;
import lombok.extern.java.Log;

@Path("/user")
@Log
public class UserResource {

	private UserService userService;
	private DataViewMapper mapper;
	private AuthService authService;

	@Inject

	public UserResource(UserService userService, DataViewMapper mapper, AuthService authService) {
		this.userService = userService;
		this.mapper = mapper;
		this.authService = authService;
	}

	@POST
	@Path("/create")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response create(UserVO view) {
		log.info("Create user: " + view.getName());
		try {
			if (userService.createUser(view)) {
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
	public UserVO load(String id) {
		log.info("Load experiment: " + id);
		try {
			User user = userService.loadById(Integer.valueOf(id));
			if (user != null) {
				return mapper.map(user);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/list/user")
	public List<UserVO> listUsers() {
		authService.checkCurrentUserIsAdmin();

		return Lists.transform(userService.loadAll(), new Function<User, UserVO>() {

			@Override
			public UserVO apply(User input) {
				return mapper.map(input);
			}
		});
	}

	@POST
	@Path("/saveroles")
	public Response saveroles(UsersVO users) {
		if (userService.saveRoles(users.getUsers())) {
			return Response.ok().build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

}