package com.project1.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import com.project1.controller.ProjectController;
import com.project1.controller.UserController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

public class ProjectControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	

	@After
	public void clear(){
	}
	
	@Test
	public void addAndGetClient() {
		ProjectController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		assertTrue(ProjectController.getClients().size()>=1);
	}
	
	@Test
	public void addAndGetProject() {
		Client client = new Client("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		Project project = ProjectController.addProject("name", client);
		assertTrue(project.getId() == ProjectController.getProject(project.getId()).getId());
	}
	
	@Test
	public void addAndGetProjectCommitment() {
		Client client = new Client("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		Project project = ProjectController.addProject("name", client);
		double hourlyRate = 1;
		Employee employee = new Employee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		ProjectCommitment projectCommitment = ProjectController.addProjectCommitment(project, employee, hourlyRate);
		assertTrue(projectCommitment.getId() == ProjectController.getProjectCommitment(projectCommitment.getId()).getId());
		assertTrue(ProjectController.getProjectCommitmentList(project.getId()).size()>=1);
	}
 
}
