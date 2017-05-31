package com.project1.test;

import static org.junit.Assert.*;

import java.util.List;

import com.project1.controller.ClientController;
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

public class ClientControllerTest {
	private static ZLEEntityManager zem = new ZLEEntityManager();
	Client client,client2;
	
	public ClientControllerTest() {
		}
	
	@Before
	public void init(){
		client = ClientController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
	}
	
	@After
	public void clear(){
		client = (Client) zem.findObject(Client.class, client.getId());
		zem.removeElement(client);
	}
	
	@Test
	public void addAndGetClient() {
		client2 = ClientController.addClient("companyName", "firstname", "lastname", "street", "plz",
				"city", "email", "tel");
		assertTrue(ClientController.getClients().size()>=2);
		client2 = (Client) zem.findObject(Client.class, client2.getId());
		zem.removeElement(client2);
	}
	
	@Test
	public void updateClientAndGetClientById() {
		ClientController.updateClient(client, "new", "new", "new", "new", "new", "new", "new", "new");
		assertEquals("new",ClientController.getClient(client.getId()).getEmail());
	}
}
