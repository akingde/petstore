package com.feiyangedu.petstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Setting extends AbstractEntity {

	private String name;
	private String value;

	@Column(nullable = false, unique = true, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 200)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
