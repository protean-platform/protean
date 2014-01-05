package com.proteanplatform.web.core.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.FilterRegistration.Dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import com.proteanplatform.web.core.mvc.CoreMvcConfig;

public class CoreApplicationInitializer implements WebApplicationInitializer {

	Logger logger = LoggerFactory.getLogger(CoreApplicationInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.info("Initializing the Client Web Context");
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(CoreConfig.class,SecurityConfig.class);
		context.refresh();

		servletContext.addListener(new ContextLoaderListener(context));
		servletContext.setInitParameter("defaultHtmlEscape", "true");

		AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
		mvcContext.register(CoreMvcConfig.class);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(
				mvcContext));
		dispatcher.setLoadOnStartup(1);
		Set<String> mappingConflicts = dispatcher.addMapping("/");
		
		servletContext.getServletRegistration ("default").addMapping ("*.js", "*.css", "*.jpg", "*.gif", "*.png");
		if (!mappingConflicts.isEmpty()) {
			for (String s : mappingConflicts) {
				logger.error("Mapping conflict: " + s);
			}
			throw new IllegalStateException("'appServlet' cannot be mapped to '/' under Tomcat versions <= 7.0.14");
		}
		
        
        registerFilter(servletContext);

	}
	
    private final void registerFilter(ServletContext servletContext) {
    	String filterName = "springSecurityFilterChain";
    	DelegatingFilterProxy springSecurityFilterChain = new DelegatingFilterProxy(filterName);
    	
        Dynamic registration = servletContext.addFilter(filterName, springSecurityFilterChain);
        if(registration == null) {
            throw new IllegalStateException("Duplicate Filter registration for '" + filterName +"'. Check to ensure the Filter is only configured once.");
        }
        registration.setAsyncSupported(true);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR, DispatcherType.ASYNC);
        registration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
    }

}