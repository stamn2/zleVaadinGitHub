package com.project1.entetyManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.project1.domain.Employee;

public class ZLEEntityManager {
	
	private static final String PERSISTENCE_UNIT_NAME = "zleDB";
    private static EntityManagerFactory factory; 
    private EntityManager em;
	
	public ZLEEntityManager() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    em = factory.createEntityManager();
	}

	void addEmployee(Employee e){
		if(e!=null){
		    em.getTransaction().begin();
		    em.persist(e);
		    em.getTransaction().commit();
		}
		
	}
}
