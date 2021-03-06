package com.scut.dbms.db;

import com.scut.dbms.core.Cases;
import com.scut.dbms.mapper.CasesMapper;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(CasesMapper.class)
public interface CasesDAO {
	@SqlQuery("select id, patient_id, diagnosis, ecg, ecg_tag, ct, ct_tag, "
			+ "complaint, radiography, radiography_tag, hos_time, radiography_time, "
			+ "ct_time, discharged_time, remarks, disease from cases where patient_id "
			+ "= :patientId")
	Cases findByPatientsId(@Bind("patientId") int patientId);
	
	@SqlUpdate("insert into cases(patient_id, diagnosis, ecg, ecg_tag, ct, ct_tag, "
			+ "complaint, radiography, radiography_tag, hos_time, radiography_time, "
			+ "ct_time, discharged_time, remarks, disease) values (:patientId, :diagnosis, "
			+ ":ecg, :ecgTag, :ct, :ctTag, :complaint, :radiography, :radiographyTag, :hosTime,"
			+ ":radiographyTime, :ctTime, :dischargedTime, :remarks, :disease)")
	void insert(@BindBean Cases cases);
	
	@SqlQuery("select id, patient_id, diagnosis, ecg, ecg_tag, ct, ct_tag, "
			+ "complaint, radiography, radiography_tag, hos_time, radiography_time, "
			+ "ct_time, discharged_time, remarks, disease from cases")
	List<Cases> findAll();
	
	@SqlUpdate("update cases set patient_id = :patientId, diagnosis = :diagnosis, ecg = :ecg, "
			+ "ecg_tag = :ecgTag, ct = :ct, ct_tag = :ctTag, complaint = :complaint, "
			+ "radiography = :radiography, radiography_tag = :radiographyTag, hos_time = :hosTime, "
			+ "radiography_time = :radiographyTime, ct_time = :ctTime, discharged_time = :dischargedTime, "
			+ "remarks = :remarks, disease = :disease where id = :id")

	void update(@BindBean Cases cases , @Bind("id") int id);
}
