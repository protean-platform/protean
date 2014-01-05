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

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proteanplatform.protean.entity.EntityUserSecurity;
import com.proteanplatform.websocket.WebSocketListener;
import com.proteanplatform.websocket.WebSocketServer;
import com.proteanplatform.websocket.WebSocketUser;

/**
 * @author Austin Miller
 *
 */
public class ProteanServer implements WebSocketListener {
	private static Logger logger = LoggerFactory.getLogger(ProteanServer.class);
	private static ProteanServer instance;
	public static final String PROTEAN_PROTOCOL_VERSION = "1";
	
	public static ProteanServer getInstance() {
		return instance;
	}
	
	protected WebSocketServer server = new WebSocketServer();
	private Map<Integer,ProteanUser> users = new HashMap<Integer,ProteanUser>();
	private Map<String,String> serverHeaders = new HashMap<String,String>();
	private Set<ProteanSubscriber> subscribers = new HashSet<>();
	private EntityManager entityManager;
	private Map<String,EntityType<?>> entities = new HashMap<String,EntityType<?>>();
	private EntityUserSecurity entityUserSecurity = new EntityUserSecurity();
	

	public ProteanServer() {
		serverHeaders.put("version", PROTEAN_PROTOCOL_VERSION);
		server.setWebSocketListener(this);
		server.setProtocol("protean");
		instance = this;
	}
	
	/**
	 * @return
	 */
	public void setPort(int port) {
		server.setPort(port);
	}


	public ProteanUser getUser(int id) {
		return users.get(id);
	}

	/* (non-Javadoc)
	 * @see org.codefrags.websocket.WebSocketListener#onNewUser(org.codefrags.websocket.WebSocketUser)
	 */
	public void onNewUser(WebSocketUser user) {
		try {
			System.out.println("new user "+user.getId());
			
			ProteanUser ptu = new ProteanUser();
			
			ptu.setProteanServer(this);
			ptu.setEntityUserSecurity(entityUserSecurity);
			ptu.setUser(user);
			ptu.setEntityManager(entityManager);
			
			users.put(user.getId(), ptu);
			
			for(ProteanSubscriber subscriber: subscribers) {
				subscriber.onNewUser(ptu);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/* (non-Javadoc)
	 * @see org.codefrags.websocket.WebSocketListener#onMessage(org.codefrags.websocket.WebSocketUser, java.lang.String)
	 */
	public void onMessage(WebSocketUser user, String message) {
		getUser(user.getId()).handleMessage(message);
	}

	/* (non-Javadoc)
	 * @see org.codefrags.websocket.WebSocketListener#onCloseConnection(org.codefrags.websocket.WebSocketUser)
	 */
	public void onCloseConnection(WebSocketUser user) {
		for(ProteanSubscriber subscriber: subscribers) {
			subscriber.onNewUser(users.get(user.getId()));
		}
		users.remove(user.getId());
	}
	

	public void run() {
		
		logger.debug("Starting protean server");
		
		if(entityManager != null) {
			for(EntityType<?> entity : entityManager.getMetamodel().getEntities()) {
				if(entity.getJavaType().getAnnotation(Protean.class) == null) {
					continue;
				}
				
				getEntities().put(entity.getName(),entity);
			}
		}
		
		Thread thread = new Thread(server);
		thread.start();
		logger.info("Started protean server");
	}
	
	/**
	 * This will block until the server thread shuts down.
	 */
	public void stop() {
		server.shutdown(false);
		logger.info("Stopped protean server");
	}

	
	
	/**
	 * <p>Converts camelcase and underscores to a string with spaces with some
	 * intelligence.</p>
	 * @param column
	 * @return
	 */
	public static String camelCaseToSentence(String in) {
		CharBuffer buffer = CharBuffer.allocate(in.length()+100);
		
		boolean last = false; // was last a capital?
		boolean lastu = false; // was last a capital?
		
		char ch;
		char next = in.charAt(0);
		
		for(int i = 1;i<in.length();++i) {
			
			ch = next;
			next = in.charAt(i);
			
			if((Character.isUpperCase(ch) && last == false) ||
					(Character.isUpperCase(ch) && last == true && 
					Character.isUpperCase(next) == false && next != '_')) {
				
				buffer.append(" ");
			}
			
			
			if(ch == '_') {
				buffer.append(" ");
			} else if(lastu) {
				buffer.append(Character.toUpperCase(ch));
			} else {
				buffer.append(ch);
			}
			
			last = Character.isUpperCase(ch) ? true : false;
			lastu = ch == '_' ? true : false;
		}
		
		buffer.append(next);
		
		buffer.put(0, Character.toUpperCase(buffer.get(0)));
		buffer.flip();
		
		return buffer.toString();
	}
	
	/**
	 * @param exampleServer
	 */
	public void subscribe(ProteanSubscriber subscriber) {
		subscribers.add(subscriber);
	}
	
	public void unsubscribe(ProteanSubscriber subscriber) {
		subscribers.remove(subscriber);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public EntityUserSecurity getEntityUserSecurity() {
		return entityUserSecurity;
	}

	public void setEntityUserSecurity(EntityUserSecurity entityUserSecurity) {
		this.entityUserSecurity = entityUserSecurity;
	}

	public Map<String,EntityType<?>> getEntities() {
		return entities;
	}

	public void setEntities(Map<String,EntityType<?>> entities) {
		this.entities = entities;
	}
	
	
}
