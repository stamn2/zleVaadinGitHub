package com.project1.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectCommitment {
    @Id
    @GeneratedValue private long id;
	
    private boolean active=true;
    private double hourlyRate;
    
    @ManyToOne
    private Project project;
    
    @ManyToOne
    private Employee employee;
    
    public ProjectCommitment(){
    }
    
    public ProjectCommitment(Project project, Employee employee, double hourlyRate){
    	this.project=project;
    	this.employee=employee;
    	this.hourlyRate = hourlyRate;
    }

	public long getId() {
		return id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
    
    
}
