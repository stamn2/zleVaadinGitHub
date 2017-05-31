package com.project1.controller;

import com.project1.domain.*;
import com.project1.entetyManager.ZLEEntityManager;
import com.project1.zle.BillingEmployeeItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.derby.tools.sysinfo;

public class ProjectController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static List<Project> getProjects(){
        return zem.getActivePojects();
    }

    public static List<Project> getArchivedProjects(){
        return zem.getInactivePojects();
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

    public static Activity getOldestActiviteFromProject(long idProject){
        return zem.getOldestActivityFromProject(idProject);
    }

    public static Activity getLastActiviteFromProject(long idProject){
        return zem.getLastActivityFromProject(idProject);
    }

    public static List<Activity> getMonthlyActivitiesFromProject(long idProject, int month, int year){
        return zem.getMonthlyActivitiesFromProject(idProject, month, year);
    }

    public static void inactiveMonthlyActivitiesFromProject(List<Activity> monthlyActivities){
        zem.startTransaction();
        for(Activity a : monthlyActivities){
            a.setActive(false);
        }
        zem.endTransaction();
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
        zem.startTransaction();
        project.setActive(false);
        zem.endTransaction();
    }

    public static void enableProject(Project project){
        zem.startTransaction();
        project.setActive(true);
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

    public static void removeCost(Cost cost){
        zem.removeElement(cost);
    }

    public static List<Cost> getMonthlyCosts(long idProject, int month, int year){
        return zem.getMonthlyCost(idProject, month, year);
    }

    //TODO use bigDecimal???
    public static double getSumCosts(List<Cost> costs){
        double sum = 0;
        for (Cost c : costs){
            sum += c.getPrice();
        }
        return sum;
    }

    //TODO use bigDecimal???
    public static List<BillingEmployeeItem> getBillingEmployeeItems(List<Activity> activitiesFromMonth){
        List<BillingEmployeeItem> billingList = new ArrayList<>();
        if(activitiesFromMonth.size() == 0){
            return billingList;
        }

        ProjectCommitment currentPC = activitiesFromMonth.get(0).getProjectCommitment();
        double cost=0;
        double totalHoures = 0;
        double hourlyRate = activitiesFromMonth.get(0).getProjectCommitment().getHourlyRate();
        for (Activity activity : activitiesFromMonth) {
            if (currentPC.getId() != activity.getProjectCommitment().getId()) {
                billingList.add(new BillingEmployeeItem(currentPC, cost, totalHoures));
                cost=0;
                totalHoures = 0;
                currentPC = activity.getProjectCommitment();
                hourlyRate = activity.getProjectCommitment().getHourlyRate();
            }
            double dif= ((double)(activity.getEndDate().getTime()-activity.getBeginDate().getTime()))/3600000;
            totalHoures+=dif;
            cost+=dif*hourlyRate;
        }
        billingList.add(new BillingEmployeeItem(activitiesFromMonth.get(activitiesFromMonth.size()-1).getProjectCommitment(), cost, totalHoures));
        return billingList;

    }

    //TODO use bigDecimal???
    public static double getSumBillingEmployeeItems(List<BillingEmployeeItem> billingEmployeeItems){
        double sum = 0;
        for (BillingEmployeeItem bei : billingEmployeeItems){
            sum += bei.getCost();
        }
        return sum;
    }

}
