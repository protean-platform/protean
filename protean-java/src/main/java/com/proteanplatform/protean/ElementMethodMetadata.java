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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Austin Miller
 *
 */
public class ElementMethodMetadata {

	private Method method;
	
	private boolean hasArg = false;

	public ElementMethodMetadata(Method method) {
		
		Type[] gtypes = method.getGenericParameterTypes();
		
		if(gtypes.length == 1) {
			hasArg = true;
		}
		
		this.method = method;
	}
	
	public void invoke(Object object, Map<String, Object> args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(hasArg) {
			Object [] objs = { args };
			method.invoke(object, objs);
		} else {
			method.invoke(object, (Object[])null);
		}
	}
}
