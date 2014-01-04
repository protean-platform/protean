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
package com.proteanplatform.protea;

import java.util.HashMap;
import java.util.Map;

import com.proteanplatform.websocket.WebSocketListener;
import com.proteanplatform.websocket.WebSocketServer;
import com.proteanplatform.websocket.WebSocketUser;

/**
 * @author Austin Miller
 *
 */
public abstract class ProTeaServer<T extends ProTeaUser> implements WebSocketListener {
	

	public static final String PROTEA_PROTOCOL_VERSION = "1";
	
	protected WebSocketServer server = new WebSocketServer();
	protected abstract Class<T> setProTeaUserClass();
	private Class<T> userClass;
	private Map<Integer,T> users = new HashMap<Integer,T>();
	private Map<String,String> serverHeaders = new HashMap<String,String>();
	

	public ProTeaServer() {
		userClass = setProTeaUserClass();
		serverHeaders.put("version", PROTEA_PROTOCOL_VERSION);
		userClass = this.setProTeaUserClass();
		server.setPort(setPort());
		server.setWebSocketListener(this);
		server.setProtocol("protea");
	}


	
	/**
	 * @return
	 */
	protected abstract int setPort();


	public T getUser(int id) {
		return users.get(id);
	}

	/* (non-Javadoc)
	 * @see org.codefrags.websocket.WebSocketListener#onNewUser(org.codefrags.websocket.WebSocketUser)
	 */
	public void onNewUser(WebSocketUser user) {
		try {
			System.out.println("new user "+user.getId());
			
			T ptu;
			ptu = userClass.newInstance();
			
			ptu.setUser(user);
			users.put(user.getId(), ptu);
			
			this.onNewUser(ptu);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	protected abstract void onNewUser(T user);

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
		this.onCloseConnection(getUser(user.getId()));
		users.remove(user.getId());
	}
	
	protected abstract void onCloseConnection(T user);

	public void run() {
		Thread thread = new Thread(server);
		thread.start();
	}
	
	/**
	 * This will block until the server thread shuts down.
	 */
	public void stop() {
		server.shutdown(false);
	}
	

}
