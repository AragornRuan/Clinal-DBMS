package com.scut.dbms.resources;

import com.scut.dbms.core.Cases;
import com.scut.dbms.core.Patients;
import com.scut.dbms.db.CasesDAO;
import com.scut.dbms.db.PatientsDAO;
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
	private final PatientsDAO patientsDAO;
	
	public CasesResources(CasesDAO casesDAO, PatientsDAO patientsDAO) {
		this.casesDAO = casesDAO;
		this.patientsDAO = patientsDAO;
	}
	
	//获取所有cases表中的数据
	@GET
	public List<Cases> findAll() {
		return casesDAO.findAll();
	}
	
	//根据patientId获取cases表数据
	@GET
	@Path("/patientId")
	public Cases findByPatientsId(@QueryParam("patientId") int patientsId) {
		return casesDAO.findByPatientsId(patientsId);
	}
	
	//根据住院号获取cases表数据
	@GET
	@Path("/adnum")
	public Cases findByAdnum(@QueryParam("admissionnumber") String admissionnumber) {
		Patients patients = patientsDAO.findByAdmissionnumber(admissionnumber);
		return casesDAO.findByPatientsId(patients.getId());
	}
	
	//向cases表中插入数据
	@POST
	@Path("/insert")
	public ResponseMessage insert(@NotNull @Valid Cases cases) {
		casesDAO.insert(cases);
		return new ResponseMessage(ErrorCode.SUCCESS, "insert into cases success.");
	}
	
	//向cases表中更新数据
	@POST
	@Path("/update")
	public ResponseMessage update(@NotNull @Valid Cases cases, @QueryParam("id") int id) {
		casesDAO.update(cases, id);
		return new ResponseMessage(ErrorCode.SUCCESS, "update cases success.");
	}
}
