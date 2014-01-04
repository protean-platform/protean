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
package com.proteanplatform.protean.entity;

import javax.persistence.metamodel.EntityType;

import com.proteanplatform.protean.element.Window;


/**
 * @author Austin Miller
 *
 */
public class EntityCreateWindow extends Window<EntityUser> {

	private EntityType<?> entity;
	private EntityForm form;

	/**
	 * @param entity
	 */
	public EntityCreateWindow(EntityType<?> entity) {
		this.entity = entity;
		this.setTitle("New "+entity.getName());
		this.setResizable(false);
	}


	public Object getBody() {
		if(form == null){
			form = EntityServer.getInstance().constructCreateEntityForm(entity);
			user.register(form);
		}
		return form;
	}


	public void close() {
		user.destroy(form);
		user.destroy(this);
	}
	
	public void ok() {
		
	}
	
	public void cancel() {
		close();
	}

}
