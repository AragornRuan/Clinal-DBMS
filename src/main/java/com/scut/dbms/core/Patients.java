package com.scut.dbms.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "patients")
public class Patients {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "sex", nullable = false)
	private String sex;
	
	@Column(name = "age", nullable = false)
	private int age;
	
	@Column(name = "admissionnumber", nullable = false)
	private String admissionnumber;
	
	public Patients() {
		
	}
	
	public Patients(String name, String sex, int age, String admissionnumber) {
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.admissionnumber = admissionnumber;
	}
	
	public Patients(int id, String name, String sex, int age, String admissionnumber) {
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.admissionnumber = admissionnumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

}
