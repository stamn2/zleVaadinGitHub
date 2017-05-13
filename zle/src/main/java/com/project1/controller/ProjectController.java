package com.project1.controller;

import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static Client addClient(String companyName, String firstname, String lastname, String street, String plz, String city, String email, String tel){
        Client client = new Client(companyName, firstname, lastname, street, plz, city, email, tel);
        zem.persistObject(client);
        return client;
    }

    public static List<Client> getClients(){
        return zem.getClients();
    }

    public static List<Project> getProjects(){
        return zem.getActivePojects();
    }

    public static Project addProject(String name, Client client){
        Project project = new Project(name, client);
        zem.persistObject(project);
        return project;
    }
    
    public static ProjectCommitment addProjectCommitment(Project project, Employee employee, double hourlyRate){
        List<ProjectCommitment> pcFromProject = zem.getProjectCommitmentWithProject(project.getId());
        for(ProjectCommitment pc : pcFromProject){
            if(pc.getEmployee().getId() == employee.getId()){
                return null;
            }
        }
    	ProjectCommitment projectCommitment = new ProjectCommitment(project, employee, hourlyRate);
        zem.persistObject(projectCommitment);
        return projectCommitment;
    }

    public static Project getProject(long id){
        return (Project)zem.findObject(Project.class, id);
    }
    
    public static ProjectCommitment getProjectCommitment(long id){
        return (ProjectCommitment)zem.findObject(ProjectCommitment.class, id);
    }
    
    public static List<ProjectCommitment> getProjectCommitmentList(long idProject){
        return zem.getProjectCommitmentWithProject(idProject);
    }
    
    public static ProjectCommitment getProjectCommitmentbyBothIds(long idEmp, long idProject){
        return zem.getProjectCommitmentWithProjectAndEmployee(idEmp,idProject);
    }

	public static Project getProject(String projectName) {
		return zem.getProjectByName(projectName);
	}

	public static void addProjectX() {
		ProjectController.addProject("ProjectX", 
    					ProjectController.addClient("X", "x", "x", "x", "x", "x", "x@mai.com", "x"));	 
	}

	public static void assignEmpToProjectX(Employee emp) {
		ProjectController.addProjectCommitment(ProjectController.getProject("ProjectX"), emp, 1234);
		
	}


}
