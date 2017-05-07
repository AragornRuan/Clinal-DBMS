package com.scut.dbms;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scut.dbms.auth.JwtCookieAuthConfiguration;

import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;



public class DBMSConfiguration extends Configuration {
	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();
	
	@Valid
	@NotNull
	private JwtCookieAuthConfiguration jwtCookieAuth = new JwtCookieAuthConfiguration();

	public JwtCookieAuthConfiguration getJwtCookieAuth() {
	  return jwtCookieAuth;
	}

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.database = dataSourceFactory;
	}
}
