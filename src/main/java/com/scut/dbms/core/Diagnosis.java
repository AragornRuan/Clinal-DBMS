package com.scut.dbms.core;

public class Diagnosis {
	private String testId;
	private String cdgResults;
	private int ecgTag;
	
	public Diagnosis() {
		
	}
	
	public Diagnosis(String testId, String cdgReaults, int ecgTag) {
		this.testId = testId;
		this.cdgResults = cdgReaults;
		this.ecgTag = ecgTag;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getCdgResults() {
		return cdgResults;
	}

	public void setCdgResults(String cdgResults) {
		this.cdgResults = cdgResults;
	}

	public int getEcgTag() {
		return ecgTag;
	}

	public void setEcgTag(int ecgTag) {
		this.ecgTag = ecgTag;
	}
}
