package com.scut.dbms;

import org.skife.jdbi.v2.DBI;

import com.scut.dbms.DBMSConfiguration;
import com.scut.dbms.db.CasesDAO;
import com.scut.dbms.db.PatientsDAO;
import com.scut.dbms.db.PatientsInfoDAO;
import com.scut.dbms.resources.CasesResources;
import com.scut.dbms.resources.PatientsInfoResources;
import com.scut.dbms.resources.PatientsResources;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
/**
 * Hello world!
 *
 */
public class DBMSApplication extends Application<DBMSConfiguration> {
	public static void main(String[] args) throws Exception {
		new DBMSApplication().run(args);
	}
	
	@Override
	public void run(DBMSConfiguration configuration, Environment environment) throws Exception {
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
		final PatientsDAO patientsDAO = jdbi.onDemand(PatientsDAO.class);
		final CasesDAO casesDAO = jdbi.onDemand(CasesDAO.class);
		final PatientsInfoDAO patientsInfoDAO = jdbi.onDemand(PatientsInfoDAO.class);
		environment.jersey().register(new PatientsResources(patientsDAO));
		environment.jersey().register(new CasesResources(casesDAO));
		environment.jersey().register(new PatientsInfoResources(patientsInfoDAO));
	}
}
