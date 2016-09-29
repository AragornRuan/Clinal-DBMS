package com.scut.dbms.resources;

import com.scut.dbms.core.CDG;
import com.scut.dbms.db.CDGDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/cdg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CDGResources {

	private CDGDAO cdgDAO;
	
	public CDGResources(CDGDAO cdgDAO) {
		this.cdgDAO = cdgDAO;
	}
	
	@GET
	CDG findByTestId(@QueryParam("testId") String testId) {
		return cdgDAO.findByTestId(testId);
	}
	
}
