package com.scut.dbms.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertResponseMessage extends ResponseMessage {

	private int id;
	
	public InsertResponseMessage(int id, int code, String message) {
		super(code, message);
		this.id = id;
	}

	@JsonProperty
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
