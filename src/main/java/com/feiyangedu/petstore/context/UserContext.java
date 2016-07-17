package com.feiyangedu.petstore.context;

import com.feiyangedu.petstore.model.User;

public class UserContext implements AutoCloseable {

	static final ThreadLocal<User> current = new ThreadLocal<User>();

	public UserContext(User user) {
		current.set(user);
	}

	/**
	 * Return current user, or throw exception if no user binded.
	 * 
	 * @return User object.
	 */
	public static User getRequiredCurrentUser() {
		User user = current.get();
		if (user == null) {
			throw new MissingContextException();
		}
		return user;
	}

	/**
	 * Return current user, or null if no user binded.
	 * 
	 * @return User object or null.
	 */
	public static User getCurrentUser() {
		return current.get();
	}

	@Override
	public void close() {
		current.remove();
	}

}