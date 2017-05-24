package com.project1.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Cost {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    @Temporal(TemporalType.DATE)
    private Date date;
    private double price;
    private String description;

    @ManyToOne
    private Project project;

    public Cost(){

    }

    public Cost(String name, Date date, double price, Project project, String description){
        this.name = name;
        this.date = date;
        this.price = price;
        this.project = project;
        this.description = description;
    }

    //------------ GETTER AND SETTER ----------------------

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Project getProject() {
        return project;
    }
}
