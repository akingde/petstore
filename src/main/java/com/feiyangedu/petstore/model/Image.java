package com.feiyangedu.petstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Image extends AbstractEntity {

	private String largeImageUrl;
	private String smallImageUrl;

	public Image() {
	}

	@Lob
	@Column(nullable = false)
	public String getLargeImageUrl() {
		return largeImageUrl;
	}

	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}

	@Lob
	@Column(nullable = false)
	public String getSmallImageUrl() {
		return smallImageUrl;
	}

	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

}
