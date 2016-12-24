package com.scut.dbms.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.scut.dbms.core.Diagnosis;
import com.scut.dbms.mapper.DiagnosisMapper;

@RegisterMapper(DiagnosisMapper.class)
public interface DiagnosisDAO {
	@SqlQuery("call diagnosisQueryInfo(:admissionnumber)")
	List<Diagnosis> queryDiagnosis(@Bind("admissionnumber") String admissionnumber);
}
