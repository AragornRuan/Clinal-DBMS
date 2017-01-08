package com.scut.dbms.resources;

import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.api.UploadFileResponseMessage;
import com.scut.dbms.constants.FileConstants;
import com.scut.dbms.core.CDG;
import com.scut.dbms.core.ECG;
import com.scut.dbms.db.CDGDAO;
import com.scut.dbms.db.ECGDAO;
import com.scut.dbms.error.ErrorCode;
import com.scut.dbms.utils.FileOperations;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
	private static final String HADOOP_SUCCESS = "_SUCCESS";
	
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
	public ResponseMessage hadoop(@QueryParam("threadId") long threadId) throws IOException, InterruptedException {
		
		String inputDir = FileConstants.HADOOP_INPUT_DIR + threadId;
		String outputDir = FileConstants.HADOOP_OUTPUT_DIR + threadId;
		
		FileOperations.makeDir(outputDir);
		
		StringBuilder runHadoop = new StringBuilder();
		runHadoop.append("python cardio.py ").append(inputDir).append(" ").append(outputDir);
		
		Process process = Runtime.getRuntime().exec(runHadoop.toString());
		LOGGER.info("Generating CDG.");
		process.waitFor();
		LOGGER.info("Generated CDG.");
		
		LOGGER.info("Storing CDG.");
		ResponseMessage response = storeCDG(outputDir);
		LOGGER.info("Stored CDG.");

		return response;
		
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResponseMessage uploadFile(
			@FormDataParam("files") InputStream uploadedInputStream,
			@FormDataParam("files") FormDataContentDisposition fileDetail) throws IOException {
		long threadId = Thread.currentThread().getId();
		String inputDir = FileConstants.HADOOP_INPUT_DIR + threadId + FileConstants.FILE_SEPARATOR; 
		FileOperations.makeDir(inputDir);
		String uploadedFileLocation = inputDir + fileDetail.getFileName();
		FileOperations.writeToFile(uploadedInputStream, uploadedFileLocation);
		return new UploadFileResponseMessage(threadId, ErrorCode.SUCCESS, "Upload file " + fileDetail.getFileName() + " successfully.");
	}
	
	/**
	 * 解析Hadoop程序计算出来的CDG文件，其中第一行为对应的ECG文件名，第二行为指标参数{Para_fft，Para_lya，Para_all}, 最后的三行为CDG的三维数组数据。
	 * @param outputDir hadoop输出文件夹
	 * @return 返回相应信息类
	 * @throws IOException
	 */
	private ResponseMessage storeCDG(String outputDir) throws IOException {
		List<File> cdgFiles = listHadoopOutputFile(outputDir);
		if (cdgFiles.isEmpty()) {
			return new ResponseMessage(ErrorCode.GENERATE_CDG_ERROR, "Hadoop generate CDG error.");
		}
		
		for (File file : cdgFiles) {
			if (file.getName().equals(HADOOP_SUCCESS)) {
				continue;
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));
			//文件名为住院号_测试号{.txt}的形式，利用正则表达式以 _和. 为分隔符，分割文件名信息
			String[] fileNameStrings = reader.readLine().trim().split("[_\\.]");
			String[] params = reader.readLine().trim().split("[\\s+]");
			
			//将三维的CDG数据转成可被javascript解析的json字符串
			String line;
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			while ((line = reader.readLine()) != null) {
				builder.append("[");
				builder.append(line.trim().replaceAll("\\s+", ","));
				builder.append("],");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append("]");
			
			String testId = fileNameStrings[1];
			String cdgData = builder.toString();
			double paraFft = Double.parseDouble(params[0]);
			double paraLya = Double.parseDouble(params[1]);
			double paraAll = Double.parseDouble(params[2]);
			String cdgResults = diagnosis(paraFft, paraLya, paraAll);
			CDG cdg = new CDG(testId, cdgData, cdgResults, paraFft, paraLya);
			cdgDAO.insert(cdg);
			
			reader.close();
		}
		
		return new ResponseMessage(ErrorCode.SUCCESS, "Generated and stored the CDG.");
	}
	
	private List<File> listHadoopOutputFile(String dirPath) {
		File directory = new File(dirPath);
		List<File> files = new ArrayList<File>();
		boolean isSuccess = false;
		
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				if (file.getName().equals(HADOOP_SUCCESS)) {
					isSuccess = true;
				}
				files.add(file);
			}
		}
		else {
			LOGGER.error("List files from {} error.", dirPath);
		}
		if (!isSuccess) {
			LOGGER.error("Calculate CDG through Hadoop error.");
		}
		
		return files;
	}
	
	/**
	 * 根据CDG得出来的指标参数得出诊断结果
	 * @param paraFft 量化指标
	 * @param paraLya 量化指标
	 * @param paraAll 根据上面两个得出的判断指标
	 * @return
	 */
	private String diagnosis(double paraFft, double paraLya, double paraAll) {
		double LeftToCenter = (-1) * (9 * 0 - 5000 * 0.4 + 459) / Math.sqrt(9 * 9 + 5000 * 5000);
		double DistantOfTwoLine = (-1) * (9 * 198 - 5000 * 0.46 + 459) / Math.sqrt(9 * 9 + 5000 * 5000);
		double Condition = DistantOfTwoLine * 10 / LeftToCenter;
		if (paraAll > Condition) {
			return "阳性";
		}
		else if (paraAll < ((-1) * Condition)) {
			return "阴性";
		}
		else {
			return "可疑阳性";
		}
	}

}
