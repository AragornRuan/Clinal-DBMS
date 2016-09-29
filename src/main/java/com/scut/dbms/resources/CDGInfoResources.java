package com.scut.dbms.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.scut.dbms.core.CDGInfo;
import com.scut.dbms.db.CDGInfoDAO;

@Path("/cdgInfo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class CDGInfoResources {

	private CDGInfoDAO cdgInfoDAO;
	
	public CDGInfoResources(CDGInfoDAO cdgInfoDAO) {
		this.cdgInfoDAO = cdgInfoDAO;
	}
	
	@GET
	public List<CDGInfo> queryCDGInfo(@QueryParam("patientId") int patientId) {
		return cdgInfoDAO.queryCDGInfo(patientId);
	}
	
}
