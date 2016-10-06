package com.scut.dbms.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.scut.dbms.core.Times;
import com.scut.dbms.mapper.TimesMapper;

@RegisterMapper(TimesMapper.class)
public interface TimesDAO {
	
	@SqlQuery("select id, patient_id, times from times")
	List<Times> findAll();
	
	@SqlQuery("select id, patient_id, times from times where patient_id = :patientId")
	Times findByPatientId(@Bind("patientId") int patientId);
	
	@SqlUpdate("insert into times (patient_id, times) values (:patientId, :times)")
	void insert(@BindBean Times times);
	
	@SqlUpdate("update times set times = :times where patient_id = :patientId")
	void update(@BindBean Times times);
}
