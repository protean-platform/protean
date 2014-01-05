package com.proteanplatform.web.core.mvc;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

@Component
public class JsonViewResolver implements ViewResolver,Ordered {

	@Autowired
	JsonView jsonView;
	
	int order=0;
	
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		
		return jsonView;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
