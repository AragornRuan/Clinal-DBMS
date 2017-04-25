package com.scut.dbms.resources;

import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.core.CDG;
import com.scut.dbms.db.CDGDAO;
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

@Path("/cdg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class CDGResources {

	private CDGDAO cdgDAO;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CDGResources.class);
	
	public CDGResources(CDGDAO cdgDAO) {
		this.cdgDAO = cdgDAO;
	}
	
	//根据testId获取cdg表数据
	@GET
	public CDG findByTestId(@QueryParam("testId") String testId) {
		return cdgDAO.findByTestId(testId);
	}
	
	//向cdg表中插入数据
	@POST
	@Path("/insert")
	public ResponseMessage insert(@NotNull @Valid CDG cdg) {
		LOGGER.info("Inserting CDG in MySQL.");
		cdgDAO.insert(cdg);
		LOGGER.info("Inserted CDG in MySQL.");
		return new ResponseMessage(ErrorCode.SUCCESS, "Inserted CDG in MySQL.");
	}
	
}
