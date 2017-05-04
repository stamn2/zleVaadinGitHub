package com.project1.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private long id;

    private boolean active;
    @Temporal(TemporalType.TIMESTAMP)
    private Date begin;
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;
    private String comment;
}
