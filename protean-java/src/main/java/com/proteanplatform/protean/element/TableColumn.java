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

import java.util.Arrays;
import java.util.List;

/**
 * @author Austin Miller
 *
 */
public class TableColumn {

	public enum Sorting {
		ASC,
		DESC,
		BOTH;
	}
	
	private String sTitle;
	private String sType;
	private List<String> asSorting;
	private boolean bVisible = true;
	
	public TableColumn(String sTitle, String sType, Sorting sorting) {
		this.sTitle = sTitle;
		this.sType = sType;
		setAsSorting(sorting);
	}
	
	public List<String> getAsSorting() {
		return asSorting;
	}
	
	public void setAsSorting(Sorting sorting) {
		switch(sorting) {
		case ASC:
			asSorting = Arrays.asList("asc");
			break;
		case BOTH:
			asSorting = Arrays.asList("asc","desc");
			break;
		case DESC:
			asSorting = Arrays.asList("desc");
			break;
		}
	}
	public String getsType() {
		return sType;
	}
	public void setsType(String sType) {
		this.sType = sType;
	}
	public String getsTitle() {
		return sTitle;
	}
	public void setsTitle(String sTitle) {
		this.sTitle = sTitle;
	}

	public boolean isbVisible() {
		return bVisible;
	}

	public void setbVisible(boolean bVisible) {
		this.bVisible = bVisible;
	}
}
