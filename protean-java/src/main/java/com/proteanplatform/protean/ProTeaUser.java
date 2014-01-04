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
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proteanplatform.protean.element.AbstractElement;
import com.proteanplatform.protean.element.Background;
import com.proteanplatform.websocket.WebSocketUser;

/**
 * @author Austin Miller
 *
 */
public abstract class ProTeaUser {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	static Class<? extends ProTeaUser> userClass;
	
	private long nextId = 0;
	
	private Map<String,String> clientHeaders = new HashMap<String,String>();

	private Map<Long,AbstractElement<? extends ProTeaUser>> elements = new HashMap<Long,AbstractElement<? extends ProTeaUser>>();

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
			
			
			AbstractElement<?> el = elements.get(Long.parseLong((String) map.get("id")));
			el.getMetadata().invoke(el, map);
			

		} catch(Exception e) {
			e.printStackTrace();
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
		if(version == null || version.equals(ProTeaServer.PROTEA_PROTOCOL_VERSION) == false) {
			System.out.println("failing user "+wsUser.getId());
			wsUser.fail();
		}

		status = Status.OPEN;
		
	}
	
	public final void setBackground(Background background) {
		command("Background",background);
	}

	public final void load(Object object) {
		try {
			
			AbstractElement<?> element = register(object);
			command(element.getCommand(),element);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AbstractElement<?> register(Object object) {

		
		
		AbstractElement<?> element = (AbstractElement<?>) object;
		
		if(elements.containsKey(element.getId())) {
			return elements.get(element.getId());
		}
		
		element.setUser(this);
		element.setId(nextId);
		++nextId;
		
		elements.put(element.getId(), (AbstractElement<? extends ProTeaUser>) element);
		return element;
	}
	
	public final void destroy(AbstractElement<? extends ProTeaUser> element) {
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
}
