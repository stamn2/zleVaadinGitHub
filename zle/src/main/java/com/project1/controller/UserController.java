package com.project1.controller;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;
import com.project1.zle.EmailSender;

import javax.mail.MessagingException;
import java.util.List;

public class UserController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static boolean addEmployee(String email, String firstname, String lastname, String street, String plz, String city, String tel, boolean isAdmin){
    	if(zem.getEmployee(email) != null){
            return false;
        }
        Employee emp = new Employee(email, firstname, lastname, street, plz, city, tel, isAdmin);
        String password = emp.generatePassword();
        System.out.println(password);
        zem.persistObject(emp);
        /*try { TODO : uncomment the lines to send email for final version
            sendAccountCreatedEmail(email, password);
        } catch (MessagingException e) {
        	System.out.println("Error: Email not send!");
        }*/
        return true;
    }
    
    public static List<Employee> getActivesEmployees(){
        return zem.getActiveEmployees();
    }

    private static void sendAccountCreatedEmail(String email, String password) throws MessagingException {
        String messageText;
        String subject;
        messageText = "<h1>Your account has been created!</h1>";
        messageText += "<h3>User: " + email + "</h3><br /><br />";
        messageText += "<h3>Password: " + password + "<h3><br /><br />";
        messageText += "<h4>Login to: <a href=\"http://localhost:8080/\" > http://localhost:8080/ <a></h4>";
        messageText += "For any questions you can contact an administrator.";
        subject = "ZLE - Your account has been created!";

        new EmailSender("smtp.gmail.com", "zle.projekt1@gmail.com", "ZLEProjekt1").send(email, subject,
                messageText);
    }
}
