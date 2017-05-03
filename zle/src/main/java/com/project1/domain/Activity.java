package com.project1.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private long id;

    private boolean active;
    private Date begin;
    private Date end;
    private String comment;
}
