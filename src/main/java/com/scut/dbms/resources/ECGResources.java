package com.scut.dbms.resources;

import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.constants.FileConstants;
import com.scut.dbms.core.CDG;
import com.scut.dbms.core.ECG;
import com.scut.dbms.db.CDGDAO;
import com.scut.dbms.db.ECGDAO;
import com.scut.dbms.error.ErrorCode;
import com.scut.dbms.utils.FileOperations;
//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;

import io.dropwizard.jdbi.ImmutableListContainerFactory;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ecg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
public class ECGResources {
	
	private ECGDAO ecgDAO;
	private CDGDAO cdgDAO;
	
	private static final String REDIS_HOST = "localhost";
	private static final String LIST_NAME = "testId";
	private static final String ECG_MAP = "ecgMap";
	private static final String CDG_MAP = "cdgMap";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ECGResources.class);
	
	private Jedis jedis;

	public ECGResources(ECGDAO ecgDAO, CDGDAO cdgDAO) {
		this.ecgDAO = ecgDAO;
		this.cdgDAO = cdgDAO;
		jedis = new Jedis(REDIS_HOST);
	}
	
	@GET
	public ECG findByTestId(@QueryParam("testId") String testId) {
		return ecgDAO.findByTestId(testId);
	}
	
	@POST
	@Path("/insert")
	public ResponseMessage insert(@Valid @NotNull ECG ecg) {
		
		String testId = ecg.getTestId();
		LOGGER.info("Inserting ECG {} into Redis.", testId);

		jedis.rpush(LIST_NAME, testId);
		jedis.hset(ECG_MAP, testId, ecg.getEcgData());
		LOGGER.info("Inserted ECG {} into Redis.", testId);
	
		LOGGER.info("Inserting ECG {} into MySQL.", testId);
		ecgDAO.insert(ecg);
		LOGGER.info("Inserted ECG {} into MySQL.", testId);
		return new ResponseMessage(ErrorCode.SUCCESS, "Inserted ECG.");
	}
	
	@POST
	@Path("/learn")
	public ResponseMessage learn(@Valid @NotNull ECG ecg) throws InterruptedException {
		
		String testId = ecg.getTestId();
		LOGGER.info("Inserting ECG {} into Redis.", testId);

		jedis.rpush(LIST_NAME, testId);
		jedis.hset(ECG_MAP, testId, ecg.getEcgData());
		LOGGER.info("Inserted ECG {} into Redis.", testId);
	
		LOGGER.info("Inserting ECG {} into MySQL.", testId);
		ecgDAO.insert(ecg);
		LOGGER.info("Inserted ECG {} into MySQL.", testId);
		
		LOGGER.info("Generating CDG {}.", testId);
		String cdgData = null;
		while ((cdgData = jedis.hget(CDG_MAP, testId)) == null) {
			Thread.sleep(50);
		}
		LOGGER.info("Generated CDG {}.", testId);
		
		LOGGER.info("Inserting CDG {} into MySQL.", testId);
		cdgDAO.insert(new CDG(testId, cdgData, "unknown", 0.0, 0.0));
		LOGGER.info("Inserted CDG {} into MySQL.", testId);
		
		return new ResponseMessage(ErrorCode.SUCCESS, "Inserted ECG, generated CDG and inserted CDG.");
	}
	
	@POST
	@Path("/multilearn")
	public ResponseMessage multiLearn(@Valid @NotNull ArrayList<ECG> ecgs) throws InterruptedException {
		
		HashSet<String> testIds = new HashSet<String>();
		LOGGER.info("Inserting ECGs into Redis and MySQL.");
		for (ECG ecg : ecgs) {
			testIds.add(ecg.getTestId());
			jedis.hset(ECG_MAP, ecg.getTestId(), ecg.getEcgData());
			jedis.rpush(LIST_NAME, ecg.getTestId());
			ecgDAO.insert(ecg);
		}
		LOGGER.info("Inserted ECGs into Redis and MySQL.");
	
		
/*		LOGGER.info("Generating and inserting CDGs.");
		for (String testId : testIds) {
			String cdgData = null;
			while ((cdgData = jedis.hget(CDG_MAP, testId)) == null) {
				Thread.sleep(50);
			}
			LOGGER.info("Inserting CDG {}.", testId);
			cdgDAO.insert(new CDG(testId, cdgData, "unknown", 0.0, 0.0));
			LOGGER.info("Inserted CDG {}.", testId);
		}
		LOGGER.info("Generated and inserted CDGs.");*/
		
		return new ResponseMessage(ErrorCode.SUCCESS, "Inserted ECG, generated CDG and inserted CDG.");
	}
	
	@GET
	@Path("/hadoop")
	public ResponseMessage hadoop() throws IOException, InterruptedException {
		
		long threadId = Thread.currentThread().getId();
		String inputDir = FileConstants.HADOOP_INPUT_DIR + threadId;
		String outputDir = FileConstants.HADOOP_OUTPUT_DIR + threadId;
		
		FileOperations.makeDir(inputDir);
		FileOperations.makeDir(outputDir);
		
		StringBuilder runHadoop = new StringBuilder();
		runHadoop.append("python cardio.py ").append(inputDir).append(" ").append(outputDir);
		
		Process process = Runtime.getRuntime().exec(runHadoop.toString());
		process.waitFor();
		
		return new ResponseMessage(ErrorCode.SUCCESS, "Calculate CDG successfully.");
		
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResponseMessage uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		String uploadedFileLocation = "G:/" + fileDetail.getFileName();
		FileOperations.writeToFile(uploadedInputStream, uploadedFileLocation);
		return new ResponseMessage(ErrorCode.SUCCESS, "Upload file " + fileDetail.getFileName() + " successfully.");
	}

}
