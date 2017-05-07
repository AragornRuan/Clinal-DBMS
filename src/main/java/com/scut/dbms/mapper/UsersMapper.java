package com.scut.dbms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.scut.dbms.core.Users;

public class UsersMapper implements ResultSetMapper<Users>{

	public Users map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		
		return new Users(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"),
				resultSet.getString("realname"), resultSet.getString("mobile"), resultSet.getString("email"));
	}
    
}
