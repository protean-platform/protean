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
package com.proteanplatform.protean.element;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the autocomplete bar on the menu, which attempts to map strings to loadable
 * "programs"
 * 
 * @author Austin Miller
 */
public class GoBar extends AbstractElement {

	public String getCommand() {
		return "GoBar";
	}
	
	public List<GoCommand> getCommands() {
		return commands;
	}

	public void setCommands(List<GoCommand> commands) {
		this.commands = commands;
	}

	private List<GoCommand> commands = new ArrayList<>();
}
