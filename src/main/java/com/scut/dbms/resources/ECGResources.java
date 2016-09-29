package com.scut.dbms.resources;

import com.scut.dbms.core.ECG;
import com.scut.dbms.db.ECGDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/ecg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class ECGResources {
	
	private ECGDAO ecgDAO;
	
	public ECGResources(ECGDAO ecgDAO) {
		this.ecgDAO = ecgDAO;
	}
	
	@GET
	public ECG findByTestId(@QueryParam("testId") String testId) {
		return ecgDAO.findByTestId(testId);
	}

}
