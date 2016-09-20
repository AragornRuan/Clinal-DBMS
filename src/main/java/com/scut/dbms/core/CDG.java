package com.scut.dbms.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cdg")
public class CDG {
	@Id
	@Column(name = "testid")
	private String testId;
	
	@Column(name = "cdg_data")
	private String cdgData;
	
	@Column(name = "cdg_results")
	private String cdgResults;
	
	@Column(name = "para_fft")
	private double paraFft;
	
	@Column(name = "para_lya")
	private double paraLya;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getCdgData() {
		return cdgData;
	}

	public void setCdgData(String cdgData) {
		this.cdgData = cdgData;
	}

	public String getCdgResults() {
		return cdgResults;
	}

	public void setCdgResults(String cdgResults) {
		this.cdgResults = cdgResults;
	}

	public double getParaFft() {
		return paraFft;
	}

	public void setParaFft(double paraFft) {
		this.paraFft = paraFft;
	}

	public double getParaLya() {
		return paraLya;
	}

	public void setParaLya(double paraLya) {
		this.paraLya = paraLya;
	}
}
