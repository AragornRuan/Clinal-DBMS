package com.scut.dbms.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessage {
	
	private int errorCode;
	
	private String contents;
	
//	private int id;
	
	public ResponseMessage() {
		
	}
	
	public ResponseMessage(int errorCode, String contents/*, int id*/) {
		this.errorCode = errorCode;
		this.contents = contents;
		//this.id = id;
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
	
	/*@JsonProperty
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}*/
}
