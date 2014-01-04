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

/**
 * @author Austin Miller
 *
 */
public enum ProteanValidator {
	
	DOLLAR("^\\d+(\\.\\d{1,2})?$","Must be in a dollar format."),
	EMAIL("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b","Not a valid email."),
	INTEGRAL("^\\d+$","Must contain only digits."),
	NUMERIC("^\\d+(\\.\\d{1,})?$","Must be a numeric, decimal digits are allowed."),
	SOME(".*[?!\\w]{1,}.*","Must contain more than whitespace.")
	;
	
	private String regex;
	private String error;

	ProteanValidator(String regex,String error) {
		this.setRegex(regex);
		this.setError(error);
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
