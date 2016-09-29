package com.scut.dbms.core;

public class CDGInfo {
	
	private String name;
	
	private String sex;
	
	private int age;
	
	private String admissionnumber;
	
	private String testId;
	
	private String cdgResults;
	
	public CDGInfo() {
		
	}
	
	public CDGInfo(String name, String sex, int age, String admissionnumber, 
			String testId, String cdgResults) {
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.admissionnumber = admissionnumber;
		this.testId = testId;
		this.cdgResults = cdgResults;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAdmissionnumber() {
		return admissionnumber;
	}

	public void setAdmissionnumber(String admissionnumber) {
		this.admissionnumber = admissionnumber;
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
	

}
