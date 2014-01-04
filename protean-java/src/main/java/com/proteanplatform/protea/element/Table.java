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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proteanplatform.protea.element.TableColumn.Sorting;

/**
 * @author Austin Miller
 *
 */
public abstract class Table<T> extends AbstractElement<T> {

	@Override
	public final String getCommand() {
		return "Table";
	}
	
	private List<TableColumn> columns = new ArrayList<TableColumn>();
	private List<List<Object>> rows = new ArrayList<List<Object>>();

	@JsonIgnore
	public List<TableColumn> getColumns() {
		return columns;
	}
	
	public void addColumn(String name) {
		addColumn(name,null,Sorting.BOTH);
	}
	
	public void addColumn(String name,String type) {
		addColumn(name,type,Sorting.BOTH);
	}
	
	public void addColumn(String name, String type, Sorting sorting) {
		columns.add(new TableColumn(name,type,sorting));
	}
	
	
	@JsonProperty("dtOptions")
	public Map<String,Object> getDataTableOptions() {
		Map<String,Object> dtOptions = new HashMap<String,Object>();
		dtOptions.put("aoColumns", columns);
		
		dtOptions.put("aaData",rows);
		
		return dtOptions;
	}
	
	public void addRow(List<Object> row) {
		rows.add(row);
	}

	public void addRow(Object ... args) {
		rows.add(Arrays.asList(args));
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}
	
	
}
