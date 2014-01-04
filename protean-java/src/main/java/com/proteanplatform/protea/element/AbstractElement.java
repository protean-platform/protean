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
package com.proteanplatform.protea.element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proteanplatform.protea.ElementMetadata;

/**
 * @author Austin Miller
 *
 */
public abstract class AbstractElement<T> {
	
	

	public abstract String getCommand();
	private long id = Long.MIN_VALUE;
	
	protected T user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonIgnore
	public T getUser() {
		return user;
	}

	@SuppressWarnings("unchecked")
	public void setUser(Object user) {
		this.user = (T) user;
	}

	public ElementMetadata getMetadata() throws Exception {
		return ElementMetadata.getMetadata(this.getClass());
	}
	
}
