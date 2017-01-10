package com.scut.dbms.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOperations {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileOperations.class);

	public static String readFile(String filePath) {
		File file = new File(filePath);
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String tempContent;
			while ((tempContent = bufferedReader.readLine()) != null) {
				stringBuilder.append(tempContent);
				stringBuilder.append("\n");
			}
		} catch (Exception exception) {
			LOGGER.error("Read file {} error.", filePath);
			exception.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ioException) {
					LOGGER.error("Close bufferedReader error.");
					ioException.printStackTrace();
				}
			}
		}

		return stringBuilder.toString();
	}

	public static String loadECG(File file) {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String tempContent;
			while ((tempContent = bufferedReader.readLine()) != null) {
				stringBuilder.append(tempContent);
				stringBuilder.append(";");
			}
		} catch (Exception exception) {
			LOGGER.error("Read file {} error.", file.getName());
			exception.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ioException) {
					LOGGER.error("Close bufferedReader error.");
					ioException.printStackTrace();
				}
			}
		}

		return stringBuilder.toString();
	}

	public static List<File> listFile(String dirPath) {
		File directory = new File(dirPath);
		List<File> files = new ArrayList<File>();
		/*
		 * if (directory.isFile()) { files.add(directory); return files; } else
		 * if (directory.isDirectory()) { File[] fileArr =
		 * directory.listFiles(); for (int i = 0; i < fileArr.length; i++) {
		 * files.addAll(listFile(fileArr[i].getName())); } }
		 */
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				files.add(file);
			}
		} else {
			LOGGER.error("List files from {} error.", dirPath);
		}
		return files;
	}

	public static void saveFile(String filename, String content) {
		BufferedWriter bufferedWriter = null;
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(content);
			LOGGER.info("Write file {} successfully", filename);
		} catch (IOException ioException) {
			LOGGER.error("Write file {} error.", filename);
			ioException.printStackTrace();
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException ioException) {
					LOGGER.error("Close bufferedWriter error.");
					ioException.printStackTrace();
				}
			}
		}
	}

	public static void makeDir(String directory) {
		File dir = new File(directory);
		if (!dir.exists()) {
			if (dir.mkdir()) {
				LOGGER.info("Create directory: {}", directory);
			} else {
				LOGGER.error("Create directory {} error.", directory);
			}
		}
	}

	public static void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
		int read;
		final int BUFFER_LENGTH = 1024;
		final byte[] buffer = new byte[BUFFER_LENGTH];
		OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
		while ((read = uploadedInputStream.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		out.flush();
		out.close();
	}

	public static void unzip(String srcZipPath, String srcZipFile) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcZipPath + srcZipFile));
			ZipInputStream zis = new ZipInputStream(bis);

			BufferedOutputStream bos = null;

			// byte[] b = new byte[1024];
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				String entryName = entry.getName();
				bos = new BufferedOutputStream(new FileOutputStream(srcZipPath + entryName));
				int b = 0;
				while ((b = zis.read()) != -1) {
					bos.write(b);
				}
				bos.flush();
				bos.close();
			}
			zis.close();
			LOGGER.info("Unzip zip file {} successfully.", srcZipFile);
		} catch (IOException e) {
			LOGGER.error("Unzip zip file {} error: {}", srcZipFile, e.getMessage());
		}
	}

	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.delete()) {
				LOGGER.info("Delete file {} successfully.", filePath);
			}
			else {
				LOGGER.error("Delete file {} failed.", filePath);
			}
		}
		else {
			LOGGER.error("File {} is not exists.", filePath);
		}
	}
}
