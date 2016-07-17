package com.feiyangedu.petstore.data;

import static org.junit.Assert.*;

import java.util.UUID;

import org.hibernate.PropertyValueException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.feiyangedu.petstore.constant.Gender;
import com.feiyangedu.petstore.exception.APIEntityNotFoundException;
import com.feiyangedu.petstore.model.User;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:test-dao-context.xml")
public class HibernateDaoTest {

	@Autowired
	ApplicationContext appContext;

	@Autowired
	HibernateDao dao;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSave() {
		long t = System.currentTimeMillis();
		User user = newUser("Green");
		dao.save(user);
		// get:
		String id = user.getId();
		User u = dao.get(User.class, id);
		assertNotNull(u);
		assertEquals(id, u.getId());
		assertEquals(u.getCreatedTime(), u.getUpdatedTime());
		assertEquals(t, u.getCreatedTime(), 500);
		assertEquals(0, u.getVersion());
	}

	@Test
	public void testSaveMultiEntities() {
		User user1 = newUser("Robot");
		User user2 = newUser("Bob");
		dao.save(user1, user2);
		// get:
		User u1 = dao.load(User.class, user1.getId());
		User u2 = dao.load(User.class, user2.getId());
		assertEquals(user1.getId(), u1.getId());
		assertEquals(user2.getId(), u2.getId());
	}

	@Test(expected = PropertyValueException.class)
	public void testSaveFailedForNameIsNull() {
		User user = newUser("Alice");
		user.setName(null);
		dao.save(user);
	}

	@Test
	public void testUpdate() throws Exception {
		long t1 = System.currentTimeMillis();
		User user = newUser("Brown");
		dao.save(user);
		assertEquals(t1, user.getUpdatedTime(), 500);
		assertEquals(0, user.getVersion());
		// check:
		assertEquals("Brown", user.getName());
		assertEquals(0, user.getVersion());
		user.setName("Updated");
		user.setEmail("updated@feiyangedu.com");
		Thread.sleep(1000);
		long t2 = System.currentTimeMillis();
		dao.update(user);
		assertEquals(t2, user.getUpdatedTime(), 500);
		assertEquals(1, user.getVersion());
		// get again:
		User u2 = dao.get(User.class, user.getId());
		assertEquals("Updated", u2.getName());
		assertEquals(user.getUpdatedTime(), u2.getUpdatedTime());
		assertEquals(1, u2.getVersion());
	}

	@Test
	public void testGetNotFound() {
		User user = dao.get(User.class, UUID.randomUUID().toString());
		assertNull(user);
	}

	@Test(expected = APIEntityNotFoundException.class)
	public void testQueryNotFound() {
		dao.load(User.class, UUID.randomUUID().toString());
	}

	User newUser(String name) {
		User u = new User();
		u.setName(name);
		u.setGender(Gender.FEMALE);
		u.setEmail(name.toLowerCase() + "@feiyangedu.com");
		return u;
	}
}
