package com.project1.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.project1.controller.UserController;
import com.project1.entetyManager.ZLEEntityManager;

public class UserControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	
	@Test
	public void addEmployee() {
		UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		System.out.println(zem.getEmployee("test@mail.com").getId());
		
		zem.removeElement(zem.getEmployee("test@mail.com"));
	}
 
}
