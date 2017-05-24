package com.project1.controller;

import com.project1.domain.*;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static List<Project> getProjects(){
        return zem.getActivePojects();
    }

    public static Project addProject(String name, Client client){
        Project project = new Project(name, client);
        zem.persistObject(project);
        return project;
    }

    public static void updateProject(Project project, String name, Client client){
        zem.startTransaction();
        project.setName(name);
        project.setClient(client);
        zem.endTransaction();
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

    public static List<Activity> getActivitiesFromProject(long idProject){
        return zem.getAllActivitiesFromProject(idProject);
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

    public static void endProject(Project project){
        List<Activity> activities = zem.getActiveActivitiesFromProject(project.getId());
        List<ProjectCommitment> projectCommitments = zem.getProjectCommitmentWithProject(project.getId());
        zem.startTransaction();
        for(Activity a : activities){
            a.setActive(false);
        }
        for(ProjectCommitment pc : projectCommitments){
            pc.setActive(false);
        }
        project.setActive(false);
        zem.endTransaction();
    }
    
    public static void inactivateProjectCommitment(ProjectCommitment pc){
    	zem.startTransaction();
    	pc.setActive(false);
    	zem.endTransaction();
    }

    public static List<Cost> getCosts(long idProject){
        return zem.getCosts(idProject);
    }

    public static Cost addCost(String name, Date date, double price, Project project, String description){
        Cost cost = new Cost(name, date, price, project, description);
        zem.persistObject(cost);
        return cost;
    }

}
