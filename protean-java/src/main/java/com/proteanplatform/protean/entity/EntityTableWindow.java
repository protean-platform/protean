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

import java.util.Map;

import javax.persistence.metamodel.EntityType;

import com.proteanplatform.protean.element.Table;
import com.proteanplatform.protean.element.Window;

/**
 * @author Austin Miller
 *
 */
public class EntityTableWindow extends Window {

	private EntityType<?> entity;

	Table table;
	
	/**
	 * @param entity
	 */
	public EntityTableWindow(EntityType<?> entity) {
		this.entity = entity;
		setTitle(entity.getName());
	}
	
	public Table getBody() {
		table = user.constructEntityTable(entity);
		user.register(table);
		return table;
	}
	
	public void create() {
		user.load(new EntityCreateWindow(entity));
	}
	
	public void edit(Map<String, Object> args) {
		
	}
	
	public void delete(Map<String, Object> args) {
		
	}
	
	public void close() {
		user.destroy(table);
		user.destroy(this);
	}


}
