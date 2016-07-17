package com.feiyangedu.petstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Category extends AbstractEntity {

	private String name;
	private String description;
	private String imageId;

	public Category() {
	}

	@Column(nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 50)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false, length = 50)
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

}
