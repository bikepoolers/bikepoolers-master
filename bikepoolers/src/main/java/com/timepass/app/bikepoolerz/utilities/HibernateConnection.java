package com.timepass.app.bikepoolerz.utilities;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import com.timepass.app.bikepoolerz.HibernateProperties;
import com.timepass.app.bikepoolerz.entities.Category;

@ComponentScan
public class HibernateConnection {
	

	@Autowired
	private HibernateProperties hibernateProperties;
	
	Properties props = new Properties();
	private Session session;
	private SessionFactory sessionFactory;
	
	public HibernateProperties getHibernateProperties() {
		return hibernateProperties;
	}

	public void setHibernateProperties(HibernateProperties hibernateProperties) {
		this.hibernateProperties = hibernateProperties;
	}
	
	public SessionFactory doConnection(){
		props.putAll(hibernateProperties.getHibernateProperties());
		sessionFactory = new Configuration().addProperties(props)
		   .addPackage("com.learn.springboot.entities")
				   .addAnnotatedClass(Category.class)
				   .buildSessionFactory();
		return sessionFactory;
	}
}
