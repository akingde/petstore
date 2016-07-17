package com.feiyangedu.petstore.exception;

public class APINonUniqueEntityException extends APIException {

	public APINonUniqueEntityException(String name, String message) {
		super("entity:nonunique", name, message);
	}

	public APINonUniqueEntityException(Class<?> clazz, String message) {
		super("entity:nonunique", clazz.getSimpleName(), message);
	}

	public APINonUniqueEntityException(Class<?> clazz) {
		super("entity:nonunique", clazz.getSimpleName(), "Too many entities.");
	}

}
