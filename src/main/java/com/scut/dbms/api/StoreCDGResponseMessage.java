package com.scut.dbms.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreCDGResponseMessage extends ResponseMessage {
	private List<String> errorCDG;
	
	public StoreCDGResponseMessage(List<String> errorCDG,int code, String message) {
		super(code, message);
		this.errorCDG = errorCDG;
	}

	@JsonProperty
	public List<String> getErrorCDG() {
		return errorCDG;
	}

	public void setErrorCDG(List<String> errorCDG) {
		this.errorCDG = errorCDG;
	}
	
	
	
}
