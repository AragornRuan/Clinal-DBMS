package com.scut.dbms.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessage {
	
	private int code;
	
	private String message;
	
//	private int id;
	
	public ResponseMessage() {
		
	}
	
	public ResponseMessage(int code, String message/*, int id*/) {
		this.code = code;
		this.message = message;
//		this.id = id;
	}
	
	@JsonProperty
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
/*	@JsonProperty
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}*/
}
