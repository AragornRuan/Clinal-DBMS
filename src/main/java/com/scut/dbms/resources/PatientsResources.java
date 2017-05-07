package com.scut.dbms.resources;

import com.scut.dbms.core.Patients;
import com.scut.dbms.core.Times;
import com.scut.dbms.db.PatientsDAO;
import com.scut.dbms.db.TimesDAO;
import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.api.UpdateResponseMessage;
import com.scut.dbms.auth.DefaultJwtCookiePrincipal;
import com.scut.dbms.error.ErrorCode;

import io.dropwizard.auth.Auth;

import com.scut.dbms.api.InsertResponseMessage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class PatientsResources {

	private final PatientsDAO patientsDAO;
	private final TimesDAO timesDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientsResources.class);

	public PatientsResources(PatientsDAO patientsDAO, TimesDAO timesDAO) {
		this.patientsDAO = patientsDAO;
		this.timesDAO = timesDAO;
	}

    //根据id获取patients表数据
	@GET
	@Path("/id")
	public Patients findById(@QueryParam("id") int id) {
		return patientsDAO.findById(id);
	}

    //获取patients所有数据
	@GET
	public List<Patients> findAll(@Auth DefaultJwtCookiePrincipal principal) {
		return patientsDAO.findAll();
	}

    //根据住院号获取patients表数据
	@GET
	@Path("/adnum")
	public Patients findByAdmissionnumber(@QueryParam("admissionnumber") String admissionnumber) {
		Patients p = patientsDAO.findByAdmissionnumber(admissionnumber);
		if (p == null) {
			LOGGER.info("patients is not exist.");
		}
		return patientsDAO.findByAdmissionnumber(admissionnumber);
	}

	/**
	 * 插入数据到patients表中
	 * 
	 * @param patients
	 * @return 成功返回1，失败返回0
	 */
	@POST
	@Path("/insert")
	public ResponseMessage insert(@NotNull @Valid Patients patients) {
		Patients p = patientsDAO.findByAdmissionnumber(patients.getAdmissionnumber());
		if (p == null) {
			patientsDAO.insert(patients);
			int id = patientsDAO.findId(patients.getAdmissionnumber());
			timesDAO.insert(new Times(id, 0));
			return new InsertResponseMessage(id, ErrorCode.SUCCESS, "Insert data into patients success.");
		}
		else {
			return new ResponseMessage(ErrorCode.PATIENT_EXIST_ERROR, "Paient " + patients.getAdmissionnumber() + " exists.");
		}
	}

    //更新patients表数据
	@POST
	@Path("/update")
	public ResponseMessage update(@NotNull @Valid Patients patients, @QueryParam("id") int id) {
		patientsDAO.update(patients, id);
		return new UpdateResponseMessage(id, ErrorCode.SUCCESS, "Update patients success.");
	}

}
