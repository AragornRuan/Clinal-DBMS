package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.Invitation;

public class InvitationMapper implements ResultSetMapper<Invitation> {

	public Invitation map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new Invitation(resultSet.getInt("id"), resultSet.getString("code"));
	}

}
