package com.scut.dbms.resources;

import com.scut.dbms.core.Patients;
import com.scut.dbms.db.PatientsDAO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.zip.CheckedOutputStream;

@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class PatientsResource {
	
	private final PatientsDAO patientsDAO;
	
	public PatientsResource(PatientsDAO patientsDAO) {
		this.patientsDAO = patientsDAO;
	}
	
	@GET
	@Path("/id")
	public Patients findById(@QueryParam("id") int id) {
		return patientsDAO.findById(id);
	}
	
	@GET
	@Path("/id/range")
	public List<Patients> findById(@QueryParam("min") int min, @QueryParam("max") int max) {
		return patientsDAO.findById(min, max);
	}
	
	@GET
	public List<Patients> findAll() {
		return patientsDAO.findAll();
	}
	
	@GET
	@Path("/adnum")
	public Patients findByAdmissionnumber(@QueryParam("admissionnumber") String admissionnumber) {
		return patientsDAO.findByAdmissionnumber(admissionnumber);
	}
	
	/**
	 * 插入数据到patients表中
	 * @param patients
	 * @return 成功返回1，失败返回0
	 */
	@POST
	@Path("/insert")
	public int insert(@NotNull @Valid Patients patients) {
		try {
			patientsDAO.insert(patients);
			return 0; 
		} catch (Exception exception) {
			return -1;
		}
		
	}


}
