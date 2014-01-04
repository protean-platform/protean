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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to specify how an entity is viewed by the admin console, in
 * table views, programs, and create and edit forms.  If it is absent from an entity, the
 * entity will not be accessible from the console.</p>
 * 
 * <p>All super objects will have their annotations considered, as well, with each property
 * appending or overwriting, as makes sense.  All field/method annotations are assumed to be 
 * annotating private fields, such that checking inheritence makes no sense.</p>
 * 
 * <p>Do not annotate embeddable fields in the embedded object.  Set the embeddable field as
 * part of the columns and annotate the embeddable directly.</p>
 * 
 * @author Austin Miller
 */
@Documented
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RUNTIME)
public @interface Protean {

	/**
	 * <p>Meaningful on an object.</p>
	 * 
	 * <p>The columns that will be available to render on the table.  This also controls 
	 * what is editable in a create and edit form.</p>
	 * 
	 * <p>Inheriting objects will be considered appended to the end of this field</p>
	 * @return
	 */
	String[] columns() default {};
	
	/**
 	 * <p>Meaningful on an object.</p>
	 * 
	 * <p>The default columns to render on the table.  If left blank, all
	 * columns will be rendered.</p>
	 * 
	 * <p>This will overwrite a super object's annotation.</p>
	 * 
	 * @return
	 */
	String[] defaultColumns() default {};
	
	/**
	 * <p>Meaningful on an object.</p>
	 * 
	 * <p>The method on this object to use to represent this object if referenced
	 * by another table.  It can return an another admin object, a primitive
	 * type, or a string, but not void.  It also cannot take arguments.</p>
	 * 
	 * <p>This will overwrite a super object's annotation.</p>
	 * 
	 * @return
	 */
	String cellMethod() default "toString";
	
	/**
	 * <p>Meaningful on a field or object.</p>
	 * 
	 * <p>This field affects CRUD forms, displaying the property in this tab.
	 * 
	 * <p>There can be just one tab if all properties call this "General", or this is 
	 * left blank.  In which case, there will be no tabs on the form.</p>
	 * 
	 * <p>On an object, applies to every field that doesn't overwrite with its own
	 * tab. Does not overwrite super objects.</p>
	 * 
	 * @return
	 */
	String tab() default "General";
	
	/**
	 * <p>Meaningful on a field or object</p>
	 * 
	 * <p>Fields will be grouped in a section based on this value.  If all fields
	 * are in the same section, there will be no visible section separation.</p> 
	 * 
	 * <p>On an object, applies to every field that doesn't overwrite with its own
	 * tab. Does not overwrite super objects.</p>
	 * 
	 * @return
	 */
	String section() default "General";
	
	/**
	 * <p>Meaningful on a field.</p>
	 * 
	 * <p>If this is true, the field can be edited in an edit form.  If this is
	 * false, the field will be displayed disabled in an edit form but it will not
	 * be displayed in a create form.</p>
	 * 
	 * @return
	 */
	boolean enabled() default true;
	
	/**
	 * <p>Meaningful on a field.</p>
	 * 
	 * <p>What to label the field in a credit or edit form. If left blank, the
	 * camel case of the field will be converted by adding spaces and capitalizing
	 * the first letter.</p>
	 * 
	 * @return
	 */
	String label() default "";
	
	
	/**
	 * <p>Meaningful on a field</p>
	 * 
	 * <p>When hovering over the tooltip helper icon, will display this 
	 * message.  The icon will not appear if the string is empty.</p>
	 * 
	 * @return
	 */
	String tooltip() default "";
	
	/**
	 * <p>Meaningful on a field</p>
	 * 
	 * <p>If these validators are not passed, a create or edit will fail.</p>
	 * 
	 * @return
	 */
	ProteanValidator [] validator() default {};
}
