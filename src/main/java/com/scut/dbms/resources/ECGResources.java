package com.scut.dbms.resources;

import com.scut.dbms.api.InsertResponseMessage;
import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.core.ECG;
import com.scut.dbms.db.ECGDAO;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ecg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class ECGResources {
	
	private ECGDAO ecgDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ECGResources.class);
	
	public ECGResources(ECGDAO ecgDAO) {
		this.ecgDAO = ecgDAO;
	}
	
	@GET
	public ECG findByTestId(@QueryParam("testId") String testId) {
		return ecgDAO.findByTestId(testId);
	}
	
	@POST
	@Path("/learning")
	public ResponseMessage insert(@Valid @NotNull ECG ecg) {
		LOGGER.info("Start to insert ECG into ecg table.");
		ecgDAO.insert(ecg);
		LOGGER.info("Insert ECG into ecg table success.");
		return new ResponseMessage(ErrorCode.SUCCESS, "Generate CDG success.");
	}

}
