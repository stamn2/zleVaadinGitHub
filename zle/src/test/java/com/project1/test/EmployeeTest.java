package com.project1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.project1.domain.Employee;

public class EmployeeTest {
	
	Employee emp; 
	
	@Before
	public void setUp(){
		emp = new Employee("email@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
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
		emp.setPassword("newPassword");
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
		emp.setIsActive(false);
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
		assertEquals(emp.getEmail(), "email@mail.com");
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
}
