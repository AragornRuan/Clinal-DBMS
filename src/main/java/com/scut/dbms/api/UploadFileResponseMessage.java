package com.scut.dbms.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadFileResponseMessage extends ResponseMessage {
	private long threadId;
	
	public UploadFileResponseMessage(long threadId, int code, String message) {
		super(code, message);
		this.threadId = threadId;
	}

	@JsonProperty
	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
}
