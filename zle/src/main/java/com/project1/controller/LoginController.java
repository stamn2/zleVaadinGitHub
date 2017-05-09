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
        /*zem.getEmployee(emp.getEmail()).setPassword(newPassword);
        zem.getEmployee(emp.getEmail()).setChangePassword(false);
        
        zem.persistObject(zem.getEmployee(emp.getEmail()));*/

        emp = (Employee)zem.findObject(Employee.class, emp.getId());
        zem.startTransaction();
        emp.hashAndSetPassword(newPassword);
        emp.setChangePassword(false);
        zem.endTransaction();
        return true;
    }

}
