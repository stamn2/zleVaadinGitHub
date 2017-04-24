package com.project1.controller;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

public class LoginController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static Employee login(String email, String password){
        Employee emp = getEmployee(email);
        if(emp != null && emp.checkPassword(password)){
            return emp;
        }
        return null;
    }
    
    public static Employee getEmployee(String email){
    	return zem.getEmployee(email);
    }

    public static boolean changePassword(Employee emp,String oldPassword, String newPassword){
        if(login(emp.getEmail(), oldPassword) == null){
            return false;
        }
        emp.setPassword(newPassword);
        emp.setChangePassword(false);
        //TODO persistence
        zem.persistEmployee(emp);
        return true;
    }

}
