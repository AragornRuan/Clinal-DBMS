package com.scut.dbms.db;

import com.scut.dbms.core.PatientsInfo;
import com.scut.dbms.mapper.PatientsInfoMapper;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PatientsInfoMapper.class)
public interface PatientsInfoDAO {
	@SqlQuery("call patientInfoQuery(:name, :male, :female, :admissionnumber, :ecgNormal, "
			+ ":ecgUnusual, :ctNormal, :ctStricture, :ctNothing, :radiographyNormal, "
			+ ":radiographyStricture, :radiographyNoting, :negative, :positive, :probablePositive, "
			+ ":times, :mindray, :aika, :cardis, :im, :ar)")
	List<PatientsInfo> queryPatientsInfo(@Bind("name") String name, @Bind("male") int male, @Bind("female") int female,
			@Bind("admissionnumber") String addmissionnumber, @Bind("ecgNormal") int ecgNormal, 
			@Bind("ecgUnusual") int ecgUnusual, @Bind("ctNormal") int ctNormal, @Bind("ctStricture") int ctStricture, 
			@Bind("ctNothing") int ctNothing, @Bind("radiographyNormal") int radiographyNormal, 
			@Bind("radiographyStricture") int radiographyStricture, @Bind("radiographyNoting") int radiographyNoting, 
			@Bind("negative") int negative, @Bind("positive") int positive, @Bind("probablePositive") int probablePositive,
			@Bind("times") int times, @Bind("mindray") int mindray, @Bind("aika") int aika, @Bind("cardis") int cardis, 
			@Bind("im") int im, @Bind("ar") int ar);
}

