package com.proteanplatform.web.core.domain.layout;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.beans.Mergeable;

import com.proteanplatform.protean.Protean;


/**
 * <p>Controls the meta values for an entity that embeds this type.  Thus, it
 * expects that the entity is an HTML displayable type.</p>
 *  
 * @author Austin Miller
 */
@Embeddable
@Protean(
	columns={"keywords","title","description","contentType"},
	section="HTML",
	tab="Page"
)
public class HtmlOptions implements Mergeable {
	
	@Column(name="html_keywords",columnDefinition="TEXT")
	private String keywords;
	
	@Column(name="html_title",length=128)
	private String title;
	
	@Column(name="html_description",columnDefinition="TEXT")
	private String description;
	
	@Column(name="html_content_type",length=128)
	private String contentType = "text/html; charset=utf-8";

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.Mergeable#isMergeEnabled()
	 */
	@Override
	public boolean isMergeEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.Mergeable#merge(java.lang.Object)
	 */
	@Override
	public Object merge(Object parent) {
		HtmlOptions o = (HtmlOptions) parent;
		
		if(o == null) {
			return this;
		}
		
		HtmlOptions options = new HtmlOptions();
		options.setContentType(o.getContentType() == null ? getContentType() : o.getContentType());
		options.setDescription(o.getDescription() == null ? getDescription() : o.getDescription());
		options.setKeywords(o.getKeywords() == null ? getKeywords() : o.getKeywords());
		return options;
	}

}
