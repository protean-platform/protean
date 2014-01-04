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
package com.proteanplatform.protea.example;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.proteanplatform.protea.ProTeaServer;

/**
 * @author Austin Miller
 *
 */
public class ExampleServer extends ProTeaServer<ExampleUser> {

	
	/* (non-Javadoc)
	 * @see org.codefrags.protea.ProTeaServer#setProTeaUserClass()
	 */
	@Override
	protected Class<ExampleUser> setProTeaUserClass() {
		return ExampleUser.class;
	}
	

	public static void main(String [] args) throws JsonParseException, JsonMappingException, IOException {
		
		ExampleServer edl = new ExampleServer();
		edl.run();
	}

	/* (non-Javadoc)
	 * @see org.codefrags.protea.ProTeaServer#onNewUser(org.codefrags.protea.ProTeaUser)
	 */
	@Override
	protected void onNewUser(ExampleUser user) {
		ExampleTableWindow etw = new ExampleTableWindow();
		user.sendServerTime();
		user.load(etw);
	}

	/* (non-Javadoc)
	 * @see org.codefrags.protea.ProTeaServer#onCloseConnection(org.codefrags.protea.ProTeaUser)
	 */
	@Override
	protected void onCloseConnection(ExampleUser user) {
		
	}

	/* (non-Javadoc)
	 * @see org.codefrags.protea.ProTeaServer#setPort()
	 */
	@Override
	protected int setPort() {
		return 8090;
	}

}
