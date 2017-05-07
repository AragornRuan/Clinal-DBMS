package com.scut.dbms;


import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.skife.jdbi.v2.DBI;

import com.scut.dbms.DBMSConfiguration;
import com.scut.dbms.auth.JwtCookieAuthBundle;
import com.scut.dbms.db.CDGDAO;
import com.scut.dbms.db.CDGInfoDAO;
import com.scut.dbms.db.CasesDAO;
import com.scut.dbms.db.DiagnosisDAO;
import com.scut.dbms.db.PatientsDAO;
import com.scut.dbms.db.PatientsInfoDAO;
import com.scut.dbms.db.TimesDAO;
import com.scut.dbms.db.UsersDAO;
import com.scut.dbms.db.ECGDAO;
import com.scut.dbms.db.InvitationDAO;
import com.scut.dbms.resources.AuthResources;
import com.scut.dbms.resources.CDGInfoResources;
import com.scut.dbms.resources.CDGResources;
import com.scut.dbms.resources.CasesResources;
import com.scut.dbms.resources.DiagnosisResources;
import com.scut.dbms.resources.PatientsInfoResources;
import com.scut.dbms.resources.PatientsResources;
import com.scut.dbms.resources.ECGResources;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import io.dropwizard.setup.Bootstrap;

/**
 * Hello world!
 *
 */
public class DBMSApplication extends Application<DBMSConfiguration> {
	public static void main(String[] args) throws Exception {
		new DBMSApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<DBMSConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
		bootstrap.addBundle(JwtCookieAuthBundle.getDefault());
	}

	@Override
	public void run(DBMSConfiguration configuration, Environment environment) throws Exception {
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
		final PatientsDAO patientsDAO = jdbi.onDemand(PatientsDAO.class);
		final CasesDAO casesDAO = jdbi.onDemand(CasesDAO.class);
		final PatientsInfoDAO patientsInfoDAO = jdbi.onDemand(PatientsInfoDAO.class);
		final ECGDAO ecgDAO = jdbi.onDemand(ECGDAO.class);
		final CDGDAO cdgDAO = jdbi.onDemand(CDGDAO.class);
		final CDGInfoDAO cdgInfoDAO = jdbi.onDemand(CDGInfoDAO.class);
		final TimesDAO timesDAO = jdbi.onDemand(TimesDAO.class);
		final DiagnosisDAO diagnosisDAO = jdbi.onDemand(DiagnosisDAO.class);
		final UsersDAO usersDAO = jdbi.onDemand(UsersDAO.class);
		final InvitationDAO invitationDAO = jdbi.onDemand(InvitationDAO.class);
		environment.jersey().register(MultiPartFeature.class);
		environment.jersey().register(new PatientsResources(patientsDAO, timesDAO));
		environment.jersey().register(new CasesResources(casesDAO, patientsDAO));
		environment.jersey().register(new PatientsInfoResources(patientsInfoDAO));
		environment.jersey().register(new ECGResources(ecgDAO, cdgDAO, patientsDAO, timesDAO));
		environment.jersey().register(new CDGResources(cdgDAO));
		environment.jersey().register(new CDGInfoResources(cdgInfoDAO, patientsDAO));
		environment.jersey().register(new DiagnosisResources(diagnosisDAO));
		environment.jersey().register(new AuthResources(usersDAO, invitationDAO));
	}
}
