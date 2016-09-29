package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.Cases;

public class CasesMapper implements ResultSetMapper<Cases> {

	public Cases map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new Cases(resultSet.getInt("id"), resultSet.getInt("patient_id"), resultSet.getString("diagnosis"),
				resultSet.getString("ecg"), resultSet.getInt("ecg_tag"), resultSet.getString("ct"), 
				resultSet.getInt("ct_tag"), resultSet.getString("complaint"), resultSet.getString("radiography"),
				resultSet.getInt("radiography_tag"), resultSet.getString("hos_time"), resultSet.getString("radiography_time"),
				resultSet.getString("ct_time"), resultSet.getString("discharged_time"), resultSet.getString("remarks"),
				resultSet.getInt("disease"));
	}
}
