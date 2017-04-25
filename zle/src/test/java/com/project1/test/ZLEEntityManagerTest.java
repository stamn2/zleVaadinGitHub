package com.project1.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project1.entetyManager.ZLEEntityManager;

public class ZLEEntityManagerTest {

	ZLEEntityManager em;
	static final String PERSISTENCE_UNIT_NAME = "zleDB-test";
	
	public ZLEEntityManagerTest() {
		em = new ZLEEntityManager(PERSISTENCE_UNIT_NAME);
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
	public void test() {
		//fail("Not yet implemented");
		assertTrue(true);
	}

}
