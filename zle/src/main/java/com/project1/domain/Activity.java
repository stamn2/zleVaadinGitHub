package com.project1.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private long id;

    private boolean active = true;
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    private String comment;

    public Activity(){

    }

    public Activity(Date begin, Date end, String comment){
        this.beginDate = begin;
        this.endDate = end;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getComment() {
        return comment;
    }
}
