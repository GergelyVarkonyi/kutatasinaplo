package hu.bme.aut.kutatasinaplo.appconfig;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new KNShiroModule(), new KNServiceModule(), new KNServletModule());
	}

}