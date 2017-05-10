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
	
	
	@Test
	public void addAndGetClient() {
		Client client = ProjectController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		assertTrue(ProjectController.getClients().size()>=1);
		client = (Client) zem.findObject(Client.class, client.getId());
		zem.removeElement(client);
	}
	
	@Test
	public void addAndGetProject() {
		Client client = ProjectController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		Project project = ProjectController.addProject("name", client);
		assertTrue(project.getId() == ProjectController.getProject(project.getId()).getId());
		project = (Project) zem.findObject(Project.class, project.getId());
		zem.removeElement(project);
		client = (Client) zem.findObject(Client.class, client.getId());
		zem.removeElement(client);
	}
	
	@Test
	public void addAndGetProjectCommitment() {
		Client client = ProjectController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		Project project = ProjectController.addProject("name", client);
		double hourlyRate = 1;
		UserController.addEmployee("test@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		Employee employee = zem.getEmployee("test@mail.com");
		ProjectCommitment projectCommitment = ProjectController.addProjectCommitment(project, employee, hourlyRate);
		assertTrue(projectCommitment.getId() == ProjectController.getProjectCommitment(projectCommitment.getId()).getId());
		assertTrue(ProjectController.getProjectCommitmentList(project.getId()).size()>=1);
		projectCommitment = (ProjectCommitment) zem.findObject(ProjectCommitment.class, projectCommitment.getId());
		zem.removeElement(projectCommitment);
		project = (Project) zem.findObject(Project.class, project.getId());
		zem.removeElement(project);
		client = (Client) zem.findObject(Client.class, client.getId());
		zem.removeElement(client);
		zem.removeElement(employee);
	}
	
	@Test
	public void getProjects(){
		Client client = ProjectController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		Project project = ProjectController.addProject("name", client);
		assertTrue(ProjectController.getProjects().size()>=1);
		project = (Project) zem.findObject(Project.class, project.getId());
		zem.removeElement(project);
		client = (Client) zem.findObject(Client.class, client.getId());
		zem.removeElement(client);
	}
 
}
