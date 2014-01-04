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
package com.proteanplatform.protea.element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Austin Miller
 *
 */
public class FormElement {

	private String label;
	private FormElementType type;
	private String tooltip = "";
	private String tab;
	private String section;
	private int maxLength = 0;
	private List<Validator> validators = new ArrayList<Validator>();
	private Object[] enumValues = {};
	private String name;
	
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public void addValidators(String regex, String error) {
		Validator validator = new Validator();
		validator.setRegex(regex);
		validator.setError(error);
		getValidators().add(validator);
	}
	public FormElementType getType() {
		return type;
	}
	public void setType(FormElementType type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public List<Validator> getValidators() {
		return validators;
	}
	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}
	public Object[] getEnumValues() {
		return enumValues;
	}
	public void setEnumValues(Object[] enumValues) {
		this.enumValues = enumValues;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
