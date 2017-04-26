package com.project1.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

public class ZLEEntityManagerTest {

	ZLEEntityManager em;
	static final String PERSISTENCE_UNIT_NAME = "zleDB-test";
	Employee emp, emp2;
	
	public ZLEEntityManagerTest() {
		em = new ZLEEntityManager(PERSISTENCE_UNIT_NAME);
		emp = new Employee("email@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		emp2 = new Employee("email2@mail.com", "firstname", "lastname", "street", "plz", "city", "tel",true);
		emp2.setIsActive(false);
	}
	
	@Before
	public void setUp(){
		
	}
	
	@After
	public void clearDB(){
		em.getAllEmployees().forEach(e->{
			em.startTransaction();
			em.removeElement(e);
		});
	}
	
	@Test
	public void persistEmployee() {
		em.persistEmployee(emp);
		assertEquals(em.getEmployee("email@mail.com"), emp);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullPersist() throws Exception{
		em.persistEmployee(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getEmployeeWithNull() throws Exception{
		em.getEmployee(null);
	}
	
	@Test
	public void getNotExistingEmployee(){
		assertNull(em.getEmployee("no.exist@mail.com"));
	}
	
	@Test
	public void getActiveEmployees(){
		em.persistEmployee(emp);
		em.persistEmployee(emp2);
		List<Employee> activeEmployees = em.getActiveEmployees();
		assertTrue(activeEmployees.size()==1);
		assertEquals(activeEmployees.get(0), emp);
	}
	
	@Test
	public void getAllEmployees(){
		em.persistEmployee(emp);
		em.persistEmployee(emp2);
		List<Employee> allEmployees = em.getAllEmployees();
		assertTrue(allEmployees.size()==2);	
		assertEquals(allEmployees.get(0), emp);
		assertEquals(allEmployees.get(1), emp2);
	}
	 
	@Test(expected = IllegalArgumentException.class)
	public void removeNull() throws Exception{
		em.removeElement(null);
	}
	
	@Test
	public void removeObject(){
		em.persistEmployee(emp);
		em.startTransaction();
		em.removeElement(emp);
		List<Employee> allEmployees = em.getAllEmployees();
		assertTrue(allEmployees.size()==0);
	}
}
