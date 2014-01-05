package com.proteanplatform.web.core.domain.system;

import org.springframework.data.repository.CrudRepository;


public interface PropertyDao extends CrudRepository<Property, Long> {
	public <E extends Property> E findByKey(String key);
}