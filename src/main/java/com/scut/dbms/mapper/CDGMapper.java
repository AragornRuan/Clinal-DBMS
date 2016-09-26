package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.CDG;

public class CDGMapper implements ResultSetMapper<CDG> {

	public CDG map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new CDG(resultSet.getString("testid"), resultSet.getString("cdg_data"), resultSet.getString("cdg_results"),
				resultSet.getDouble("para_fft"), resultSet.getDouble("para_lya"));
	}

}
