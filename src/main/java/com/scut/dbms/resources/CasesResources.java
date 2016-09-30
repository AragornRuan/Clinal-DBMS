package com.scut.dbms.resources;

import com.scut.dbms.core.Cases;
import com.scut.dbms.db.CasesDAO;
import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.error.ErrorCode;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Path("/cases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class CasesResources {
	private final CasesDAO casesDAO;
	
	public CasesResources(CasesDAO casesDAO) {
		this.casesDAO = casesDAO;
	}
	
	@GET
	public List<Cases> findAll() {
		return casesDAO.findAll();
	}
	
	@GET
	@Path("/patientId")
	public Cases findByPatientsId(@QueryParam("patientId") int patientsId) {
		return casesDAO.findByPatientsId(patientsId);
	}
	
	@POST
	@Path("/insert")
	public ResponseMessage insert(@NotNull @Valid Cases cases) {
		casesDAO.insert(cases);
		return new ResponseMessage(ErrorCode.SUCCESS, "insert into cases success.");
	}
	
	@POST
	@Path("/update")
	public ResponseMessage update(@NotNull @Valid Cases cases, @QueryParam("id") int id) {
//		casesDAO.update(cases, id);
		casesDAO.update(cases, id);
		return new ResponseMessage(ErrorCode.SUCCESS, "update cases success.");
	}
}
