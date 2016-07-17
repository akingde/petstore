package com.feiyangedu.petstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Address extends AbstractEntity {

	private String province;
	private String city;
	private String zip;
	private String street;
	private String contactName;

	public Address() {
	}

	@Column(nullable = false, length = 50)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(nullable = false, length = 50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(nullable = false, length = 10)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(nullable = false, length = 50)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(nullable = false, length = 50)
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

}
