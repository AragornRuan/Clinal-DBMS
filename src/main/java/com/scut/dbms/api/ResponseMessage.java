package com.scut.dbms.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessage {
	private String contents;
	
	private int errorCode;
	
	public ResponseMessage() {
		
	}
	
	public ResponseMessage(int errorCode, String contents) {
		this.errorCode = errorCode;
		this.contents = contents;
	}
	
	@JsonProperty
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@JsonProperty
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
