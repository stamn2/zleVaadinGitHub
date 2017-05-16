package com.project1.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
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
	Client client,client2;
	Project project, project2;
	Employee employee;
	ProjectCommitment projectCommitment, projectCommitment2;
	double hourlyRate;
	
	public ProjectControllerTest() {
		hourlyRate = 1;
		}
	
	@Before
	public void init(){
		client = ProjectController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		project = ProjectController.addProject("name", client);	
		UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		employee = zem.getEmployee("test");
		projectCommitment = ProjectController.addProjectCommitment(project, employee, hourlyRate);
	}
	
	@After
	public void clear(){
		//No-Project&RealTimeActivityProject
		List<Project> systemProject = zem.getSystemProjects();
		for (Project project : systemProject) {
			zem.removeElement(zem.getProjectCommitmentWithProjectAndEmployee(zem.getEmployee("test").getId(), project.getId()));
		}
		projectCommitment = (ProjectCommitment) zem.findObject(ProjectCommitment.class, projectCommitment.getId());
		zem.removeElement(projectCommitment);
		project = (Project) zem.findObject(Project.class, project.getId());
		zem.removeElement(project);
		client = (Client) zem.findObject(Client.class, client.getId());
		zem.removeElement(client);
		employee = zem.getEmployee("test");
		zem.removeElement(employee);
	}
	
	@Test
	public void addAndGetClient() {
		client2 = ProjectController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		assertTrue(ProjectController.getClients().size()>=1);
		client2 = (Client) zem.findObject(Client.class, client2.getId());
		zem.removeElement(client2);
	}
	
	@Test
	public void addAndGetProject() {
		project2 = ProjectController.addProject("name", client);
		assertTrue(project2.getId() == ProjectController.getProject(project2.getId()).getId());
		project2 = (Project) zem.findObject(Project.class, project2.getId());
		zem.removeElement(project2);
	}
	
	@Test
	public void addAndGetProjectCommitment() {
		assertTrue(projectCommitment.getId() == ProjectController.getProjectCommitment(projectCommitment.getId()).getId());
		assertTrue(ProjectController.getProjectCommitmentList(project.getId()).size()>=1);
	}
	
	@Test
	public void addTwoTimesSameProjectCommitment() {
		assertNull(ProjectController.addProjectCommitment(project, employee, hourlyRate));
	}
	
	@Test
	public void getProjects(){
		assertTrue(ProjectController.getProjects().size()>=1);
	}
	
	@Test
	public void getNullProjectcommitmentWithProjectAndEmployee(){
		long wrongProjectID = 999;
		assertNull(zem.getProjectCommitmentWithProjectAndEmployee(employee.getId(), wrongProjectID));
	}
}
