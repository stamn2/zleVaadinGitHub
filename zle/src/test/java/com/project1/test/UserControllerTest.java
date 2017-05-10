package com.project1.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import com.project1.controller.UserController;
import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

public class UserControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	

	@After
	public void clear(){
		zem.removeElement(zem.getEmployee("test@mail.com"));
	}
	
	@Test
	public void addEmployee() {
		assertTrue(UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true));
	}
	
	@Test
	public void addSameEmployee() {
		UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		assertFalse(UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true));
	}
	
	@Test
	public void getActiveEmployees() {
		UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		assertTrue(UserController.getActivesEmployees().size()>=1);
	}
	
	@Test
	public void generateNewPassword() {
		UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		zem.getEmployee("test@mail.com").setChangePassword(false);
		UserController.generatedNewPassword(zem.getEmployee("test@mail.com"));
		assertTrue(zem.getEmployee("test@mail.com").isChangePassword());
	}
 
}
