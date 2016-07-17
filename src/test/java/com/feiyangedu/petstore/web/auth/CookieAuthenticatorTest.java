package com.feiyangedu.petstore.web.auth;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.feiyangedu.petstore.model.PasswordAuth;
import com.feiyangedu.petstore.model.User;
import com.feiyangedu.petstore.service.UserService;
import com.feiyangedu.petstore.util.Base64Util;
import com.feiyangedu.petstore.util.HashUtil;

public class CookieAuthenticatorTest {

	static final String AUTH_ID = "auth1001-6bc9-4247-b44c-453aa02b08aa";
	static final String AUTH_SALT = "salt1001-b508-4fc7-b3e3-4109a56dadcd";
	static final String AUTH_PASSWD = HashUtil.sha1("password");

	static final String USER_ID = "user1001-3150-4ace-b8c1-13d66bc8faa2";
	static final String USER_NAME = "Mr Cookie";
	static final String USER_EMAIL = "cookie@feiyangedu.com";

	User user;
	PasswordAuth auth;
	CookieHelper helper;
	CookieAuthenticator authenticator;

	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setId(USER_ID);
		user.setName(USER_NAME);
		user.setEmail(USER_EMAIL);

		auth = new PasswordAuth();
		auth.setId(AUTH_ID);
		auth.setPasswd(AUTH_PASSWD);
		auth.setSalt(AUTH_SALT);
		auth.setUser(user);

		helper = new CookieHelper();
		helper.userService = new UserService() {
			@Override
			public PasswordAuth getPasswordAuth(String id) {
				if (AUTH_ID.equals(id)) {
					return auth;
				}
				return null;
			}
		};

		authenticator = new CookieAuthenticator();
		authenticator.cookieHelper = helper;
	}

	@Test
	public void testAuthOk() {
		String str = helper.encode(auth, 60_000);
		Cookie cookie = new Cookie(CookieHelper.SESSION_COOKIE_NAME, Base64Util.urlEncodeToString(str));
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(cookie);
		User user = authenticator.authenticate(request, new MockHttpServletResponse());
		assertNotNull(user);
		assertEquals(USER_ID, user.getId());
	}

	@Test
	public void testSetCookieAndAuthOk() {
		// set cookie to response:
		MockHttpServletResponse response = new MockHttpServletResponse();
		String str = helper.encode(auth, 60_000);
		helper.setSessionCookie(response, str);
		Cookie sessionCookie = response.getCookie(CookieHelper.SESSION_COOKIE_NAME);
		assertNotNull(sessionCookie);
		// auth by cookie:
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(new Cookie(CookieHelper.SESSION_COOKIE_NAME, sessionCookie.getValue()));
		User user = authenticator.authenticate(request, new MockHttpServletResponse());
		assertNotNull(user);
		assertEquals(USER_ID, user.getId());
	}

	@Test
	public void testAuthFailedForNoCookieFound() {
		User user = authenticator.authenticate(new MockHttpServletRequest(), new MockHttpServletResponse());
		assertNull(user);
	}

	@Test
	public void testAuthFailedForCookieExpired() throws Exception {
		String str = helper.encode(auth, 1);
		Cookie cookie = new Cookie(CookieHelper.SESSION_COOKIE_NAME, Base64Util.urlEncodeToString(str));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setCookies(cookie);
		Thread.sleep(1200);
		User user = authenticator.authenticate(request, response);
		assertNull(user);
		Cookie respCookie = response.getCookie(CookieHelper.SESSION_COOKIE_NAME);
		assertNotNull(respCookie);
		assertEquals("-deleted-", respCookie.getValue());
		assertEquals(0, respCookie.getMaxAge());
	}

}
