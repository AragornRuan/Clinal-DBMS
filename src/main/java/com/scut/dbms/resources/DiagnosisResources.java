package com.scut.dbms.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scut.dbms.core.Diagnosis;
import com.scut.dbms.db.DiagnosisDAO;

@Path("/diagnosis")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class DiagnosisResources {
	
	private DiagnosisDAO diagnosisDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosisResources.class);
	
	public DiagnosisResources(DiagnosisDAO diagnosisDAO) {
		this.diagnosisDAO = diagnosisDAO;
	}
	
	@GET
	public List<Diagnosis> queryDiagnosis(@QueryParam("admissionnumber") String admissionnumber) {
		return diagnosisDAO.queryDiagnosis(admissionnumber);
	}
}