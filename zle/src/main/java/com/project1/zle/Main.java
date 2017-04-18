package com.project1.zle;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Main {
	
    private static final String PERSISTENCE_UNIT_NAME = "zleDB";
    private static EntityManagerFactory factory;
    
public static void main(String[] args) {
	System.out.println("hello from worben");
	System.out.println("hello, thank you for the mail!");
	System.out.println("Test");
	
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();
    // read the existing entries and write to console
   /* Query q = em.createQuery("select t from Todo t");
    List<Todo> todoList = q.getResultList();
    for (Todo todo : todoList) {
        System.out.println(todo);
    }
    System.out.println("Size is: " + todoList.size());

    // create new todo
    em.getTransaction().begin();
    Todo todo = new Todo();
    todo.setSummary("This is a test");
    todo.setDescription("This is a test");
    em.persist(todo);
    em.getTransaction().commit();

    em.close();*/
}
}
