package com.proteanplatform.web.core.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proteanplatform.web.core.domain.AbstractEntity;

@Entity
public class Client extends AbstractEntity {
	
	@Column(length=60)
	private String password;
	
	@Column(length=128,unique=true)
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
