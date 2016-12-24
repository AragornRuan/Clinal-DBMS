package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.Diagnosis;

public class DiagnosisMapper implements ResultSetMapper<Diagnosis>{

	public Diagnosis map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new Diagnosis(resultSet.getString("testid"), resultSet.getString("cdg_results"), 
				resultSet.getInt("ecg_tag"));
	}

}
