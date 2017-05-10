package com.project1.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;

public class ProjectCommitmentTest {
	

	ProjectCommitment projectCommitment;
	Employee employee, employee2;
	Project project, project2;
	double hourlyRate, hourlyRate2;
	
	public ProjectCommitmentTest() {
		employee = new Employee();
		project = new Project();
		hourlyRate = 1;
	}
	
	@Before
	public void setUp(){
		projectCommitment = new ProjectCommitment(project, employee, hourlyRate);
		
	} 
	
	@Test
	public void getId(){
		assertTrue(projectCommitment.getId()>=0);
	}
	
	@Test
	public void isActive() {
		assertTrue(projectCommitment.isActive());
	}
	
	@Test
	public void setActive() {
		projectCommitment.setActive(false);
		assertFalse(projectCommitment.isActive());
	}
	
	@Test
	public void getHourlyRate() {
		assertTrue(hourlyRate == projectCommitment.getHourlyRate());
	}
	
	@Test
	public void setHourlyRaten() {
		hourlyRate2=2;
		projectCommitment.setHourlyRate(hourlyRate2);
		assertTrue(hourlyRate2 == projectCommitment.getHourlyRate());
	}
	
	@Test
	public void getProject() {
		assertEquals(project, projectCommitment.getProject());
	}
	
	@Test
	public void setProject() {
		project2 = new Project();
		projectCommitment.setProject(project2);
		assertEquals(project2, projectCommitment.getProject());
	}
	
	@Test
	public void getEmployee() {
		assertEquals(employee, projectCommitment.getEmployee());
	}
	
	@Test
	public void setEmployee() {
		employee2 = new Employee();
		projectCommitment.setEmployee(employee2);
		assertEquals(employee2, projectCommitment.getEmployee());
	}
}
