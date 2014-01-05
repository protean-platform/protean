/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.proteanplatform.protean.element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proteanplatform.protean.ElementMetadata;
import com.proteanplatform.protean.ProteanUser;

/**
 * @author Austin Miller
 *
 */
public abstract class AbstractElement {
	
	

	public abstract String getCommand();
	private long id = Long.MIN_VALUE;
	
	protected ProteanUser user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonIgnore
	public ProteanUser getUser() {
		return user;
	}

	public void setUser(ProteanUser user) {
		this.user = user;
	}

	public ElementMetadata getMetadata() throws Exception {
		return ElementMetadata.getMetadata(this.getClass());
	}
	
}
