package com.project1.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project1.controller.UserController;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.entetyManager.ZLEEntityManager;

public class UserControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	
	Employee emp;

	@After
	public void clear(){
		//No-Project&RealTimeActivityProject
		List<Project> systemProject = zem.getSystemProjects();
		for (Project project : systemProject) {
			zem.removeElement(zem.getProjectCommitmentWithProjectAndEmployee(zem.getEmployee("test").getId(), project.getId()));
		}
		zem.removeElement(zem.getEmployee("test"));
	}
	
	@Test
	public void addEmployee() {
		assertTrue(UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true) != null);
	}
	
	@Test
	public void addSameEmployee() {
		UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		assertFalse(UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true) != null);
	}
	
	@Test
	public void getActiveEmployees() {
		UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		assertTrue(UserController.getActivesEmployees().size()>=1);
	}
	
	@Test
	public void generateNewPassword() {
		UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		zem.getEmployee("test").setChangePassword(false);
		UserController.generatedNewPassword(zem.getEmployee("test"));
		assertTrue(zem.getEmployee("test").isChangePassword());
	}
	
	
	@Test
	public void disableEmployee() {
		emp = UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		UserController.disableEmployeeAccount(emp);
		assertTrue(UserController.getInactivesEmployees().get(UserController.getInactivesEmployees().size()-1).getId()==emp.getId());
		UserController.enableEmployeeAccount(emp);
	}
	
	
	@Test
	public void enableEmployee() {
		emp = UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		UserController.disableEmployeeAccount(emp);
		UserController.enableEmployeeAccount(emp);
		assertTrue(zem.getEmployee("test").isActive());
	}
	
	@Test
	public void updateEmploydee() {
		emp = UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		UserController.updateEmployee(emp, "test", "new", "new", "new", "new", "new","new", true);
		assertEquals("new", zem.getEmployee("test").getFirstname());
	}
	
	@Test
	public void getEmployeeById() {
		emp = UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		assertTrue(UserController.getEmployee(emp.getId()).getId()==emp.getId());
	}
	
	@Test
	public void enableNonExistingEmployee() {
		emp = UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		assertFalse(UserController.enableEmployeeAccount(emp));
	}
 
}
