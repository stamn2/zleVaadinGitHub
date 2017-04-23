package com.project1.domain;


import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import com.project1.zle.BCrypt;


@Entity
public class Employee implements Serializable {

	private static final String VALID_PW_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; //+ !@#$%^&*()-_=+{}[]|:;<>?,./
    private static final int DEFAULT_PASSWORD_LENGTH = 12;
    private static final Random RANDOM = new SecureRandom();


	@Id 
    @GeneratedValue  private long id;

    private boolean active = true;
    private boolean changePassword = true;
    private boolean isAdmin = false;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String street;
    private String plz;
    private String city;
    private String tel;

    public Employee(){

    }

    public Employee(String email, String firstname, String lastname, String street, String plz, String city,String tel, boolean isAdmin){
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.street = street;
        this.plz = plz;
        this.city = city;
        this.tel = tel;
        this.isAdmin = isAdmin;
        password = generatePassword();
    }

    //TODO
    public String generatePassword(){
        StringBuilder pw = new StringBuilder();
    	for (int i=0; i<DEFAULT_PASSWORD_LENGTH; i++) {                        
            int index = (int)(RANDOM.nextInt(VALID_PW_CHARS.length()));
            pw.append(VALID_PW_CHARS.charAt(index));
            System.out.println(i);
    }
    	String hashedPw = hashPassword(pw.toString()); //-> not used here, stored directly in DB
        return pw.toString();
    }

    //TODO
    public String hashPassword(String password){
    	String stronger_salt = BCrypt.gensalt(12);
    	this.password= BCrypt.hashpw(password, stronger_salt); 
        return password;
    }

    //------------ GETTER AND SETTER ----------------------
    public long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getStreet() {
        return street;
    }

    public String getPlz() {
        return plz;
    }

    public String getCity() {
        return city;
    }

    public String getTel() {
        return tel;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

	public boolean checkPassword(String password) {
		return BCrypt.checkpw(password, this.password);
	}
}
