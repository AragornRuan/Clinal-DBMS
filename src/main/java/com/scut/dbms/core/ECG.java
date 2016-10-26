package com.scut.dbms.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ecg")
public class ECG {
	@Id
	@Column(name = "testid", nullable = false)
	private String testId;
    
	@Column(name = "patient_id", nullable = false)
	private int patientId;
	
	@Column(name = "ecg_data")
	private String ecgData;
	
	@Column(name = "source")
	private String source;
	
	public ECG() {
		
	}
	
	public ECG(String testId, int patientId, String ecgData, String source) {
		this.testId = testId;
		this.patientId = patientId;
		this.ecgData = ecgData;
		this.source = source;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getEcgData() {
		return ecgData;
	}

	public void setEcgData(String ecgData) {
		this.ecgData = ecgData;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
