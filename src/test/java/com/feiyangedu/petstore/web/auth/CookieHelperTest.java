package com.feiyangedu.petstore.web.auth;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.feiyangedu.petstore.model.PasswordAuth;
import com.feiyangedu.petstore.model.User;
import com.feiyangedu.petstore.service.UserService;
import com.feiyangedu.petstore.util.HashUtil;

public class CookieHelperTest {

	static final String AUTH_ID = "auth1001-6bc9-4247-b44c-453aa02b08aa";
	static final String AUTH_SALT = "salt1001-b508-4fc7-b3e3-4109a56dadcd";
	static final String AUTH_PASSWD = HashUtil.sha1("password");

	static final String USER_ID = "user1001-3150-4ace-b8c1-13d66bc8faa2";
	static final String USER_NAME = "Mr Cookie";
	static final String USER_EMAIL = "cookie@feiyangedu.com";

	User user;
	PasswordAuth auth;
	CookieHelper helper;

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
	}

	@Test
	public void testEncodeAndDecodeOk() {
		String str = helper.encode(auth, 600);
		User user = helper.decode(str);
		assertNotNull(user);
		assertEquals(USER_ID, user.getId());
	}

	@Test
	public void testDecodeFailedForExpired() throws Exception {
		String str = helper.encode(auth, 1);
		Thread.sleep(1200);
		User user = helper.decode(str);
		assertNull(user);
	}

	@Test
	public void testDecodeFailedForInvalidAuthId() throws Exception {
		String str = helper.encode(auth, 600);
		auth.setId(AUTH_ID.replace("auth1001", "auth9009"));
		User user = helper.decode(str.replace("auth1001", "auth9009"));
		assertNull(user);
	}

	@Test
	public void testDecodeFailedForInvalidAuthPasswd() throws Exception {
		String str = helper.encode(auth, 600);
		auth.setPasswd(HashUtil.sha1("bad-password"));
		User user = helper.decode(str);
		assertNull(user);
	}

	@Test
	public void testDecodeFailedForInvalidAuthSalt() throws Exception {
		String str = helper.encode(auth, 600);
		auth.setSalt(AUTH_SALT.replace("salt1001", "salt9001"));
		User user = helper.decode(str);
		assertNull(user);
	}

}
