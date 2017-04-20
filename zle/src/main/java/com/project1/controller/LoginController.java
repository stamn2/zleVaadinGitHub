package com.project1.controller;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

public class LoginController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static Employee login(String email, String password){
        Employee emp = zem.getEmployee(email);
        if(emp != null && password.equals(emp.getPassword())){ //TODO hashpassword
            return emp;
        }
        return null;
    }

}
