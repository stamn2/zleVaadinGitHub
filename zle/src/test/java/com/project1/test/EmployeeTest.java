package com.project1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.project1.domain.Employee;

public class EmployeeTest {
	
	Employee emp; 
	
	@Before
	public void setUp(){
		emp = new Employee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
	} 

	@Test
	public void generatePassword() {
		String pw = emp.generatePassword();
		String pw2 = emp.generatePassword();
		assertNotEquals(pw2, pw);
		assertTrue(pw.length()==12);
	}
	
	@Test
	public void checkPassword(){
		String pw = emp.generatePassword();
		assertTrue(emp.checkPassword(pw));
	}
	
	@Test
	public void isChangePassword(){
		assertTrue(emp.isChangePassword());
	}
	
	@Test
	public void passwordChanged(){
		emp.setChangePassword(false);
		assertFalse(emp.isChangePassword());
	}
	
	@Test
	public void setPassword(){
		emp.hashAndSetPassword("newPassword");
		assertTrue(emp.checkPassword("newPassword"));
	}
	
	@Test
	public void getId(){
		assertTrue(emp.getId()>=0);
	}
	
	@Test
	public void isActive(){
		assertTrue(emp.isActive());
	}
	
	@Test
	public void isInactive(){
		emp.setActive(false);
		assertFalse(emp.isActive());
	}
	
	
	@Test
	public void isAdmin(){
		assertTrue(emp.isAdmin());
	}
	
	@Test
	public void isNotAdmin(){
		emp.setIsAdmin(false);
		assertFalse(emp.isAdmin());
	}
	
	@Test
	public void getEmail(){
		assertEquals(emp.getEmail(), "test");
	}
	
	@Test
	public void getFirstName(){
		assertEquals(emp.getFirstname(), "firstname");
	}
	
	@Test
	public void getLastName(){
		assertEquals(emp.getLastname(), "lastname");
	}
	
	@Test
	public void getStreet(){
		assertEquals(emp.getStreet(), "street");
	}
	
	@Test
	public void getPlz(){
		assertEquals(emp.getPlz(), "plz");
	}
	
	@Test
	public void getCity(){
		assertEquals(emp.getCity(), "city");
	}
	
	@Test
	public void getTel(){
		assertEquals(emp.getTel(), "tel");
	}
	
	@Test
	public void setEmail(){
		emp.setEmail("new");
		assertEquals(emp.getEmail(), "new");
	}
	
	@Test
	public void setFirstName(){
		emp.setFirstname("new");
		assertEquals(emp.getFirstname(), "new");
	}
	
	@Test
	public void setLastName(){
		emp.setLastname("new");
		assertEquals(emp.getLastname(), "new");
	}
	
	@Test
	public void setStreet(){
		emp.setStreet("new");
		assertEquals(emp.getStreet(), "new");
	}
	
	@Test
	public void setPlz(){
		emp.setPlz("new");
		assertEquals(emp.getPlz(), "new");
	}
	
	@Test
	public void setCity(){
		emp.setCity("new");
		assertEquals(emp.getCity(), "new");
	}
	
	@Test
	public void setTel(){
		emp.setTel("new");
		assertEquals(emp.getTel(), "new");
	}
}
