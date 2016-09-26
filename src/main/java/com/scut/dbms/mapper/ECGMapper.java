package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.ECG;

public class ECGMapper implements ResultSetMapper<ECG>{

	public ECG map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new ECG(resultSet.getString("testid"), resultSet.getInt("patient_id"), 
				resultSet.getString("ecg_data"), resultSet.getString("source"));
	}
	
}
