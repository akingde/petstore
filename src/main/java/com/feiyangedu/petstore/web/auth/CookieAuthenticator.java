package com.feiyangedu.petstore.web.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.feiyangedu.petstore.model.User;

/**
 * Authenticate by cookie.
 */
@Component
@Order(30)
public class CookieAuthenticator implements Authenticator {

	final Log log = LogFactory.getLog(getClass());

	@Autowired
	CookieHelper cookieHelper;

	@Override
	public User authenticate(HttpServletRequest request, HttpServletResponse response) {
		String cookieValue = cookieHelper.getSessionCookieValue(request, CookieHelper.SESSION_COOKIE_NAME);
		if (cookieValue == null) {
			return null;
		}
		User user = cookieHelper.decode(cookieValue);
		if (user == null) {
			cookieHelper.deleteSessionCookie(response);
			return null;
		}
		return user;
	}

}
