package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.CDGInfo;

public class CDGInfoMapper implements ResultSetMapper<CDGInfo> {

	public CDGInfo map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new CDGInfo(resultSet.getString("name"), resultSet.getString("sex"), resultSet.getInt("age"), 
				resultSet.getString("admissionnumber"), resultSet.getString("testid"), resultSet.getString("cdg_results"));
	}

}
