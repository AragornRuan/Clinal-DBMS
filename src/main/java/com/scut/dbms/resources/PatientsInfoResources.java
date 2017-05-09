package com.scut.dbms.resources;

import com.scut.dbms.auth.DefaultJwtCookiePrincipal;
import com.scut.dbms.core.PatientsInfo;
import com.scut.dbms.db.PatientsInfoDAO;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;

/**
 * 对应于patientInfoQuery存储过程的API
 */
@Path("/patientsinfo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class PatientsInfoResources {
	private PatientsInfoDAO patientsInfoDAO;
	
    public PatientsInfoResources(PatientsInfoDAO patientsInfoDAO) {
		this.patientsInfoDAO = patientsInfoDAO;
	}
	
	//调用存储过程patientInfoQuery
	@GET
	public List<PatientsInfo> queryPatientsInfo(@Auth DefaultJwtCookiePrincipal principal, @QueryParam("name") String name, @QueryParam("male") int male, @QueryParam("female") int female,
			@QueryParam("admissionnumber") String addmissionnumber, @QueryParam("ecgNormal") int ecgNormal, 
			@QueryParam("ecgUnusual") int ecgUnusual, @QueryParam("ctNormal") int ctNormal, @QueryParam("ctStricture") int ctStricture, 
			@QueryParam("ctNothing") int ctNothing, @QueryParam("radiographyNormal") int radiographyNormal, 
			@QueryParam("radiographyStricture") int radiographyStricture, @QueryParam("radiographyNoting") int radiographyNoting, 
			@QueryParam("negative") int negative, @QueryParam("positive") int positive, @QueryParam("probablePositive") int probablePositive,
			@QueryParam("times") int times, @QueryParam("mindray") int mindray, @QueryParam("aika") int aika, @QueryParam("cardis") int cardis, 
			@QueryParam("im") int im, @QueryParam("ar") int ar) {
		return patientsInfoDAO.queryPatientsInfo(name, male, female, addmissionnumber, ecgNormal, ecgUnusual, ctNormal, ctStricture, ctNothing, radiographyNormal, 
					radiographyStricture, radiographyNoting, negative, positive, probablePositive, times, mindray, aika, cardis, im, ar);
	}
}
