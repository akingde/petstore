package com.feiyangedu.petstore.web.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.feiyangedu.petstore.model.PasswordAuth;
import com.feiyangedu.petstore.model.User;
import com.feiyangedu.petstore.service.UserService;
import com.feiyangedu.petstore.util.Base64Util;
import com.feiyangedu.petstore.util.HashUtil;

@Component
public class CookieHelper {

	final Log log = LogFactory.getLog(getClass());

	String sessionCookieName = "thesession";
	int sessionCookieExpires = 24 * 3600;

	@Autowired
	UserService userService;

	/**
	 * Encode value as String that composed by: auth.id : expires : sha1(auth.id
	 * : expires : auth.salt : auth.password)
	 * 
	 * @param auth
	 * @return
	 */
	public String encode(PasswordAuth auth, int expiresInSeconds) {
		long expiresTime = expiresInSeconds * 1000L + System.currentTimeMillis();
		String strToSign = stringToSign(auth, expiresTime);
		String sha1 = HashUtil.sha1(strToSign);
		return auth.getId() + ":" + expiresTime + ":" + sha1;
	}

	public User decode(String str) {
		String[] ss = str.split("\\:", 3);
		if (ss.length != 3) {
			return null;
		}
		String theId = ss[0];
		String expires = ss[1];
		String sha1 = ss[2];
		long expiresTime = 0;
		if (theId.isEmpty()) {
			log.info("Invalid auth id: " + str);
			return null;
		}
		if (sha1.length() != 40) {
			log.info("Invalid sha1: " + str);
			return null;
		}
		try {
			expiresTime = Long.parseLong(expires);
		} catch (NumberFormatException e) {
			log.info("Invalid expires: " + str);
			return null;
		}
		if (expiresTime < System.currentTimeMillis()) {
			log.info("token expires: " + str);
			return null;
		}
		PasswordAuth pauth = userService.getPasswordAuth(theId);
		if (pauth == null) {
			log.info("PasswordAuth not found by id: " + str);
			return null;
		}
		String strToSign = stringToSign(pauth, expiresTime);
		String expectedSha1 = HashUtil.sha1(strToSign);
		if (!expectedSha1.equals(sha1)) {
			log.info("Invalid sha1: " + str);
			return null;
		}
		return pauth.getUser();
	}

	/**
	 * Get session cookie value, or null if not found.
	 */
	String getSessionCookieValue(HttpServletRequest request, String sessionCookieName) {
		Cookie[] cs = request.getCookies();
		if (cs == null) {
			return null;
		}
		for (Cookie c : cs) {
			if (sessionCookieName.equals(c.getName())) {
				try {
					return Base64Util.urlDecodeToString(c.getValue());
				} catch (IllegalArgumentException e) {
					return null;
				}
			}
		}
		return null;
	}

	public void setSessionCookie(HttpServletResponse response, String sessionCookieValue) {
		String encodedValue = Base64Util.urlEncodeToString(sessionCookieValue);
		_setCookie(response, encodedValue, this.sessionCookieExpires);
	}

	public void deleteSessionCookie(HttpServletResponse response) {
		_setCookie(response, "-deleted-", 0);
	}

	void _setCookie(HttpServletResponse response, String value, int expiry) {
		Cookie cookie = new Cookie(sessionCookieName, value);
		cookie.setMaxAge(expiry);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	String stringToSign(PasswordAuth auth, long expiresTime) {
		return auth.getId() + ":" + expiresTime + ":" + auth.getSalt() + ":" + auth.getPasswd();
	}
}
