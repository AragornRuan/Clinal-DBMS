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
	@SqlQuery("select id, patients_id, diagnosis, ecg, ecg_tag, ct, ct_tag, "
			+ "complaint, radiography, radiography_tag hos_time, radiography_time, "
			+ "ct_time, discharged_time, remarks, disease from cases where patients_id "
			+ "= :patientsId")
	Cases findByPatientsId(@Bind("patientsId") int patientsId);
	
	@SqlUpdate("insert into cases(patients_id, diagnosis, ecg, ecg_tag, ct, ct_tag, "
			+ "complaint, radiography, radiography_tag hos_time, radiography_time, "
			+ "ct_time, discharged_time, remarks, disease) values (:patientsId, :diagnosis, "
			+ ":ecg, :ecgTag, :et, :ctTag, :complaint, :radiography, :radiographyTag, :hosTime,"
			+ ":radiographyTime, :ctTime, :dischargedTime, :remarks, :disease)")
	void insert(@BindBean Cases cases);
	
	@SqlQuery("select id, patients_id, diagnosis, ecg, ecg_tag, ct, ct_tag, "
			+ "complaint, radiography, radiography_tag hos_time, radiography_time, "
			+ "ct_time, discharged_time, remarks, disease from cases")
	List<Cases> findAll();
	
}
