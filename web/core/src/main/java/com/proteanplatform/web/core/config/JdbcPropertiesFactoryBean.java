package com.proteanplatform.web.core.config;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class JdbcPropertiesFactoryBean {

	private Properties getDatabaseProperties() throws Exception {
		Properties props = new Properties();
		try {
			props .load(Thread.currentThread().getContextClassLoader().getResourceAsStream("/datasource.properties"));
		} catch(Exception e) {
			// this is for unit tests, we still want to throw an exception if we can't find either file...
			props.load(new FileInputStream("src/main/resources/datasource.properties"));
			Class.forName("org.postgresql.Driver");
		}
		return props;
	}
	
	public Properties getProperties() throws Exception {

		Properties props = new Properties();
		
		
		Connection connection = null;
		try {
			props = getDatabaseProperties();
			
			Class.forName(props.getProperty("datasource.driver"));
			
			String url = props.getProperty("datasource.url");
			String username = props.getProperty("datasource.username");
			String password = props.getProperty("datasource.password");
			
			connection = DriverManager.getConnection(url,username,password);
			
			PreparedStatement stm = connection.prepareStatement("select key,value from property");
			stm.execute();
			
			ResultSet rs = stm.getResultSet();
			while(rs.next()) {
				props.put(rs.getString(1),rs.getString(2));
			}
			
		} finally {
			if(connection != null) {
				connection.close();
			}
		}
		return props;
	}
}