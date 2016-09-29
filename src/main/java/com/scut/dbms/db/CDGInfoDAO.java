package com.scut.dbms.db;

import com.scut.dbms.core.CDGInfo;
import com.scut.dbms.mapper.CDGInfoMapper;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(CDGInfoMapper.class)
public interface CDGInfoDAO {
	
	@SqlQuery("call cdgInfoQuery(:patientId)")
	List<CDGInfo> queryCDGInfo(@Bind("patientId") int patientId);
}
