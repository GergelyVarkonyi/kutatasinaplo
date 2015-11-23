package hu.bme.aut.kutatasinaplo.appconfig;

import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.service.impl.AuthServiceImpl;
import hu.bme.aut.kutatasinaplo.service.impl.UserServiceImpl;
import hu.bme.aut.kutatasinaplo.view.resource.AuthResource;
import hu.bme.aut.kutatasinplo.security.filter.AuthFilter;
import hu.bme.aut.kutatasinplo.security.filter.ShiroFilter;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class KNServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		ResourceConfig rc = new PackagesResourceConfig("hu.bme.aut.kutatasinaplo.view.resource");
		for (Class<?> resource : rc.getClasses()) {
			bind(resource);
		}

		serve("/rest/*").with(GuiceContainer.class);

		filter("/*").through(ShiroFilter.class);
		filter("/auth/*").through(AuthFilter.class);
	}

}
