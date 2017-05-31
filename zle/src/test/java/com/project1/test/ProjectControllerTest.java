package com.project1.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.project1.controller.ClientController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project1.controller.ProjectController;
import com.project1.controller.RecordController;
import com.project1.controller.UserController;
import com.project1.domain.Activity;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

public class ProjectControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	Client client,client2;
	Project project, project2,project3;
	Employee employee;
	ProjectCommitment projectCommitment, projectCommitment2;
	double hourlyRate;
	Activity activity;
	
	public ProjectControllerTest() {
		hourlyRate = 1;
		}
	
	@Before
	public void init(){
		client = ClientController.addClient("companyName", "firstname", "lastname", "street", "plz",
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
	
	@Test
	public void getArchievedProjects(){
		project3 = ProjectController.addProject("archieved", client);
		ProjectController.endProject(project3);
		assertTrue(project3.getId()==ProjectController.getArchivedProjects().get(ProjectController.getArchivedProjects().size()-1).getId());
		project3 = (Project) zem.findObject(Project.class, project3.getId());
		zem.removeElement(project3);
	}
	
	@Test
	public void updateProject(){
		ProjectController.updateProject(project, "new", client);
		assertEquals("new", ProjectController.getProject(project.getId()).getName());
		}
	
	@Test
	public void getActivities(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		assertTrue(ProjectController.getActivitiesFromProject(project.getId()).size()==1);
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
	
	@Test
	public void getOldestActivities(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		assertTrue(ProjectController.getOldestActiviteFromProject(project.getId()).getId()==activity.getId());
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
	
	@Test
	public void getLastActivities(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		assertTrue(ProjectController.getLastActiviteFromProject(project.getId()).getId()==activity.getId());
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
	
	@Test //TODO change month!!!
	public void getMonthlyActivities(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		assertTrue(ProjectController.getMonthlyActivitiesFromProject(project.getId(), 5, 2017).size()==1);
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
	
	@Test //TODO change month!!!
	public void inactivateMonthlyActivities(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		ProjectController.inactiveMonthlyActivitiesFromProject(ProjectController.getMonthlyActivitiesFromProject(project.getId(), 5, 2017));
		assertFalse(ProjectController.getActivitiesFromProject(project.getId()).get(ProjectController.getActivitiesFromProject(project.getId()).size()-1).isActive());
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
}
