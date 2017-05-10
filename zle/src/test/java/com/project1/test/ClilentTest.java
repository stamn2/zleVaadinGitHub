package com.project1.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.project1.domain.Client;
import com.project1.domain.Employee;

public class ClilentTest {
	
	Client client; 
	
	@Before
	public void setUp(){
		client = new Client("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
	} 
	
	@Test
	public void getId(){
		assertTrue(client.getId()>=0);
	}
	
	@Test
	public void getCompanyName(){
		assertEquals(client.getCompanyName(), "companyName");
	}
	
	@Test
	public void getFirstName(){
		assertEquals(client.getFirstname(), "firstname");
	}
	
	@Test
	public void getLastName(){
		assertEquals(client.getLastname(), "lastname");
	}
	
	@Test
	public void getStreet(){
		assertEquals(client.getStreet(), "street");
	}
	
	@Test
	public void getPlz(){
		assertEquals(client.getPlz(), "plz");
	}
	
	@Test
	public void getCity(){
		assertEquals(client.getCity(), "city");
	}
	
	@Test
	public void getEmail(){
		assertEquals(client.getEmail(), "email");
	}
	
	@Test
	public void getTel(){
		assertEquals(client.getTel(), "tel");
	}
}
