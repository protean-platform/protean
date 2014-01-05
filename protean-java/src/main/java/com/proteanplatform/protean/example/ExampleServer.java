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
package com.proteanplatform.protean.example;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.proteanplatform.protean.ProteanSubscriber;
import com.proteanplatform.protean.ProteanServer;
import com.proteanplatform.protean.ProteanUser;

/**
 * @author Austin Miller
 *
 */
public class ExampleServer implements ProteanSubscriber {

	public static void main(String [] args) throws JsonParseException, JsonMappingException, IOException {
		
		ProteanServer server = new ProteanServer();
		server.setPort(8090);
		server.subscribe(new ExampleServer());
		server.run();
	}

	/* (non-Javadoc)
	 * @see com.proteanplatform.protean.ProteanListener#onNewUser(com.proteanplatform.protean.ProteanUser)
	 */
	@Override
	public void onNewUser(ProteanUser user) {
		ExampleTableWindow etw = new ExampleTableWindow();
		user.sendServerTime();
		user.load(etw);
	}


	/* (non-Javadoc)
	 * @see com.proteanplatform.protean.ProteanListener#onCloseConnection(com.proteanplatform.protean.ProteanUser)
	 */
	@Override
	public void onCloseConnection(ProteanUser user) {
		
	}

}
