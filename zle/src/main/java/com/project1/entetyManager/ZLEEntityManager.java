package com.project1.entetyManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;


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
			throw new IllegalArgumentException("Object is null");
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
			em.refresh(employee);
			return employee;
		} catch (NoResultException e) {
			return null; 
		}
	}

	public List<Employee> getActiveEmployees(){
		Query q = em.createQuery("select o from Employee o where o.active = true");
		List<Employee> employeeList = q.getResultList();
		for(Employee e : employeeList) {
			em.refresh(e);
		}
		return employeeList;
	}
	
	public List<Employee> getAllEmployees(){
		Query q = em.createQuery("select o from Employee o");
		List<Employee> employeeList = q.getResultList();
		for(Employee e : employeeList) {
			em.refresh(e);
		}
		return employeeList;
	}

	public List<Client> getClients(){
		Query q = em.createQuery("select o from Client o");
		List<Client> clientList = q.getResultList();
		for(Client c : clientList) {
			em.refresh(c);
		}
		return clientList;
	}

	public List<Project> getActivePojects(){
		Query q = em.createQuery("select o from Project o where o.active = true");
		List<Project> projectList = q.getResultList();
		for(Project p : projectList) {
			em.refresh(p);
		}
		return projectList;
	}
	
	public List<ProjectCommitment> getProjectCommitment(long id){
		Query q = em.createQuery("select o from ProjectCommitment o where o.project ="+id);
		List<ProjectCommitment> projectCommitmenttList = q.getResultList();
		for(ProjectCommitment p : projectCommitmenttList) {
			em.refresh(p);
		}
		return projectCommitmenttList;
	}
	
	public void startTransaction(){
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
	}

	
	public void endTransaction(){
		if(em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}

	
	public Object findObject(Class c, Long id){
		Object o = em.find(c, id);
		em.refresh(o);
		return o;
	}

	public void removeElement(Object o) {
		if (o == null)
			throw new IllegalArgumentException("object is null");
		startTransaction();
		em.remove(o);
		em.getTransaction().commit();
	}
}
