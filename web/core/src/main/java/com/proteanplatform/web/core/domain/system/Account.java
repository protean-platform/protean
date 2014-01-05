package com.proteanplatform.web.core.domain.system;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proteanplatform.web.core.domain.AbstractEntity;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name="account_type", discriminatorType= DiscriminatorType.INTEGER)
public class Account extends AbstractEntity {
	
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
