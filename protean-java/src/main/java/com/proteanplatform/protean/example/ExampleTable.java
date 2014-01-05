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
package com.proteanplatform.protean.example;

import java.util.Random;

import com.proteanplatform.protean.element.Table;
import com.proteanplatform.protean.element.TableColumn;
import com.proteanplatform.protean.element.TableColumn.Sorting;

/**
 * @author Austin Miller
 *
 */
public class ExampleTable extends Table {
	public ExampleTable() {
		super();
		
		Random random = new Random();
		
		for(int i =0;i<1000;++i) {
			addRow(i,"TEST"+i,"foobar",random.nextInt());
		}

		getColumns().add(new TableColumn("id",null,Sorting.BOTH));
		getColumns().add(new TableColumn("external_id",null,Sorting.BOTH));
		getColumns().add(new TableColumn("name",null,Sorting.BOTH));
		getColumns().add(new TableColumn("created_date",null,Sorting.BOTH));
	}

}
