package com.feiyangedu.petstore.web.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.feiyangedu.petstore.model.User;
import com.feiyangedu.petstore.service.UserService;
import com.feiyangedu.petstore.util.Base64Util;

/**
 * Authenticate by header authorization: Basic base64encoded-string
 */
@Component
@Order(10)
public class BasicAuthenticator implements Authenticator {

	final Log log = LogFactory.getLog(getClass());

	@Autowired
	UserService userService;

	@Override
	public User authenticate(HttpServletRequest request, HttpServletResponse response) {
		String hdr = request.getHeader("Authorization");
		if (hdr == null || !hdr.startsWith("Basic ")) {
			return null;
		}
		String userAndPwd = Base64Util.decodeToString(hdr.substring(6));
		String[] ss = userAndPwd.split(":", 2);
		if (ss.length != 2) {
			log.warn("Bad Authorization header: " + hdr + ", decoded: " + userAndPwd);
			return null;
		}
		String email = ss[0];
		String passwd = ss[1];
		return userService.getAuthenticatedUser(email, passwd);
	}

}
