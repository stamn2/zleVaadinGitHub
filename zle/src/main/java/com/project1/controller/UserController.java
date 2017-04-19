package com.project1.controller;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

public class UserController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static void addEmployee(String email, String firstname, String lastname, String street, String plz, String city, String tel){
    	Employee emp = new Employee(email, firstname, lastname, street, plz, city, tel);
        String password = emp.generatePassword(); //TODO
        zem.addEmployee(emp);
    }
}
