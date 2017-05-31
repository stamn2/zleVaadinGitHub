package com.project1.controller;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

/**
 * @author Rosalie Truong & Nils Stampfli
 */
public class LoginController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    /**
     * Check login from an active employee
     * @param email email of the employee
     * @param password password of the employee
     * @return the logged employee or null if the employee doesn't exists, isn't active or with wrong credentials
     */
    public static Employee login(String email, String password){
        Employee emp = zem.getEmployee(email); //Get an active employee from database
        if(emp != null && emp.checkPassword(password)){ //Check if the employee was found and if credentials are correct
            return emp;
        }
        return null;
    }

    /**
     * Find an employee with his email
     * @param email email of the employee
     * @return the employee with the given email
     */
    public static Employee getEmployee(String email){
        return zem.getEmployee(email);
    }

    /**
     * Change the password from an active employee
     * @param emp employee who's password will be changed
     * @param oldPassword password that was used until now
     * @param newPassword new password that the employee will use
     * @return if the password change was successful, when false, the old password was wrong or employee doesn't exists or is inactive
     */
    public static boolean changePassword(Employee emp,String oldPassword, String newPassword){
        if(login(emp.getEmail(), oldPassword) == null){ //Check login from client (to prevent that someone else change the password)
            return false;
        }

        emp = (Employee)zem.findObject(Employee.class, emp.getId());
        zem.startTransaction(); //begin modifications in database
        emp.hashAndSetPassword(newPassword);
        emp.setChangePassword(false); //the password doesn't need to be changed on a further login
        zem.endTransaction(); //end modifications in database
        return true;
    }
}
