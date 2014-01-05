package com.proteanplatform.web.core.config;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.proteanplatform.web.*.domain")
@EnableTransactionManagement
@ComponentScan({"com.proteanplatform.web.*.config","com.proteanplatform.web.*.bean"})
public class CoreConfig implements Ordered {

	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() throws Exception {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		JdbcPropertiesFactoryBean factory = new JdbcPropertiesFactoryBean();
		configurer.setProperties(factory.getProperties());
	    return configurer;	
	}
	
	@Value("${datasource.driver}")
	String driver;
	
	@Value("${datasource.username}")
	String username;
	
	@Value("${datasource.password}")
	String password;
	
	@Value("${datasource.url}")
	String url;

	@Value("${hibernate.dialect}")
	String hibernateDialect;

	@Value("${hibernate.auto}")
	String hibernateAuto;

	@Autowired
	Properties properties;
	
	@Bean 
	public DataSource dataSource() throws IOException {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		
		ds.setDriverClassName(driver);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(url);
		
		return ds;
	}
	

	@Bean
	public EntityManagerFactory entityManagerFactory() throws IOException {
	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setGenerateDdl(true);

	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setPackagesToScan("com.quasar");
	    factory.setDataSource(dataSource());
	    
	    Properties jpaProperties = new Properties();
	    jpaProperties.setProperty("hibernate.hbm2ddl.auto", hibernateAuto);
	    jpaProperties.setProperty("hibernate.dialect", hibernateDialect);
	    
		factory.setJpaProperties(jpaProperties );
	    
	    factory.afterPropertiesSet();

	    return factory.getObject();
	}

	@Bean 
	public PlatformTransactionManager transactionManager() throws IOException {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}
	
	@Bean 
    public HibernateExceptionTranslator hibernateExceptionTranslator(){ 
      return new HibernateExceptionTranslator(); 
    }

	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}


	/* (non-Javadoc)
	 * @see org.springframework.core.Ordered#getOrder()
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
