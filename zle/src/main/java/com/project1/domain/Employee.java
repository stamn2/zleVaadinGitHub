package com.project1.domain;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.project1.zle.BCrypt;

@Entity
public class Employee implements Serializable {

    @Id 
    @GeneratedValue  private long id;

    private boolean active = true;
    private boolean changePassword = true;
    private boolean isAdmin = false;
    private String email;
    private String password="123";
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
    }

    //TODO
    public String generatePassword(){
        return "";
    }

    //TODO
    public String hashPassword(String password){
    	String stronger_salt = BCrypt.gensalt(12);
    	this.password= BCrypt.hashpw(password, stronger_salt); 
        return "";
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
