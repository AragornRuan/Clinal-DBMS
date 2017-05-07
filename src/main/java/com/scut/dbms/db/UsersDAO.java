package com.scut.dbms.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.scut.dbms.core.Users;
import com.scut.dbms.mapper.UsersMapper;

@RegisterMapper(UsersMapper.class)
public interface UsersDAO {
	
	@SqlQuery("select id, username, password, realname, mobile, email from users")
	List<Users> findAll();
	
	@SqlQuery("select id, username, password, realname, mobile, email from users "
			+ "where username = :username")
	Users findByUsername(@Bind("username") String username);
	
	@SqlUpdate("insert into users(username, password, realname, mobile, email) "
			+ "values (:username, :password, :realname, :mobile, :email)")
	void insert(@BindBean Users users);
	
	@SqlUpdate("update users set username = :username, password = :password, realname = :realname, "
			+ "mobile = :mobile, email = :email where username = :username")
	void update(@BindBean Users users);

}
