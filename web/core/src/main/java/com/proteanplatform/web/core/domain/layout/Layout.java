package com.proteanplatform.web.core.domain.layout;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import com.proteanplatform.protean.Protean;
import com.proteanplatform.web.core.domain.AbstractEntity;

@Entity
@Protean(
	columns={"layoutPage","htmlOptions","resources"},
	defaultColumns={"id","externalId","layoutPage"},
	section="Layout"
)
public class Layout extends AbstractEntity {
	
	@Column(name="layout_page",nullable=false,length=128)
	private String layoutPage;
	
	@Embedded
	private HtmlOptions htmlOptions;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Resource> resources;
	
	public String getLayoutPage() {
		return layoutPage;
	}

	public void setLayoutPage(String layoutPage) {
		this.layoutPage = layoutPage;
	}

	public HtmlOptions getHtmlOptions() {
		if(htmlOptions == null) {
			htmlOptions = new HtmlOptions();
		}
		return htmlOptions;
	}

	public void setHtmlOptions(HtmlOptions htmlOptions) {
		this.htmlOptions = htmlOptions;
	}

	public List<String> getResourceUrlsByType(ResourceType resourceType) {
		List<String> resourceUrls = new ArrayList<>();
		for(Resource resource : resources) {
			if(resource.getResourceType() == resourceType) {
				resourceUrls.add(resource.getUrl());
			}
		}
		return resourceUrls;
	}

	public List<Resource> getResources() {
		if(resources == null) {
			resources = new ArrayList<>();
		}
		
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
}
