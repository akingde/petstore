package com.feiyangedu.petstore.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.feiyangedu.petstore.constant.OrderStatus;

@Entity
public class OrderForm extends AbstractEntity {

	private Address address;
	private float price;
	private OrderStatus status;

	@ManyToOne
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
