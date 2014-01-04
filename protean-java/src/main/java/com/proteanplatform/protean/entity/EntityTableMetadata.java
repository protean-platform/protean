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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proteanplatform.protean.Protean;
import com.proteanplatform.protean.ProteanValidator;
import com.proteanplatform.protean.element.FormElement;
import com.proteanplatform.protean.element.FormElementType;



/**
 * @author Austin Miller
 *
 */
public class EntityTableMetadata {
	private static Logger logger = LoggerFactory.getLogger(EntityTableMetadata.class);
	private static CharBuffer buffer = CharBuffer.allocate(1<<8);
	
	private List<EntityTableCellMetadata> cells = new ArrayList<EntityTableCellMetadata>();
	private List<FormElement> elements = new ArrayList<FormElement>();
	private List<String> visibleColumns = new ArrayList<String>();
	private EntityType<?> entity;
	
	/**
	 * @param entity
	 * @throws NoSuchMethodException 
	 */
	public EntityTableMetadata(EntityType<?> entity) {
		visibleColumns.addAll(Arrays.asList(entity.getJavaType().getAnnotation(Protean.class).defaultColumns()));
		this.entity = entity;
	    calculateAnnotations("",entity.getJavaType());
	}
	
	/**
	 * Crawl all super annotations to get inheriting values.
	 * @param clazz 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	private void calculateAnnotations(String prefix,Class<?> clazz) {
		if(clazz == Object.class) {
			return;
		}
		
		calculateAnnotations(prefix,clazz.getSuperclass());
		
		Protean admin = clazz.getAnnotation(Protean.class);
		
		if(admin == null) {
			return;
		}
		
		for(String column : admin.columns()) {
			processColumn(prefix,admin,clazz, column);
		}
	}

	/**
	 * @param clazz
	 * @param column
	 */
	private void processColumn(String prefix,Protean admin, Class<?> clazz, String column) {
		try {
			Field field = clazz.getDeclaredField(column);
			
			if(field.getAnnotation(Embedded.class) != null) {
				calculateAnnotations(prefix+column+".",field.getType());
				return;
			}
			
			Protean cAdmin = field.getAnnotation(Protean.class);
			Column col = field.getAnnotation(Column.class);
			
			int length = 0;
			String def = "";
			
			if(col != null) {
				length = col.length();
				def = col.columnDefinition();
			}
			
			FormElement el = new FormElement();
			
			if(cAdmin != null) {
				el.setLabel(cAdmin.label().length() == 0 ? format(column) : cAdmin.label());
			} else {
				el.setLabel(format(column));
			}
			
			if(field.getType() == String.class) {
				if(def.contains("TEXT")) {
					el.setType(FormElementType.text);
					el.setMaxLength(0);
				} else if(length > 256) {
					el.setType(FormElementType.text);
					el.setMaxLength(length);
				} else {
					el.setType(FormElementType.string);
					el.setMaxLength(length);
				}
			} else if(field.getType() == boolean.class ||
					field.getType() == Boolean.class) {
				el.setType(FormElementType.bool);
			} else if(field.getType() == int.class ||
					field.getType() == Integer.class ||
					field.getType() == long.class ||
					field.getType() == Long.class ||
					field.getType() == short.class ||
					field.getType() == Short.class ||
					field.getType() == byte.class ||
					field.getType() == Byte.class ) {
					el.setType(FormElementType.integer);
			} else if(field.getType() == float.class ||
					field.getType() == Float.class ||
					field.getType() == Double.class ||
					field.getType() == double.class) {
				el.setType(FormElementType.numeric);
			} else if(field.getType().isEnum())  {
				el.setType(FormElementType.enumeration);
				el.setEnumValues(field.getType().getEnumConstants());
			} else if(Date.class.isAssignableFrom(field.getType())) {
				el.setType(FormElementType.date);
			} else if(Object.class.isAssignableFrom(field.getType())) {
				// TODO bah this one is tough
				el.setType(FormElementType.select);
			}
			
			for(ProteanValidator validator : admin.validator()) {
				el.addValidators(validator.getRegex(), validator.getError());
			}
			
			if(cAdmin == null || cAdmin.tab().equals("General")) {
				el.setTab(admin.tab());
			} else {
				el.setTab(cAdmin.tab());
			}
			
			if(cAdmin == null || cAdmin.section().equals("General")) {
				el.setSection(admin.section());
			} else {
				el.setSection(cAdmin.section());
			}
			
			if(cAdmin != null) {
				el.setTooltip(cAdmin.tooltip());
			}
			
			String name = prefix+column;
			el.setName(name);
			
			getElements().add(el);
			
			EntityTableCellMetadata cell = new EntityTableCellMetadata(name,entity.getJavaType());
			cells.add(cell);
			
			if(visibleColumns.isEmpty() == false && visibleColumns.contains(name) == false) {
				cell.getColumn().setbVisible(false);
			} 
		} catch(Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * <p>Converts camelcase and underscores to a string with spaces with some
	 * intelligence.</p>
	 * @param column
	 * @return
	 */
	private String format(String in) {
		buffer.clear();
		
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

	public List<EntityTableCellMetadata> getCells() {
		return cells;
	}
	
	public List<Object> toRow(Object o) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Object> line = new ArrayList<Object>();
		
		for(EntityTableCellMetadata cell : cells) {
			line.add(cell.toCellString(o));
		}
		
		return line;
	}

	public List<FormElement> getElements() {
		return elements;
	}

	public void setElements(List<FormElement> elements) {
		this.elements = elements;
	}



}
