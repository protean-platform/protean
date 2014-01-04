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
package com.proteanplatform.protean.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.proteanplatform.protean.Protean;
import com.proteanplatform.protean.element.TableColumn;
import com.proteanplatform.protean.element.TableColumn.Sorting;

/**
 * @author Austin Miller
 *
 */
public class EntityTableCellMetadata {

	private List<Method> methods = new ArrayList<Method>();
	private TableColumn column;
	
	/**
	 * @param clazz
	 * @param anno
	 * @throws NoSuchMethodException 
	 */
	public EntityTableCellMetadata(String name,Class<?> clazz) throws NoSuchMethodException {
		
		Method method = null;
		Class<?> rtype = clazz;
		
		String[]tokens = name.split("\\.");
		for(int i = 0;i<tokens.length-1;++i) {
			method = rtype.getMethod("get"+ StringUtils.capitalize(tokens[i]), new Class[0]);
			methods.add(method);
			rtype = method.getReturnType();
		}

		method = rtype.getMethod("get"+ StringUtils.capitalize(tokens[tokens.length-1]), new Class[0]);
		rtype = method.getReturnType();
		String type = "string"; // determines sortability
		
		// have to do string, date first, since they can both assign object.
		if(String.class.isAssignableFrom(rtype)) {
			methods.add(method);
		}
		
		else if (Date.class.isAssignableFrom(rtype)) {
			methods.add(method);
			type = "date";
		}

		else if(rtype == boolean.class || rtype == char.class ||
				rtype == Boolean.class || rtype == Character.class) {
			methods.add(method);
		}

		else if(rtype.isPrimitive() || rtype == int.class || rtype == Integer.class || rtype == Long.class ||
				rtype == Short.class ||	rtype == Byte.class || rtype == Double.class || rtype == Float.class )  {
			methods.add(method);
			type="numeric";
		}
		
		else if(Object.class.isAssignableFrom(rtype)) {
			calculateObjectCell(method);
		}
		
		column = new TableColumn(name,type,Sorting.BOTH);
		
	}

	
	public TableColumn getColumn() {
		return column;
	}
	public void setColumn(TableColumn column) {
		this.column = column;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	
	public String toCellString(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object o = object;
		for(Method method : methods) {
			o = method.invoke(o, (Object[])null);
			if(o == null) {
				return "";
			}
		}

		return o.toString();
	}
	
	/**
	 * <p>The representative of the an object cell must be a string or a number.  We let the
	 * admin annotation argument "cellMethod" determine which method should return a 
	 * legal represenation.  If this is itself an object, we repeat the procedure. </p>
	 * 
	 * @param cell
	 * @param method
	 * @throws NoSuchMethodException
	 */
	private void calculateObjectCell(Method method) throws NoSuchMethodException {
		methods.add(method);
		Class<?> rtype = method.getReturnType();
		
		while(Object.class.isAssignableFrom(rtype)) {
			
			if(rtype.isAnnotationPresent(Protean.class)) {
				Protean adm = rtype.getAnnotation(Protean.class);
				
				// we must insist that the method supplied must exist and must take no arguments
				try {
					method = rtype.getMethod(adm.cellMethod(), new Class[0]);
				} catch(Exception e) {
					method = null;
				}
				
				if(method == null) {
					method = rtype.getMethod("toString", new Class[0]);
				}
			} 
			
			else {
				method = rtype.getMethod("toString", new Class[0]);
			}
			
			rtype = method.getReturnType();
			methods.add(method);
			
			if(String.class.isAssignableFrom(rtype)) {
				break; // Otherwise, the loop might never end.
			}
		}
	}

}
