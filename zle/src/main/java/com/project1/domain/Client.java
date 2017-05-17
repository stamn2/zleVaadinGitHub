package com.project1.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Client {

    @Id
    @GeneratedValue
    private long id;

    private String companyName;
    private String firstname;
    private String lastname;
    private String street;
    private String plz;
    private String city;
    private String email;
    private String tel;

    public Client(){

    }

    public Client(String companyName, String firstname, String lastname, String street, String plz, String city, String email, String tel){
        this.companyName = companyName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.street = street;
        this.plz = plz;
        this.city = city;
        this.email = email;
        this.tel = tel;
    }

    //------------ GETTER AND SETTER ----------------------

    public long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
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

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
