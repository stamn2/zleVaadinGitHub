package com.project1.controller;

import com.project1.domain.Employee;

public class UserController {
    public static void addEmployee(String email, String firstname, String lastname, String street, String plz, String city, String tel){
    	Employee user = new Employee(email, firstname, lastname, street, plz, city, tel);
        String password = user.generatePassword(); //TODO
        
    }
}
