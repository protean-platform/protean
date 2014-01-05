package com.proteanplatform.web.core.config;

import java.security.SecureRandom;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.passwordEncoder(passwordEncoder())
			.usersByUsernameQuery("SELECT email,password,true FROM client WHERE email = ?")
			.authoritiesByUsernameQuery("SELECT ?,'user'")
			
			;
		
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
        http
	        .authorizeRequests()
	            .anyRequest().permitAll()
	            .and()
	        .formLogin()
	        	.loginPage("/login")
	        	.failureUrl("/login?error")
	        	.defaultSuccessUrl("/client")
	        	.permitAll()
	        	.and()
	        .httpBasic().and()
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/");
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12,new SecureRandom());
		return passwordEncoder;
	}

}
