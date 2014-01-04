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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proteanplatform.protean.Protean;
import com.proteanplatform.protean.ProTeaServer;

/**
 * @author Austin Miller
 *
 */
public class EntityServer extends ProTeaServer<EntityUser> {
	
	private static EntityServer instance;
	
	public static EntityServer getInstance() {
		return instance;
	}
	private Logger logger = LoggerFactory.getLogger(EntityServer.class);
	
	
	private Map<String,EntityType<?>> entities = new HashMap<String,EntityType<?>>();
	
	private Map<EntityType<?>,EntityTableMetadata> entityMetadata= new HashMap<EntityType<?>,EntityTableMetadata>();

	private EntityManager entityManager;
	
	public Map<String,EntityType<?>> getEntities() {
		return entities;
	}
	
	@Override
	protected Class<EntityUser> setProTeaUserClass() {
		return EntityUser.class;
	}

	@Override
	protected int setPort() {
		return 8090;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	protected void onNewUser(EntityUser user) {
		logger.info("New user {} ",user.getId());
		user.sendServerTime();
		user.load(new EntityGoBar(entities));
	}

	@Override
	protected void onCloseConnection(EntityUser user) {
		logger.info("Connection closed for user {} ",user.getId());
	}

	public void start(EntityManager entityManager) {
		logger.debug("Starting admin server");
		instance = this;
		
		this.entityManager = entityManager; 
		
		for(EntityType<?> entity : entityManager.getMetamodel().getEntities()) {
			if(entity.getJavaType().getAnnotation(Protean.class) == null) {
				continue;
			}
			
			entities.put(entity.getName(),entity);
		}
		
		super.run();
		logger.info("Started protean server");
		
	}
	
	public void stop() {
		super.stop();
		logger.info("Stopped protean server");
	}
	
	public EntityForm constructCreateEntityForm(EntityType<?> entity) {
		EntityForm form = new EntityForm();
		EntityTableMetadata metadata = getMetadata(entity);
		
		form.setElements(metadata.getElements());
		return form;
		
	}
	
	@SuppressWarnings("rawtypes")
	public EntityTable constructEntityTable(EntityType<?> entity) {
		EntityTable table = new EntityTable();

		try {
			EntityTableMetadata metadata = getMetadata(entity);
			
			table.setEntity(entity);
	
			Query query = entityManager.createQuery("SELECT e FROM "+entity.getName()+" e");
			List list = query.getResultList();
			
			for(EntityTableCellMetadata cell : metadata.getCells()) {
				table.getColumns().add(cell.getColumn());
			}
			
			for(Object object : list) {
				table.addRow(metadata.toRow(object));
			}
		} catch(Exception e) {
	    	// if we get here, the table will look funny on the front end, but oh well.
	    	logger.error(e.getMessage(),e);
		}
		return table;
	}

	/**
	 * @param entity
	 * @return
	 * @throws NoSuchMethodException 
	 */
	private EntityTableMetadata getMetadata(EntityType<?> entity) {
		
		if(entityMetadata.containsKey(entity) == false) {
			entityMetadata.put(entity, new EntityTableMetadata(entity));
		}
		
		return entityMetadata.get(entity);
	}
	


	


}
