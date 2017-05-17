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

    public static Client addClient(String companyName, String firstname, String lastname, String street, String plz,
                                   String city, String email, String tel){
        Client client = new Client(companyName, firstname, lastname, street, plz, city, email, tel);
        zem.persistObject(client);
        return client;
    }

    public static void updateClient(Client client, String companyName, String firstname, String lastname, String street,
                                    String plz, String city, String email, String tel){
        zem.startTransaction();
        client.setCompanyName(companyName);
        client.setFirstname(firstname);
        client.setLastname(lastname);
        client.setStreet(street);
        client.setPlz(plz);
        client.setCity(city);
        client.setEmail(email);
        client.setTel(tel);
        zem.endTransaction();
    }

    public static Client getClient(long clientId) {
        return (Client)zem.findObject(Client.class, clientId);
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

    public static void assignEmployeeToSystemProjects(Employee emp){
        List<Project> systemProjects = zem.getSystemProjects();
        for(Project p : systemProjects){
            ProjectController.addProjectCommitment(p, emp, 0);
        }
    }

    public static Project getRealTimeRecordingProject(){
        List<Project> systemProjects = zem.getSystemProjects();
        for(Project p : systemProjects){
            if(p.getName().equals("RealTimeRecording")){
                return p;
            }
        }
        return null;
    }


}
