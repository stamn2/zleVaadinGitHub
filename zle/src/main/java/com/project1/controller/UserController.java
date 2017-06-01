package com.project1.controller;

import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;
import com.project1.zle.EmailSender;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @author Rosalie Truong & Nils Stampfli
 */
public class UserController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_USER = "zle.projekt1@gmail.com";
    private static final String EMAIL_PASSWORD = "ZLEProjekt1";

    /**
     * Add a new employee in the system
     * @param email email of the employee
     * @param firstname firstname of the employee
     * @param lastname lastname of the employee
     * @param street street of the employee
     * @param plz plz of the employee
     * @param city city of the employee
     * @param tel tel of the employee
     * @param isAdmin admin right of the employee
     * @return the new employee
     */
    public static Employee addEmployee(String email, String firstname, String lastname, String street, String plz, String city, String tel, boolean isAdmin){
        if(zem.getEmployee(email) != null){ //Check if another active employee with same email exists
            return null; //Doesn't allow two active employee with same email
        }
        Employee emp = new Employee(email, firstname, lastname, street, plz, city, tel, isAdmin);
        String password = emp.generatePassword(); //Generate a password for the employee
        System.out.println("Password: " + password);
        zem.persistObject(emp); //Save in database

        /*try { //TODO : uncomment the lines to send email for final version and remove system.out.println password
            sendAccountCreatedEmail(email, password); //Send email with login datas to employee
        } catch (MessagingException e) {
        	System.out.println("Error: Email not send!");
        }*/

        ProjectController.assignEmployeeToSystemProjects(emp); //Assign the employee to the system projects
        return emp;
    }

    /**
     * Generate a new password for the employee. The function should be used when the employee has forgotten his password.
     * @param emp employee who needs a new password
     */
    public static void generatedNewPassword(Employee emp){
        emp = (Employee)zem.findObject(Employee.class, emp.getId()); //Find employee in database

        zem.startTransaction(); //begin modifications in database
        String newPw = emp.generatePassword(); //Generate a password for the employee
        System.out.println("Password: " + newPw);
        emp.setChangePassword(true); //Ask employee to the change password when he logs in
        zem.endTransaction(); //end modifications in database

        /*try { //TODO : uncomment the lines to send email for final version and remove system.out.println newPw
            sendGenNewPasswordEmail(emp.getEmail(), newPw); //Send email with new password to the employee
        } catch (MessagingException e) {
        	System.out.println("Error: Email not send!");
        }*/
    }

    /**
     * Disable the account from an employee. The activities from employee will stay in the database and billed to the client.
     * The employee can no more log in.
     * @param employee employee who should be inactive
     */
    public static void disableEmployeeAccount(Employee employee){
        zem.startTransaction(); //begin modifications in database
        employee.setActive(false); //Disable account
        zem.endTransaction(); //end modifications in database

        /*try { //TODO : uncomment the lines to send email for final version
            sendAccountDisabledEmail(employee.getEmail()); //Send email to inform the employee that his account is disabled
        } catch (MessagingException e) {
            System.out.println("Error: Email not send!");
        }*/
    }

    /**
     * Enable the account from an employee. He can now log in.
     * @param employee employee who should be activated
     * @return if activation has been successful. If not, an active employee with same email exists.
     */
    public static boolean enableEmployeeAccount(Employee employee){
        if(zem.getEmployee(employee.getEmail()) != null){ //Check if there exists an active employee with same email
            return false; //Doesn't allow two active employee with same email
        }

        zem.startTransaction(); //begin modifications in database
        employee.setActive(true); //Enable account
        zem.endTransaction(); //end modifications in database

        /*try { //TODO : uncomment the lines to send email for final version
            sendAccountEnabledEmail(employee.getEmail()); //Send email to inform the employee that his account is enabled
        } catch (MessagingException e) {
            System.out.println("Error: Email not send!");
        }*/

        return true;
    }

    /**
     * Update the details from an employee
     * @param employee employee that need to be updated
     * @param email email of the employee
     * @param firstname first name of the employee
     * @param lastname last name of the employee
     * @param street street of the employee
     * @param plz plz of the employee
     * @param city city of the employee
     * @param tel tel of the employee
     * @param isAdmin admin right of the employee
     */
    public static void updateEmployee(Employee employee, String email, String firstname, String lastname, String street,
                                      String plz, String city, String tel, boolean isAdmin){
        zem.startTransaction(); //begin modifications in database
        employee.setEmail(email);
        employee.setFirstname(firstname);
        employee.setLastname(lastname);
        employee.setStreet(street);
        employee.setPlz(plz);
        employee.setCity(city);
        employee.setTel(tel);
        employee.setIsAdmin(isAdmin);
        zem.endTransaction(); //end modifications in database

        /*try { //TODO : uncomment the lines to send email for final version
            sendAccountUpdatedEmail(employee); //Send email to inform the employee that his account is updated
        } catch (MessagingException e) {
            System.out.println("Error: Email not send!");
        }*/
    }

    /**
     * Get all active employees
     * @return a list of the active employees
     */
    public static List<Employee> getActivesEmployees(){
        return zem.getActiveEmployees();
    }

    /**
     * Get all inactive employees
     * @return a list of the inactive employees
     */
    public static List<Employee> getInactivesEmployees(){
        return zem.getInactiveEmployees();
    }

    /**
     * Find an employee with his id
     * @param employeeId id from employee
     * @return the employee with the given id
     */
    public static Employee getEmployee(long employeeId) {
        return (Employee)zem.findObject(Employee.class, employeeId);
    }

    private static void sendAccountCreatedEmail(String email, String password) throws MessagingException {
        StringBuilder messageText = new StringBuilder();
        messageText.append("<h1>Your account has been created!</h1>");
        messageText.append("<h3>User: " + email + "</h3><br /><br />");
        messageText.append("<h3>Password: " + password + "<h3><br /><br />");
        messageText.append("<h4>Login to: <a href=\"http://localhost:8080/\" > http://localhost:8080/ <a></h4>");
        messageText.append("For any questions you can contact an administrator.");

        String subject = "ZLE - Your account has been created!";

        new EmailSender(EMAIL_HOST, EMAIL_USER, EMAIL_PASSWORD).send(email, subject,
                messageText.toString());
    }

    private static void sendGenNewPasswordEmail(String email, String password) throws MessagingException {
        StringBuilder messageText = new StringBuilder();
        messageText.append("<h1>You've got a new Password!</h1>");
        messageText.append("<h3>Password: " + password + "<h3><br /><br />");
        messageText.append("For any questions you can contact an administrator.");

        String subject = "ZLE - Your new Password";

        new EmailSender(EMAIL_HOST, EMAIL_USER, EMAIL_PASSWORD).send(email, subject,
                messageText.toString());
    }

    private static void sendAccountUpdatedEmail(Employee emp) throws MessagingException {
        StringBuilder messageText = new StringBuilder();
        messageText.append("<h1>Your account has been updated!</h1>");
        messageText.append("<h3>Your coordinates:</h3>");
        messageText.append(emp.getFirstname() + " " + emp.getLastname()+"<br />");
        messageText.append(emp.getStreet()+"<br />");
        messageText.append(emp.getPlz() + " " + emp.getCity()+"<br />");
        messageText.append(emp.getTel() +"<br /><br />");
        messageText.append("For any questions you can contact an administrator.");

        String subject = "ZLE - Your account has been updated!";

        new EmailSender(EMAIL_HOST, EMAIL_USER, EMAIL_PASSWORD).send(emp.getEmail(), subject,
                messageText.toString());
    }

    private static void sendAccountDisabledEmail(String email) throws MessagingException {
        String messageText;
        messageText = "<h1>Your account has been disabled!</h1>";
        messageText += "For any questions you can contact an administrator.";

        String subject = "ZLE - Your account has been disabled!";

        new EmailSender(EMAIL_HOST, EMAIL_USER, EMAIL_PASSWORD).send(email, subject,
                messageText);
    }

    private static void sendAccountEnabledEmail(String email) throws MessagingException {
        String messageText;
        messageText = "<h1>Your account has been enabled, you can now login!</h1>";
        messageText += "For any questions you can contact an administrator.";

        String subject = "ZLE - Your account has been enabled!";

        new EmailSender(EMAIL_HOST, EMAIL_USER, EMAIL_PASSWORD).send(email, subject,
                messageText);
    }
}
