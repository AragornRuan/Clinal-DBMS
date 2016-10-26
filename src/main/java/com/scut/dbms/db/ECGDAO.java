package com.scut.dbms.db;

import com.scut.dbms.core.ECG;
import com.scut.dbms.mapper.ECGMapper;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(ECGMapper.class)
public interface ECGDAO {
	
	@SqlQuery("select testid, patient_id, ecg_data, source from ecg "
			+ "where testid = :testId")
	ECG findByTestId(@Bind("testId") String testId);
	
	@SqlQuery("select ecg_data from ecg where testid = :testId")
	String findECGData(@Bind("testId") String testId);
	
	@SqlUpdate("insert into ecg (testid, patient_id, ecg_data, source) values "
			+ "(:testId, :patientId, :ecgData, :source)")
	void insert(@BindBean ECG ecg);
}
