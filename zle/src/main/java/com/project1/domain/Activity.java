package com.project1.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private long id;

    private boolean active = true;
    private boolean realTimeRecord = false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    private String comment;
    @ManyToOne
    ProjectCommitment projectCommitment;

    public Activity(){

    }

    public Activity(Date begin, Date end, String comment, ProjectCommitment projectCommitment){
        this.beginDate = begin;
        this.endDate = end;
        this.comment = comment;
        this.projectCommitment = projectCommitment;
    }

    //------------ GETTER AND SETTER ----------------------

    public long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }
    
    public boolean isRealTimeRecord(){
    	return realTimeRecord;
    }
    
    public void setRealTimeRecord(boolean isRealtimeRecord){
    	 realTimeRecord=isRealtimeRecord;
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

    public ProjectCommitment getProjectCommitment() {
        return projectCommitment;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setProjectCommitment(ProjectCommitment projectCommitment) {
        this.projectCommitment = projectCommitment;
    }
}
