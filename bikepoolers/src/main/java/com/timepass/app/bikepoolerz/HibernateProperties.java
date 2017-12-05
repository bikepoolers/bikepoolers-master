package com.timepass.app.bikepoolerz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/application.properties")
@ConfigurationProperties(prefix = "hibernate")

public class HibernateProperties {

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	public C3p0 getC3p0() {
		return c3p0;
	}

	public void setC3p0(C3p0 c3p0) {
		this.c3p0 = c3p0;
	}

	Map<String, Object> hibernateProperties = new HashMap<String, Object>();
	private String dialect;
	private Connection connection;
	private C3p0 c3p0; 
	private String current_session_context_class;
	
	public String getCurrent_session_context_class() {
		return current_session_context_class;
	}

	public void setCurrent_session_context_class(
			String current_session_context_class) {
		this.current_session_context_class = current_session_context_class;
	}

	public String getDialect() {
		return dialect;
	}

	public Map<String, Object> getHibernateProperties() {
		if(null == hibernateProperties || 0 == hibernateProperties.size() ){
			hibernateProperties.put("hibernate.connection.driver_class", connection.getDriver_class());
			hibernateProperties.put("hibernate.connection.url", connection.getUrl());
			hibernateProperties.put("hibernate.connection.username", connection.getUsername());
			hibernateProperties.put("hibernate.connection.password", connection.getPassword());
			hibernateProperties.put("hibernate.c3p0.min_size", c3p0.getMin_size());
			hibernateProperties.put("hibernate.c3p0.max_size", c3p0.getMax_size() );
			hibernateProperties.put("hibernate.c3p0.timeout", c3p0.getTimeout());
			hibernateProperties.put("hibernate.c3p0.max_statements", c3p0.getMax_statements());
			hibernateProperties.put("hibernate.dialect", dialect);
			hibernateProperties.put("hibernate.current_session_context_class", current_session_context_class);
		}
		return hibernateProperties;
	}

	public void setHibernateProperties(Map<String, Object> hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public static class Connection{
		private String driver_class;
		private String url;
		private String username;
		private String password;
		public String getDriver_class() {
			return driver_class;
		}
		public void setDriver_class(String driver_class) {
			this.driver_class = driver_class;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

	}
	
	public static class C3p0{
		private String min_size;
		private String max_size;
		private String timeout;
		private String max_statements;
		public String getMin_size() {
			return min_size;
		}
		public void setMin_size(String min_size) {
			this.min_size = min_size;
		}
		public String getMax_size() {
			return max_size;
		}
		public void setMax_size(String max_size) {
			this.max_size = max_size;
		}
		public String getTimeout() {
			return timeout;
		}
		public void setTimeout(String timeout) {
			this.timeout = timeout;
		}
		public String getMax_statements() {
			return max_statements;
		}
		public void setMax_statements(String max_statements) {
			this.max_statements = max_statements;
		}
		
	}
	

}
