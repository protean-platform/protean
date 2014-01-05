package com.proteanplatform.web.core.domain;

import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.ui.ModelMap;

import com.proteanplatform.protean.Protean;
import com.proteanplatform.web.core.domain.layout.HtmlOptions;
import com.proteanplatform.web.core.domain.layout.Layout;
import com.proteanplatform.web.core.domain.layout.ResourceType;

@MappedSuperclass
@Protean(
	columns={"layout","htmlOptions"},
	tab="Page"
)
public abstract class AbstractViewEntity extends AbstractEntity {

	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	private Layout layout;
	
	@Embedded
	private HtmlOptions htmlOptions;

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	public final String createView(ModelMap modelMap) {
		modelMap.put("stylesheets",layout.getResourceUrlsByType(ResourceType.STYLESHEET));
		modelMap.put("javascripts",layout.getResourceUrlsByType(ResourceType.JAVASCRIPT));
		modelMap.put("htmlOptions",layout.getHtmlOptions().merge(getHtmlOptions()));
		modelMap.put("bodyLayout", getBodyLayout());
		return "layout/" + layout.getLayoutPage();
	}
	
	public abstract String getBodyLayout();

	public HtmlOptions getHtmlOptions() {
		return htmlOptions;
	}

	public void setHtmlOptions(HtmlOptions htmlOptions) {
		this.htmlOptions = htmlOptions;
	} 
}
