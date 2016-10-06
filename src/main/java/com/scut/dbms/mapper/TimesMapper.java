package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.Times;

public class TimesMapper implements ResultSetMapper<Times> {
	public Times map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new Times(resultSet.getInt("id"), resultSet.getInt("patient_id"), resultSet.getInt("times"));
	}
}
