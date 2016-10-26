package com.scut.dbms.resources;

import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.core.ECG;
import com.scut.dbms.db.ECGDAO;
import com.scut.dbms.error.ErrorCode;

import redis.clients.jedis.Jedis;

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
	
	private static final String REDIS_HOST = "localhost";
	private static final String LIST_NAME = "testId";
	private static final String HASH_NAME = "ecgMap";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ECGResources.class);
	
	private Jedis jedis;

	public ECGResources(ECGDAO ecgDAO) {
		this.ecgDAO = ecgDAO;
		jedis = new Jedis(REDIS_HOST);
	}
	
	@GET
	public ECG findByTestId(@QueryParam("testId") String testId) {
		return ecgDAO.findByTestId(testId);
	}
	
	@POST
	@Path("/insert")
	public ResponseMessage insert(@Valid @NotNull ECG ecg) {
		
		LOGGER.info("Inserting ECG {} into Redis.", ecg.getTestId());

		jedis.rpush(LIST_NAME, ecg.getTestId());
		jedis.hset(HASH_NAME, ecg.getTestId(), ecg.getEcgData());
		LOGGER.info("Inserted ECG {} into Redis.", ecg.getTestId());
	
		LOGGER.info("Inserting ECG {} into MySQL.", ecg.getTestId());
		ecgDAO.insert(ecg);
		LOGGER.info("Inserted ECG {} into MySQL.", ecg.getTestId());
		return new ResponseMessage(ErrorCode.SUCCESS, "Inserted ECG.");
	}

}
