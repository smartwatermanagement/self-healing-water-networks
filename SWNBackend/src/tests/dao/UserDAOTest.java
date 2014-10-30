package tests.dao;

import static org.junit.Assert.*;
import model.User;

import org.junit.Test;

import dao.impl.UserDAO;

public class UserDAOTest {

	@Test
	public void testFindByUserNamePassword() {
		UserDAO userdao = new UserDAO();
		User user = userdao.findByUserNamePassword("Kumudini", "abc");
		assertNotEquals(user,null);
		assertEquals(user.getId(), 1);
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByUserName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsersByAggregation() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsersByIssueType() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUsersByAggregationAndIssueType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSubscriptionForUser() {
		fail("Not yet implemented");
	}

}
