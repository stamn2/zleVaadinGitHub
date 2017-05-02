package com.project1.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Project {
    @Id
    @GeneratedValue private long id;

    private String name;
    private boolean active = true;

    @ManyToOne
    private Client client;

    public Project(){

    }

    public Project(String name, Client client){
        this.name = name;
        this.client = client;
    }


    public String getCost() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getNumbOfEmp() {
        // TODO Auto-generated method stub
        return null;
    }

    //------------ GETTER AND SETTER ----------------------

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Client getClient() {
        return client;
    }
}
