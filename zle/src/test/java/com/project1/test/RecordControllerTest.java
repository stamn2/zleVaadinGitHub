package com.project1.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import com.project1.controller.ClientController;
import org.eclipse.persistence.sessions.Record;
import org.junit.After;
import org.junit.AfterClass;
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

public class RecordControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	
	static Client client;
	static Project project;
	double hourlyRate;
	Employee employee;
	ProjectCommitment projectCommitment,noProject;
	Activity activity, activity2, activity3;
	
	public RecordControllerTest() {
		double hourlyRate = 1;
	}
	
	@Before
	public void init(){
		client = ClientController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		project = ProjectController.addProject("name", client);
		UserController.addEmployee("test", "firstname", "lastname", "street", "plz", "city", "tel",true);
		employee = zem.getEmployee("test");
		projectCommitment = ProjectController.addProjectCommitment(project, employee, hourlyRate);
		activity = RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
	}
	
	@After
	public void clear(){
		RecordController.removeActivity(activity);
		//No-Project&RealTimeActivityProject
		List<Project> systemProject = zem.getSystemProjects();
		for (Project project : systemProject) {
			zem.removeElement(zem.getProjectCommitmentWithProjectAndEmployee(employee.getId(), project.getId()));
		}
		projectCommitment = (ProjectCommitment) zem.findObject(ProjectCommitment.class, projectCommitment.getId());
		zem.removeElement(projectCommitment);
		project = (Project) zem.findObject(Project.class, project.getId());
		zem.removeElement(project);
		client = (Client) zem.findObject(Client.class, client.getId());
		zem.removeElement(client);
		zem.removeElement(employee);
	}
	
	@Test
	public void getProjectCommitmentListFromEmployee() {
		assertTrue(RecordController.
				getProjectCommitmentListFromEmployee(employee.getId()).size()>=1);
	}
	
	@Test
	public void addAndGetActivity() {
		activity2 = RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		assertTrue(activity2.getId() == RecordController.getActivity(activity2.getId()).getId());
		RecordController.removeActivity(activity2);
	}
	
	@Test
	public void getActivitiesFromEmployee(){
		assertTrue(RecordController.getActivitiesFromEmployee(employee.getId()).size()>=1);
	}
	
	@Test
	public void updateActivity(){
		String newComment = "newComment";
		Date newBegin = new Date();
		Date newEnd = new Date();
		RecordController.updateActivity(activity, newBegin, newEnd, newComment, projectCommitment);
		assertEquals(newComment, RecordController.getActivity(activity.getId()).getComment());
		assertEquals(newBegin, RecordController.getActivity(activity.getId()).getBeginDate());
		assertEquals(newEnd, RecordController.getActivity(activity.getId()).getEndDate());
	}
	
	@Test
	public void removeActivity(){
		activity3 = RecordController.addActivity(new Date(), new Date(), "comment", projectCommitment);
		RecordController.removeActivity(activity3);
	}
	
	@Test
	public void startAndGetRealTimeRecordActivity(){
		activity3 = RecordController.startRealTimeRecording(employee);
		assertTrue(activity3.getId() == RecordController.getRealTimeRecordActivity(employee.getId()).getId());
		RecordController.removeActivity(activity3);
	}
	
	@Test
	public void stopRealTimeRecordActivity(){
		activity3 = RecordController.startRealTimeRecording(employee);
		assertFalse(RecordController.stopRealTimeRecording(employee).isRealTimeRecord());	
		RecordController.removeActivity(activity3);
	}
	
	@Test
	public void getNoRealTimeRecord(){
		assertNull(RecordController.getRealTimeRecordActivity(employee.getId()));
	}
}
