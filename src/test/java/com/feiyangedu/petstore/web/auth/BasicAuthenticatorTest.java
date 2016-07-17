package com.feiyangedu.petstore.web.auth;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.feiyangedu.petstore.constant.Gender;
import com.feiyangedu.petstore.model.User;
import com.feiyangedu.petstore.service.UserService;
import com.feiyangedu.petstore.util.Base64Util;
import com.feiyangedu.petstore.util.HashUtil;

public class BasicAuthenticatorTest {

	static final String ID = UUID.randomUUID().toString();
	static final String EMAIL = "admin@feiyangedu.com";
	static final String PASSWORD = HashUtil.sha1("admin@feiyangedu.com:password");

	BasicAuthenticator authenticator;

	@Before
	public void setUp() throws Exception {
		authenticator = new BasicAuthenticator();
		authenticator.userService = new UserService() {
			@Override
			public User getAuthenticatedUser(String email, String passwd) {
				if (EMAIL.equals(email) && PASSWORD.equals(passwd)) {
					User user = new User();
					user.setEmail(EMAIL);
					user.setGender(Gender.FEMALE);
					user.setId(ID);
					user.setName("Test");
					return user;
				}
				return null;
			}
		};
	}

	@Test
	public void testBasicAuthOk() {
		String header = "Basic " + Base64Util.encodeToString(EMAIL + ":" + PASSWORD);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addHeader("Authorization", header);
		User user = authenticator.authenticate(request, new MockHttpServletResponse());
		assertNotNull(user);
		assertEquals(ID, user.getId());
		assertEquals(EMAIL, user.getEmail());
	}

	@Test
	public void testBasicAuthFailedForBadHeader1() {
		String header = "BASIC " + Base64Util.encodeToString(EMAIL + ":" + PASSWORD);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addHeader("Authorization", header);
		User user = authenticator.authenticate(request, new MockHttpServletResponse());
		assertNull(user);
	}

	@Test
	public void testBasicAuthFailedForBadHeader2() {
		String header = "Basic " + Base64Util.encodeToString(EMAIL + "-" + PASSWORD);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addHeader("Authorization", header);
		User user = authenticator.authenticate(request, new MockHttpServletResponse());
		assertNull(user);
	}

	@Test
	public void testBasicAuthFailedForBadEmail() {
		String header = "Basic " + Base64Util.encodeToString("test@example.com" + ":" + PASSWORD);
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addHeader("Authorization", header);
		User user = authenticator.authenticate(request, new MockHttpServletResponse());
		assertNull(user);
	}

	@Test
	public void testBasicAuthFailedForBadPassword() {
		String header = "Basic " + Base64Util.encodeToString(EMAIL + ":" + HashUtil.sha1("bad-password"));
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
		request.addHeader("Authorization", header);
		User user = authenticator.authenticate(request, new MockHttpServletResponse());
		assertNull(user);
	}
}
