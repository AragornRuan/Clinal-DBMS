package com.scut.dbms.core;

public class PatientsInfo {
	private String name;
	
	private String sex;
	
	private int age;
	
	private String admissionnumber;
	
	private String hosTime;
	
	private String source;
	
	public PatientsInfo() {
		
	}
	
	public PatientsInfo(String name, String sex, int age, String admissionnumber,
			String hosTime, String source) {
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.admissionnumber = admissionnumber;
		this.hosTime = hosTime;
		this.source = source;
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

	public String getHosTime() {
		return hosTime;
	}

	public void setHosTime(String hosTime) {
		this.hosTime = hosTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
