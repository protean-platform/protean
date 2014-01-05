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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.proteanplatform.protean.element.AbstractElement;
import com.proteanplatform.protean.element.Window;

/**
 * @author Austin Miller
 *
 */
public class ElementMetadata {
	
	/**
	 * 
	 */
	private static final String BASEPATH = "org.codefrags.protea.element.";
	private static Map<Class<?>,ElementMetadata> metadatas = new HashMap<Class<?>,ElementMetadata>();
	
	
	private static Map<String,Set<String>> elementMethods = new HashMap<String,Set<String>>();
	
	
	{
		elementMethods.put("gobar", new HashSet<String>(Arrays.asList("go")));
		elementMethods.put("table", new HashSet<String>(Arrays.asList("refresh","create","delete")));
		elementMethods.put("window", new HashSet<String>(Arrays.asList("close","create","delete","edit","ok","cancel")));
	}
	
	public static ElementMetadata getMetadata(Class<?> clazz) throws Exception {
		
		if(metadatas.containsKey(clazz)) {
			return metadatas.get(clazz);
		}
		
		ElementMetadata em;
		try {
			em = new ElementMetadata(clazz);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage(),e);
		}
		metadatas.put(clazz, em);
		
		
		return em; 
	}
	
	private Map<String,ElementMethodMetadata> methods = new HashMap<String,ElementMethodMetadata>();
	
	public ElementMetadata(Class<?> clazz) {
		
		if(clazz.isAssignableFrom(Window.class)) {
			System.out.println("what");
		}

		String elementName = getClass(clazz);
		
		Set<String> em = elementMethods.get(elementName); 
		
		if(em != null) {
			for(Method method : clazz.getMethods()) {
				String name = method.getName();
				if(em.contains(name)) {
					methods.put(name, new ElementMethodMetadata(method));
				}
			}
		}
	}
	
	/**
	 * @param clazz
	 */
	private String getClass(Class<?> clazz) {
		Class<?> cl = clazz;
		while(cl.getCanonicalName().startsWith(BASEPATH) == false) {
			cl = cl.getSuperclass();
		}
		
		return cl.getCanonicalName().replace(BASEPATH, "").toLowerCase();
	}

	public Set<String> getMethodNames() {
		return methods.keySet();
	}
	
	public void invoke(AbstractElement element,Map<String, Object> methodMap) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String methodName = (String) methodMap.get("name"); 
		@SuppressWarnings("unchecked")
		Map<String,Object> args = (Map<String, Object>) methodMap.get("args");

		methods.get(methodName).invoke(element,args);
	}
	
	
	

}
