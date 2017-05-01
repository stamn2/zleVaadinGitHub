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
    private boolean active;

    @ManyToOne
    private Client client;
}
