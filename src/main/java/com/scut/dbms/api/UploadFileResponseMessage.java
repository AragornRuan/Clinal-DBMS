package com.scut.dbms.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadFileResponseMessage extends ResponseMessage {
	private long threadId;
	private List<String> errorECG;
	
	public UploadFileResponseMessage(long threadId, List<String> errorECG,int code, String message) {
		super(code, message);
		this.threadId = threadId;
		this.errorECG = errorECG;
	}

	@JsonProperty
	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	@JsonProperty
	public List<String> getErrorECG() {
		return errorECG;
	}

	public void setErrorECG(List<String> errorECG) {
		this.errorECG = errorECG;
	}
}
