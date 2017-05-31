package com.project1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;

public class ProjectTest {
	
	Client client;
	String name;
	Project project;
	
	public ProjectTest() {
		client = new Client();
		name = "name";
	}
	
	
	@Before
	public void setUp(){
		project = new Project(name, client);
	} 
	
	@Test
	public void getId(){
		assertTrue(project.getId()>=0);
	}
	
	@Test
	public void isActive() {
		assertTrue(project.isActive());
	}
	
	@Test
	public void getName() {
		assertEquals(name, project.getName());
	}
	
	@Test
	public void getClient() {
		assertEquals(client, project.getClient());
	}
	
	@Test
	public void setActive(){
		project.setActive(false);
		assertFalse(project.isActive());
	}
	
	@Test
	public void setName(){
		project.setName("new");
		assertEquals(project.getName(), "new");
	}
	
	@Test
	public void setEmail(){
		Client newClient = new Client();
		project.setClient(newClient);
		assertEquals(project.getClient(),newClient);
	}
}
