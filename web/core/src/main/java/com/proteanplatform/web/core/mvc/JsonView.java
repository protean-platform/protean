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

package com.proteanplatform.web.core.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class uses jackson to serialize anything put in the model map as json for
 * a spring mvc request.  There is a possible security hole if classes are
 * put into the model map that contain sensitive data and aren't annotated
 * so that jackson ignores the sensitive data. 
 *
 * @author Austin Miller
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
@Component
public class JsonView implements View {

	@Override
	public String getContentType() {
		return "application/json";
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType(getContentType());

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonGenerator json = null;

		try {
			json = factory.createGenerator(response.getWriter());
			json.writeObject(model);
		} finally {
			if (json != null) {
				json.close();
			}
		}
	}

}
