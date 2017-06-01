package com.project1.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import com.project1.domain.Cost;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

public class ProjectControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	Client client,client2;
	Project project, project2;
	Employee employee;
	ProjectCommitment projectCommitment;
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
		employee=UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
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
		ProjectController.endProject(project);
		assertTrue(project.getId()==ProjectController.getArchivedProjects().get(ProjectController.getArchivedProjects().size()-1).getId());
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
		assertTrue(ProjectController.getMonthlyActivitiesFromProject(project.getId(), 6, 2017).size()==1);
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
	
	@Test //TODO change month!!!
	public void inactivateMonthlyActivities(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		ProjectController.inactiveMonthlyActivitiesFromProject(ProjectController.getMonthlyActivitiesFromProject(project.getId(), 6, 2017));
		assertFalse(ProjectController.getActivitiesFromProject(project.getId()).get(ProjectController.getActivitiesFromProject(project.getId()).size()-1).isActive());
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
	@Test
	public void enableProject(){
		ProjectController.endProject(project);
		ProjectController.enableProject(project);
		assertTrue(project.isActive());
	}
	
	@Test
	public void inactivateProjectCommitment(){
		ProjectController.inactivateProjectCommitment(projectCommitment);
		assertFalse(projectCommitment.isActive());
	}
	
	@Test
	public void addAndGetCosts(){
		Cost cost = ProjectController.addCost("test", new Date(), 1, project, "test");
		assertTrue(ProjectController.getCosts(project.getId()).size()==1);
		zem.removeElement(zem.findObject(Cost.class, cost.getId()));
	}
	
	@Test
	public void removeCosts(){
		Cost cost = ProjectController.addCost("test", new Date(), 1, project, "test");
		ProjectController.removeCost(cost);
		assertTrue(ProjectController.getCosts(project.getId()).size()==0);
	}
	
	@Test
	public void getMonthlyCosts(){
		Cost cost = ProjectController.addCost("test", new Date(), 1, project, "test");
		assertTrue(ProjectController.getMonthlyCosts(project.getId(), 6, 2017).size()==1);
		zem.removeElement(zem.findObject(Cost.class, cost.getId()));
	}
	
	@Test
	public void getSumCosts(){
		Cost cost = ProjectController.addCost("test", new Date(), 1, project, "test");
		Cost cost2 = ProjectController.addCost("test", new Date(), 1, project, "test");
		assertTrue(ProjectController.getSumCosts(ProjectController.getCosts(project.getId()))==2);
		zem.removeElement(zem.findObject(Cost.class, cost.getId()));
		zem.removeElement(zem.findObject(Cost.class, cost2.getId()));
	}
	
	@Test
	public void getBillingEmployeeItemsAndSum(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		assertTrue(ProjectController.getBillingEmployeeItems(
						ProjectController.getActivitiesFromProject(project.getId())).size()==1);
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
	
	@Test
	public void getBillingEmployeeItemsIfEmpty(){
		assertTrue(ProjectController.getBillingEmployeeItems(
						ProjectController.getActivitiesFromProject(project.getId())).size()==0);
		}
	
	@Test
	public void getBillingEmployeeItemsIfDifferentProjectCommitments(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		
		project2 = ProjectController.addProject("new", client);
		ProjectCommitment pc2 = ProjectController.addProjectCommitment(project2, employee, hourlyRate);
		Activity activity2 = RecordController.addActivity(new Date(), new Date(), "comment", pc2);
		
		List<Activity> listWithTwoProjectCommitments = new ArrayList<>();
		listWithTwoProjectCommitments.addAll(ProjectController.getActivitiesFromProject(project.getId()));
		listWithTwoProjectCommitments.addAll(ProjectController.getActivitiesFromProject(project2.getId()));
		assertTrue(ProjectController.getBillingEmployeeItems(listWithTwoProjectCommitments).size()==2);
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		zem.removeElement(zem.findObject(Activity.class, activity2.getId()));
		pc2 = (ProjectCommitment) zem.findObject(ProjectCommitment.class, pc2.getId());
		zem.removeElement(pc2);
		project2 = (Project) zem.findObject(Project.class, project2.getId());
		zem.removeElement(project2);
		}
	
	@Test
	public void getSumBillingItems(){
		activity=RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		assertTrue(
				ProjectController.getSumBillingEmployeeItems(
						ProjectController.getBillingEmployeeItems(
								ProjectController.getActivitiesFromProject(project.getId())))==0);
		zem.removeElement(zem.findObject(Activity.class, activity.getId()));
		}
}
