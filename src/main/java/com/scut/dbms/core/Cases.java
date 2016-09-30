package com.scut.dbms.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 对应于数据库中的cases表
 * 
 * @author aragorn
 *
 */
@Entity
@Table(name = "cases")
public class Cases {
	/**
	 * primary key
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "patient_id", nullable = false)
	private int patientId;

	@Column(name = "diagnosis")
	private String diagnosis;

	@Column(name = "ecg")
	private String ecg;

	@Column(name = "ecg_tag")
	private int ecgTag;

	@Column(name = "ct")
	private String ct;

	@Column(name = "ct_tag")
	private int ctTag;

	@Column(name = "complaint")
	private String complaint;

	@Column(name = "radiography")
	private String radiography;

	@Column(name = "radiography_tag")
	private int radiographyTag;

	@Column(name = "hos_time")
	private String hosTime;

	@Column(name = "radiography_time")
	private String radiographyTime;

	@Column(name = "ct_time")
	private String ctTime;

	@Column(name = "discharged_time")
	private String dischargedTime;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "disease")
	private int disease;

	public Cases() {
		
	}
	
	public Cases(int patientId, String diagnosis, String ecg, int ecgTag, String ct, int ctTag, String complaint,
			String radiography, int radiographyTag, String hosTime, String radiographyTime, String ctTime,
			String dischargedTime, String remarks, int disease) {
		this.patientId = patientId;
		this.diagnosis = diagnosis;
		this.ecg = ecg;
		this.ecgTag = ecgTag;
		this.ct = ct;
		this.ctTag = ctTag;
		this.complaint = complaint;
		this.radiography = radiography;
		this.radiographyTag = radiographyTag;
		this.hosTime = hosTime;
		this.radiographyTime = radiographyTime;
		this.ctTime = ctTime;
		this.dischargedTime = dischargedTime;
		this.remarks = remarks;
		this.disease = disease;
	}

	public Cases(int id, int patientId, String diagnosis, String ecg, int ecgTag, String ct, int ctTag,
			String complaint, String radiography, int radiographyTag, String hosTime, String radiographyTime,
			String ctTime, String dischargedTime, String remarks, int disease) {
		this.id = id;
		this.patientId = patientId;
		this.diagnosis = diagnosis;
		this.ecg = ecg;
		this.ecgTag = ecgTag;
		this.ct = ct;
		this.ctTag = ctTag;
		this.complaint = complaint;
		this.radiography = radiography;
		this.radiographyTag = radiographyTag;
		this.hosTime = hosTime;
		this.radiographyTime = radiographyTime;
		this.ctTime = ctTime;
		this.dischargedTime = dischargedTime;
		this.remarks = remarks;
		this.disease = disease;
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

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getEcg() {
		return ecg;
	}

	public void setEcg(String ecg) {
		this.ecg = ecg;
	}

	public int getEcgTag() {
		return ecgTag;
	}

	public void setEcgTag(int ecgTag) {
		this.ecgTag = ecgTag;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public int getCtTag() {
		return ctTag;
	}

	public void setCtTag(int ctTag) {
		this.ctTag = ctTag;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public String getRadiography() {
		return radiography;
	}

	public void setRadiography(String radiography) {
		this.radiography = radiography;
	}

	public int getRadiographyTag() {
		return radiographyTag;
	}

	public void setRadiographyTag(int radiographyTag) {
		this.radiographyTag = radiographyTag;
	}

	public String getHosTime() {
		return hosTime;
	}

	public void setHosTime(String hosTime) {
		this.hosTime = hosTime;
	}

	public String getRadiographyTime() {
		return radiographyTime;
	}

	public void setRadiographyTime(String radiographyTime) {
		this.radiographyTime = radiographyTime;
	}

	public String getCtTime() {
		return ctTime;
	}

	public void setCtTime(String ctTime) {
		this.ctTime = ctTime;
	}

	public String getDischargedTime() {
		return dischargedTime;
	}

	public void setDischargedTime(String dischargedTime) {
		this.dischargedTime = dischargedTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getDisease() {
		return disease;
	}

	public void setDisease(int disease) {
		this.disease = disease;
	}

}
