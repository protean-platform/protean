package com.proteanplatform.web.core.domain.system;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="log_event")
public class LogEvent {
	
	@Id
	private long id;
	
	

}
