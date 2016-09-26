package com.scut.dbms.db;

import com.scut.dbms.core.CDG;
import com.scut.dbms.mapper.CDGMapper;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(CDGMapper.class)
public interface CDGDAO {
	@SqlQuery("select testid, cdg_data, cdg_results, para_fft, para_lya from cdg "
			+ "where testid = :testId")
	CDG findByTestId(@Bind("testId") String testId);
	
	@SqlUpdate("insert into cdg (testid, cdg_data, cdg_results, para_fft, para_lya) values "
			+ "(:testId, :cdgData, :cdgResults, :paraFft, :paraLya)")
	void insert(@BindBean CDG cdg);
}
