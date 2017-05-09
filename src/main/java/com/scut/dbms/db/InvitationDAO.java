package com.scut.dbms.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import com.scut.dbms.core.Invitation;
import com.scut.dbms.mapper.InvitationMapper;

@RegisterMapper(InvitationMapper.class)
public interface InvitationDAO {
	
	@SqlQuery("select id, code from invitation")
	List<Invitation> findAll();
	
	@SqlQuery("select id, code from invitation where code = :code")
	Invitation findByCode(@Bind("code") String code); 
	
	@SqlUpdate("insert into invitation(code) values (:code)")
	void insert(@BindBean Invitation invitation);

}
