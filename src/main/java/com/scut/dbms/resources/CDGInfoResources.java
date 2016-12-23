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

import com.scut.dbms.core.CDGInfo;
import com.scut.dbms.core.Patients;
import com.scut.dbms.db.CDGInfoDAO;
import com.scut.dbms.db.PatientsDAO;

@Path("/cdgInfo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class CDGInfoResources {

	private CDGInfoDAO cdgInfoDAO;
	private PatientsDAO patientsDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(CDGInfoResources.class);
	
	public CDGInfoResources(CDGInfoDAO cdgInfoDAO, PatientsDAO patientsDAO) {
		this.cdgInfoDAO = cdgInfoDAO;
		this.patientsDAO = patientsDAO;
	}
	
	@GET
	public List<CDGInfo> queryCDGInfo(@QueryParam("patientId") int patientId) {
		return cdgInfoDAO.queryCDGInfo(patientId);
	}
	
	@GET
	@Path("/adnum")
	public List<CDGInfo> queryCDGInfoByAdmissionnumber(@QueryParam("admissionnumber") String admissionnumber) {
		Patients patients = patientsDAO.findByAdmissionnumber(admissionnumber);
		return cdgInfoDAO.queryCDGInfo(patients.getId());
	}
	
}
