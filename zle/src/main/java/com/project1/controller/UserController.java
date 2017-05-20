package com.project1.controller;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;
import com.project1.zle.EmailSender;

import javax.mail.MessagingException;
import java.util.List;

public class UserController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static Employee addEmployee(String email, String firstname, String lastname, String street, String plz, String city, String tel, boolean isAdmin){
    	if(zem.getEmployee(email) != null){
            return null;
        }
        Employee emp = new Employee(email, firstname, lastname, street, plz, city, tel, isAdmin);
        String password = emp.generatePassword();
        System.out.println(password);
        zem.persistObject(emp);
        /*try { //TODO : uncomment the lines to send email for final version and remove system.out.println password
            sendAccountCreatedEmail(email, password);
        } catch (MessagingException e) {
        	System.out.println("Error: Email not send!");
        }*/
        ProjectController.assignEmployeeToSystemProjects(emp);
        return emp;
    }

    public static void generatedNewPassword(Employee emp){
        emp = (Employee)zem.findObject(Employee.class, emp.getId());
        zem.startTransaction();
        String newPw = emp.generatePassword();
        System.out.println(newPw);
        emp.setChangePassword(true);
        zem.endTransaction();

        /*try { //TODO : uncomment the lines to send email for final version and remove system.out.println newPw
            sendGenNewPasswordEmail(emp.getEmail(), newPw);
        } catch (MessagingException e) {
        	System.out.println("Error: Email not send!");
        }*/
    }

    public static void disableEmployeeAccount(Employee employee){
        zem.startTransaction();
        employee.setActive(false);
        zem.endTransaction();
        /*try { //TODO : uncomment the lines to send email for final version
            sendAccountDisabledEmail(employee.getEmail());
        } catch (MessagingException e) {
            System.out.println("Error: Email not send!");
        }*/
    }

    public static void updateEmployee(Employee employee, String email, String firstname, String lastname, String street,
                                      String plz, String city, String tel, boolean isAdmin){
        zem.startTransaction();
        employee.setEmail(email);
        employee.setFirstname(firstname);
        employee.setLastname(lastname);
        employee.setStreet(street);
        employee.setPlz(plz);
        employee.setCity(city);
        employee.setTel(tel);
        employee.setIsAdmin(isAdmin);
        zem.endTransaction();

        /*try { //TODO : uncomment the lines to send email for final version
            sendAccountUpdatedEmail(employee);
        } catch (MessagingException e) {
            System.out.println("Error: Email not send!");
        }*/
    }
    
    public static List<Employee> getActivesEmployees(){
        return zem.getActiveEmployees();
    }

    public static Employee getEmployee(long employeeId) {
        return (Employee)zem.findObject(Employee.class, employeeId);
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

    private static void sendGenNewPasswordEmail(String email, String password) throws MessagingException {
        String messageText;
        String subject;
        messageText = "<h1>You've got a new Password!</h1>";
        messageText += "<h3>Password: " + password + "<h3><br /><br />";
        messageText += "For any questions you can contact an administrator.";
        subject = "ZLE - Your new Password";

        new EmailSender("smtp.gmail.com", "zle.projekt1@gmail.com", "ZLEProjekt1").send(email, subject,
                messageText);
    }

    private static void sendAccountUpdatedEmail(Employee emp) throws MessagingException {
        String messageText;
        String subject;
        messageText = "<h1>Your account has been updated!</h1>";
        messageText += "<h3>Your coordinates:</h3>";
        messageText += emp.getFirstname() + " " + emp.getLastname()+"<br />";
        messageText += emp.getStreet()+"<br />";
        messageText += emp.getPlz() + " " + emp.getCity()+"<br />";
        messageText += emp.getTel() +"<br /><br />";
        messageText += "For any questions you can contact an administrator.";
        subject = "ZLE - Your account has been updated!";

        new EmailSender("smtp.gmail.com", "zle.projekt1@gmail.com", "ZLEProjekt1").send(emp.getEmail(), subject,
                messageText);
    }

    private static void sendAccountDisabledEmail(String email) throws MessagingException {
        String messageText;
        String subject;
        messageText = "<h1>Your account has been disabled!</h1>";
        messageText += "For any questions you can contact an administrator.";
        subject = "ZLE - Your account has been disabled!";

        new EmailSender("smtp.gmail.com", "zle.projekt1@gmail.com", "ZLEProjekt1").send(email, subject,
                messageText);
    }
}
