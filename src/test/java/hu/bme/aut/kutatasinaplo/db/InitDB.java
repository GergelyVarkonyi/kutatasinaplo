package hu.bme.aut.kutatasinaplo.db;

import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.ExperimentType;
import hu.bme.aut.kutatasinaplo.model.KeyValuePair;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.shiro.crypto.hash.Sha512Hash;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import com.google.common.collect.Lists;

public class InitDB {
	@Test
	public void Init() {
		Configuration configuration = new Configuration();
		configuration.configure("/META-INF/hibernate.cfg.xml");

		new SchemaExport(configuration).create(true, true);

		EntityManager em = Persistence.createEntityManagerFactory("kutatasiNaploPU").createEntityManager();

		String salt = UUID.randomUUID().toString();
		Sha512Hash sha512Hash = new Sha512Hash("0000", salt, 10);
		User admin = User.builder()
				.name("admin")
				.email("admin@nodomain.com")
				.password(sha512Hash.toBase64())
				.role(Role.ADMIN)
				.salt(salt)
				.build();

		salt = UUID.randomUUID().toString();
		sha512Hash = new Sha512Hash("0000", salt, 10);
		User user = User.builder()
				.name("test")
				.email("test@nodomain.com")
				.password(sha512Hash.toBase64())
				.role(Role.USER)
				.salt(salt)
				.build();

		Experiment publicExp = Experiment.builder()
				.name("Public test")
				.description("Initital test experiment, which is public.")
				.owner(user)
				.type(ExperimentType.PUBLIC)
				.urls(Lists.newArrayList(KeyValuePair.builder()
						.keyOfInstance("index")
						.value("http://www.index.hu")
						.build()))
				.build();

		Experiment privateExp = Experiment.builder()
				.name("Private test")
				.description("Initital test experiment, which is private.")
				.owner(user)
				.type(ExperimentType.PRIVATE)
				.participants(Lists.newArrayList(user))
				.urls(Lists.newArrayList(KeyValuePair.builder()
						.keyOfInstance("index")
						.value("http://www.index.hu")
						.build()))
				.build();

		em.getTransaction().begin();
		em.persist(admin);
		em.persist(user);
		em.persist(publicExp);
		em.persist(privateExp);
		em.flush();
		em.getTransaction().commit();
	}
}
