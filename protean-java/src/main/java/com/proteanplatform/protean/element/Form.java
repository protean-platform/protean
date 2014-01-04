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
 * @author Austin Miller
 */
public abstract class Form<T> extends AbstractElement<T>{

	/* (non-Javadoc)
	 * @see org.codefrags.protea.element.AbstractElement#getCommand()
	 */
	@Override
	public String getCommand() {
		return "Form";
	}

	public List<FormElement> getElements() {
		return elements;
	}

	public void setElements(List<FormElement> elements) {
		this.elements = elements;
	}

	private List<FormElement> elements = new ArrayList<FormElement>();

}
