package com.project1.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.project1.controller.LoginController;
import com.project1.controller.UserController;
import com.project1.entetyManager.ZLEEntityManager;

public class UserControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	
	@Test
	public void addEmployee() {
		System.out.println(UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true));
		String pw ="newPassword123";
		
		zem.getEmployee("test@mail.com").setPassword(pw);
		
		zem.persistObject(zem.getEmployee("test@mail.com"));
		
		System.out.println(zem.getEmployee("test@mail.com").checkPassword(pw));
		System.out.println(LoginController.login("test@mail.com", pw));
		zem.removeElement(zem.getEmployee("test@mail.com"));
	}
 
}
