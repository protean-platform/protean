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
package com.proteanplatform.protean;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proteanplatform.protean.element.AbstractElement;
import com.proteanplatform.protean.element.Background;
import com.proteanplatform.protean.element.Form;
import com.proteanplatform.protean.element.GoBar;
import com.proteanplatform.protean.element.GoCommand;
import com.proteanplatform.protean.element.Table;
import com.proteanplatform.protean.entity.EntityTableCellMetadata;
import com.proteanplatform.protean.entity.EntityTableMetadata;
import com.proteanplatform.protean.entity.EntityTableWindow;
import com.proteanplatform.protean.entity.EntityUserSecurity;
import com.proteanplatform.websocket.WebSocketUser;

/**
 * @author Austin Miller
 *
 */
public class ProteanUser {
	private static Logger logger = LoggerFactory.getLogger(ProteanUser.class);
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static Class<? extends ProteanUser> userClass;
	
	private long nextId = 0;
	
	private Map<String,String> clientHeaders = new HashMap<String,String>();

	private Map<Long,AbstractElement> elements = new HashMap<Long,AbstractElement>();

	private ProteanServer proteanServer;
	
	private EntityUserSecurity entityUserSecurity;
	
	private EntityManager entityManager;
	
	void setUser(WebSocketUser wsUser) {
		this.wsUser = wsUser;
	}
	

	/**
	 * @param message
	 */
	void handleMessage(String message) {
		switch(status) {
		
		case DELIVERING_PAYLOAD:
			break;
		
		case NEGOTIATING_HEADERS:
			receiveHeaders(message);
			break;
			
		case OPEN:
			handleMessageOpen(message);
			break;
			
		default:
			break;
		
		}
	}

	/**
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	private void handleMessageOpen(String message) {
		try {
			Map<String,Object> map = (Map<String, Object>) mapper.readValue(message, Map.class).get("event");
			
			if(map.get("name").equals("go")) {
				Map<String,Object> args = (Map<String,Object>) map.get("args");
				go(args.get("command").toString());
			} else {
				AbstractElement el = elements.get(Long.parseLong((String) map.get("id")));
				el.getMetadata().invoke(el, map);
			}
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}


	/**
	 * @param object
	 */
	private void go(String command) {
		EntityType<?> entity = proteanServer.getEntities().get(command);
		
		if(entity != null && entityUserSecurity.hasAccess(this, entity)) {
			load(new EntityTableWindow(entity));
		} else {
			entityUserSecurity.go(command);
		}
	}


	/**
	 * @param message
	 */
	private void receiveHeaders(String message) {
		for(String line : message.split("\n")) {
			String []tokens = line.split(":",2);
			clientHeaders.put(tokens[0].trim(), tokens[1].trim());
			System.out.println("clientHeaders " + toString() + " " + clientHeaders);
		}
		
		String version = clientHeaders.get("version");
		if(version == null || version.equals(ProteanServer.PROTEAN_PROTOCOL_VERSION) == false) {
			System.out.println("failing user "+wsUser.getId());
			wsUser.fail();
		}

		status = Status.OPEN;
		
	}
	
	public final void setBackground(Background background) {
		command("Background",background);
	}

	public final void load(AbstractElement element) {
		try {
			
			register(element);
			command(element.getCommand(),element);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param object
	 * @return
	 */
	public void register(AbstractElement element) {

		if(elements.containsKey(element.getId())) {
			return;
		}
		
		element.setUser(this);
		element.setId(nextId);
		++nextId;
		
		elements.put(element.getId(), element);
	}
	
	public final void destroy(AbstractElement element) {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", element.getId());
		command("Destroy",m);
		elements.remove(element.getId());
	}

	private WebSocketUser wsUser;
	
	private Status status = Status.NEGOTIATING_HEADERS;

	public enum Status {
		NEGOTIATING_HEADERS,
		DELIVERING_PAYLOAD,
		OPEN;
	}
	
	public int getId() {
		return wsUser.getId();
	}
	
	public String toString() {
		return "user " + wsUser.getId();
	}
	
	public void sendServerTime() {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("epoch", System.currentTimeMillis());
		command("Time",m);
	}
	
	public void sendGoBar() {
		GoBar goBar = new GoBar();
		for(EntityType<?> entity : proteanServer.getEntities().values()) {
			if(entityUserSecurity.hasAccess(this, entity)) {
				GoCommand command = new GoCommand();
				command.setLabel(entity.getName());
				command.setValue(entity.getName());
				goBar.getCommands().add(command);
			}
		}
		
		load(goBar);
	}
	
	private final void command(String cmd, Object o) {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put(cmd, o);
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw,m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		wsUser.send(sw.toString());
	}


	public ProteanServer getProteanServer() {
		return proteanServer;
	}


	public void setProteanServer(ProteanServer proteanServer) {
		this.proteanServer = proteanServer;
	}


	public EntityUserSecurity getEntityUserSecurity() {
		return entityUserSecurity;
	}


	public void setEntityUserSecurity(EntityUserSecurity entityUserSecurity) {
		this.entityUserSecurity = entityUserSecurity;
	}
	


	public Form constructCreateEntityForm(EntityType<?> entity) {
		Form form = new Form();
		EntityTableMetadata metadata = EntityTableMetadata.getMetadata(entity);
		
		form.setElements(metadata.getElements());
		return form;
		
	}
	
	@SuppressWarnings("rawtypes")
	public Table constructEntityTable(EntityType<?> entity) {
		Table table = new Table();

		try {
			EntityTableMetadata metadata = EntityTableMetadata.getMetadata(entity);
	
			Query query = getEntityManager().createQuery(entityUserSecurity.getQuery(this, entity));
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




	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
