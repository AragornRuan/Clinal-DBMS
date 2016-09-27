package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.PatientsInfo;

public class PatientsInfoMapper implements ResultSetMapper<PatientsInfo> {

	public PatientsInfo map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new PatientsInfo(resultSet.getString("name"), resultSet.getString("sex"), resultSet.getInt("age"), 
				resultSet.getString("admissionnumber"), resultSet.getString("hos_time"), resultSet.getString("source"));
	}

}
