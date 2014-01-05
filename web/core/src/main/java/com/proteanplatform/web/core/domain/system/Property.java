package com.proteanplatform.web.core.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

import com.proteanplatform.protean.Protean;


@Entity
@Protean(columns={"key","value"})
public class Property {

	@Index(name="property_pkey")
	@Id
	@Column(unique=true,length=512)
	private	String key;
	
	@Column(length=512)
	private	String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
