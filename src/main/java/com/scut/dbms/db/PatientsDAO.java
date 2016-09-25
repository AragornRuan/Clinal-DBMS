package com.scut.dbms.db;

import com.scut.dbms.core.Patients;
import com.scut.dbms.mapper.PatientsMapper;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PatientsMapper.class)
public interface PatientsDAO {
	@SqlUpdate("insert into patients (name, sex, age, admissionnumber) values "
			+ "(:name, :sex, :age, :admissionnumber)")
	void insert(@BindBean Patients patients);
	
	@SqlQuery("select id, name, sex, age, admissionnumber from patients where "
			+ "admissionnumber = :admissionnumber")
	Patients findByAdmissionnumber(@Bind("admissionnumber") String admissionnumber);
	
	@SqlQuery("select id, name, sex, age, admissionnumber from patients where "
			+ "id = :id")
	Patients findById(@Bind("id") int id);
	
	@SqlQuery("select id, name, sex, age, admissionnumber from patients")
	List<Patients> findAll();
	
	@SqlQuery("select id, name, sex, age, admissionnumber from patients where id > :min and id < :max")
	List<Patients> findById(@Bind("min") int min, @Bind("max") int max);
	
	@SqlUpdate("update patients set name = :name, sex = :sex, age = :age, admissionnumber = :admissionnumber "
			+ "where admissionnumber = :oldadnum")
	void update(@Bind("oldadnum") String oldadnum, @BindBean Patients patients);
	
	@SqlQuery("select id from patients where admissionnumber = :admissionnumber")
	int findId(@Bind("admissionnumber") String admissionnumber);
}
