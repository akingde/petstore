package com.feiyangedu.petstore.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class PetTag extends AbstractEntity {

	private Pet pet;
	private Tag tag;

	public PetTag() {
	}

	@ManyToOne
	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	@ManyToOne
	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

}
