package com.project1.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    
    @OneToMany
    private List<Activity> activities = new ArrayList<>();
    
    public ProjectCommitment(){
    }
    
    public ProjectCommitment(Project project, Employee employee, double hourlyRate){
    	this.project=project;
    	this.employee=employee;
    	this.hourlyRate = hourlyRate;
    }
	
	public void addActivity(Activity activity){
		activities.add(activity);
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

	public List<Activity> getActivitiesList() {
		return activities;
	}
    
    
}
