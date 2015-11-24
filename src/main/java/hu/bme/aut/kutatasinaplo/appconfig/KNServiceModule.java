package hu.bme.aut.kutatasinaplo.appconfig;

import hu.bme.aut.kutatasinaplo.mapper.DataViewMapper;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinaplo.service.UserService;
import hu.bme.aut.kutatasinaplo.service.impl.AuthServiceImpl;
import hu.bme.aut.kutatasinaplo.service.impl.ExperimentServiceImpl;
import hu.bme.aut.kutatasinaplo.service.impl.UserServiceImpl;
import hu.bme.aut.kutatasinaplo.view.resource.AuthResource;
import hu.bme.aut.kutatasinaplo.view.resource.ExperimentResource;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class KNServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserService.class).to(UserServiceImpl.class);
		bind(AuthService.class).to(AuthServiceImpl.class);
		bind(ExperimentService.class).to(ExperimentServiceImpl.class);
		bind(AuthResource.class);
		bind(ExperimentResource.class);
		bind(DataViewMapper.class);
	}

	@Provides
	@Singleton
	EntityManager provideEntityManager() {
		return Persistence.createEntityManagerFactory("kutatasiNaploPU").createEntityManager();
	}
}
