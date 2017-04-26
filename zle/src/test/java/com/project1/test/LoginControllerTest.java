package com.project1.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project1.controller.LoginController;
import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

public class LoginControllerTest {
	
	ZLEEntityManager em;
	static final String PERSISTENCE_UNIT_NAME = "zleDB";
	Employee emp;
	String pw;
	
	public LoginControllerTest() {
		em = new ZLEEntityManager(PERSISTENCE_UNIT_NAME);
		emp = new Employee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
	}

	@Before
	public void setUp(){
		pw = emp.generatePassword();
		em.persistObject(emp);
	}
	
	@After
	public void clearDB(){
		emp = em.getEmployee("test@mail.com");
		em.removeElement(emp);
	}
	
	@Test
	public void login() {
		assertEquals(LoginController.login("test@mail.com", pw).getEmail(), emp.getEmail());
	}
	
	@Test
	public void loginWithWrongPw() {
		assertNull(LoginController.login("test@mail.com", "wrongPw"));
	} 
	
	@Test
	public void loginWithWrongEmail() {
		assertNull(LoginController.login("wrong@mail.com", pw));
	} 
	
	@Test
	public void getEmployee() {
		assertEquals(LoginController.getEmployee("test@mail.com").getEmail(),emp.getEmail());
	} 
	
	@Test
	public void getWrongEmployee() {
		assertNull(LoginController.getEmployee("wrong@mail.com"));
	} 
	
	/*@Test
	public void changePassword() {
		emp = em.getEmployee("test@mail.com");
		System.out.println(emp);
		//LoginController.changePassword(emp, pw, "newPassword");
		assertTrue(LoginController.changePassword(emp, pw, "newPassword"));
		assertEquals(emp.getId(),LoginController.login("test@mail.com", "newPassword").getId());
	} */
	
	@Test
	public void changePasswordWithWrongPassword() {	
		emp = em.getEmployee("test@mail.com");
		assertFalse(LoginController.changePassword(emp, "wrongPassword", "newPassword"));
	} 

}
