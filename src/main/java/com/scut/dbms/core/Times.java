package com.scut.dbms.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "times")
public class Times {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "patient_id", nullable = false)
	private int patientId;
	
	@Column(name = "times")
	private int times;
	
	public Times() {
		
	}
	
	public Times(int id, int patientId, int times) {
		this.id = id;
		this.patientId = patientId;
		this.times = times;
	}
	
	public Times(int patientId, int times) {
		this.patientId = patientId;
		this.times = times;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
