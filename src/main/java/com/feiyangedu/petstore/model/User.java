package com.feiyangedu.petstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.feiyangedu.petstore.constant.Gender;

@Entity
public class User extends AbstractEntity {

	private String email;
	private String name;
	private int role;
	private Gender gender;

	@Column(nullable = false, unique = true, length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	@Column(nullable = false, length = 50)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

}
