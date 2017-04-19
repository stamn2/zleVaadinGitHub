package com.project1.entetyManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.project1.domain.Employee;


public class ZLEEntityManager {
	
	private static final String PERSISTENCE_UNIT_NAME = "zleDB";
    private static EntityManagerFactory factory; 
    private EntityManager em;
	
	public ZLEEntityManager() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    em = factory.createEntityManager();
	}

	public void addEmployee(Employee e){
		if(e!=null){
		    em.getTransaction().begin();
		    em.persist(e);
		    em.getTransaction().commit();
		    em.close();
		}
	}
	
	public Employee getEmployee(String email){
		em.getTransaction().begin();
		Query q = em.createQuery("select o from Employee o where o.email='"+email+"' and o.active = true");
	    Employee employee = (Employee) q.getSingleResult();
	    em.close();
		return employee;
	}
}
