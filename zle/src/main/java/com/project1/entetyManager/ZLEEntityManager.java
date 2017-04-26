package com.project1.entetyManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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

	public ZLEEntityManager(String PERSISTENCE_UNIT_NAME) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    em = factory.createEntityManager();
	}
	
	public void persistObject(Object o){
		if (o == null)
			throw new IllegalArgumentException("Employee is null");
		    startTransaction();
		    em.persist(o);
		    em.getTransaction().commit();
	}
	
	public Employee getEmployee(String email){
		if (email == null)
			throw new IllegalArgumentException("email is null");
		Query q = em.createQuery("select o from Employee o where o.email='"+email+"' and o.active = true");
		try {
			Employee employee = (Employee) q.getSingleResult();
			return employee;
		} catch (NoResultException e) {
			return null; 
		}

	}

	public List<Employee> getActiveEmployees(){
		Query q = em.createQuery("select o from Employee o where o.active = true");
		List<Employee> employeeList = q.getResultList();
		return employeeList;
	}
	
	public List<Employee> getAllEmployees(){
		Query q = em.createQuery("select o from Employee o");
		List<Employee> employeeList = q.getResultList();
		return employeeList;
	}
	
	public void startTransaction(){
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
	}
	
	public void removeElement(Object o) {
		if (o == null)
			throw new IllegalArgumentException("object is null");
		startTransaction();
		em.remove(o);
		em.getTransaction().commit();
	}
}
