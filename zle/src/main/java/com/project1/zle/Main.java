package com.project1.zle;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.project1.domain.Employee;

public class Main {
	
    private static final String PERSISTENCE_UNIT_NAME = "zleDB";
    private static EntityManagerFactory factory; 
    
public static void main(String[] args) {
	System.out.println("hello from worben");
	System.out.println("hello, thank you for the mail!");
	System.out.println("Test");
	
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

    em.close();

}
}
