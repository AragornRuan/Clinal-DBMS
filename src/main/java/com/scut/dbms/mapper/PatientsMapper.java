package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.Patients;

public class PatientsMapper implements ResultSetMapper<Patients>{

	public Patients map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		
		return new Patients(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("sex"), 
				resultSet.getInt("age"), resultSet.getString("admissionnumber"));
	}

}
