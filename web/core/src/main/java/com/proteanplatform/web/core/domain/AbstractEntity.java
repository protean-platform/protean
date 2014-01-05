package com.proteanplatform.web.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.proteanplatform.protean.Protean;

@MappedSuperclass
@Protean(columns={"id","externalId","createdDate","updatedDate"})
public abstract class AbstractEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Protean(enabled=false)
	private Long id;

	@Column(name="external_id",unique = true,length=128,nullable=true)
	private String externalId;
	
	@Column(name="created_date", columnDefinition="TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP",nullable=false,updatable=false)
	@Protean(enabled=false)
	private Date createdDate = null;
	
	@Column(name="updated_date", columnDefinition="TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP",nullable=false)
	@Protean(enabled=false)
	private Date updatedDate = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	@PrePersist
	private void prePersist() {
		setCreatedDate(new Date());
		setUpdatedDate(new Date());
	}
	
	@PreUpdate
	private void preUpdate() {
		updatedDate = new Date();
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
