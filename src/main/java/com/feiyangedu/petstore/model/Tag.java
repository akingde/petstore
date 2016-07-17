package com.feiyangedu.petstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Tag extends AbstractEntity {

	private String name;

	public Tag() {
	}

	@Column(nullable = false, unique = true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
