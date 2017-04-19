package com.project1.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee implements Serializable {

    @Id 
    @GeneratedValue  private long id;

    private boolean active = true;
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

    public Employee(String email, String firstname, String lastname, String street, String plz, String city,String tel){
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.street = street;
        this.plz = plz;
        this.city = city;
        this.tel = tel;
    }

    //TODO
    public String generatePassword(){
        return "";
    }

    //TODO
    public String hashPassword(){
        return "";
    }

    public String getPassword(){
        return password;
    }
}
