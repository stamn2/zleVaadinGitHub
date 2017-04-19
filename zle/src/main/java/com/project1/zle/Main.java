package com.project1.zle;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.project1.domain.Employee;



public class Main {
	
    private static final String PERSISTENCE_UNIT_NAME = "zleDB";
    private static EntityManagerFactory factory; 
    
public static void main(String[] args) {
	
	factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();
    
    
    //clean DB-Table Employee
    Query q = em.createQuery("select o from Employee o");
    List<Employee> todoList = q.getResultList();
    for (Employee todo : todoList) {
        System.out.println(todo);
    }
    for (Employee employee : todoList) {
    	em.getTransaction().begin();
        em.remove(employee);
        em.getTransaction().commit();
	}

    System.out.println("Size is: " + todoList.size());
    
    String email="email@mail.com";
    
	em.getTransaction().begin();
	
	Query q2 = em.createQuery("select o from Employee o where o.email='"+email+"' and o.active = 'true'");
    try {
    	Employee employee = (Employee) q2.getSingleResult();
	} catch (NoResultException e) {
		System.out.println("null");
	}


    em.close();

}
}
