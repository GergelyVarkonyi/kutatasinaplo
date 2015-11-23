package hu.bme.aut.kutatasinaplo.db;

import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

public class InitDB {
	@Test
	public void Init() {
		Configuration configuration = new Configuration();
		configuration.configure("/META-INF/hibernate.cfg.xml");

		new SchemaExport(configuration).create(true, true);

		EntityManager em = Persistence.createEntityManagerFactory("kutatasiNaploPU").createEntityManager();
		User user = User.builder()
				.name("admin")
				.email("admin@nodomain.com")
				.password("0000")
				.role(Role.ADMIN)
				.build();

		em.getTransaction().begin();
		em.persist(user);
		em.flush();
		em.getTransaction().commit();
	}
}
