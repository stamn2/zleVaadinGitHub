package com.project1.controller;

import com.project1.domain.*;
import com.project1.entetyManager.ZLEEntityManager;
import com.project1.zle.BillingEmployeeItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.derby.tools.sysinfo;

/**
 * @author Rosalie Truong & Nils Stampfli
 */
public class ProjectController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    /**
     * Get all active projects
     * @return a list of the active projects
     */
    public static List<Project> getProjects(){
        return zem.getActivePojects();
    }

    /**
     * Get all inactive projects
     * @return a list of the inactive projects
     */
    public static List<Project> getArchivedProjects(){
        return zem.getInactivePojects();
    }

    /**
     * Get the real time recording project. It is where the activities are stored when the employee does
     * a real time recording (Start/Stop), before he assigns the activity to a project.
     * @return the real time recording project (system project)
     */
    public static Project getRealTimeRecordingProject(){
        List<Project> systemProjects = zem.getSystemProjects(); //Get the system projects
        for(Project p : systemProjects){
            if(p.getName().equals("RealTimeRecording")){ //Find the real time project
                return p;
            }
        }
        return null;
    }

    /**
     * Add an employee to the projects of the system that it can do real time recording or assign his activity to no project
     * @param emp employee that has to be assigned to the system projects
     */
    public static void assignEmployeeToSystemProjects(Employee emp){
        List<Project> systemProjects = zem.getSystemProjects(); //Get the system projects
        for(Project p : systemProjects){
            ProjectController.addProjectCommitment(p, emp, 0); //Assign employee to the system project
        }
    }

    /**
     * Add a new project in the system
     * @param name name of the project
     * @param client client of the project
     * @return the new project
     */
    public static Project addProject(String name, Client client){
        Project project = new Project(name, client);
        zem.persistObject(project); //Save in database
        return project;
    }

    /**
     * Update the details from a project
     * @param project project that need to be updated
     * @param name name of the project
     * @param client client of the project
     */
    public static void updateProject(Project project, String name, Client client){
        zem.startTransaction(); //begin modifications in database
        project.setName(name);
        project.setClient(client);
        zem.endTransaction(); //end modifications in databse
    }

    /**
     * Find a project with his id
     * @param idProject id from project
     * @return the project with the given id
     */
    public static Project getProject(long idProject){
        return (Project)zem.findObject(Project.class, idProject);
    }

    /**
     * End a project. The employees can't add activities after that and admins can't modify the project.
     * @param project project that has to be ended
     */
    public static void endProject(Project project){
        zem.startTransaction();
        project.setActive(false); //End project
        zem.endTransaction();
    }

    /**
     * Enable again a project. The employee can again add activities and admin bring modifications.
     * @param project project that has to be enabled
     */
    public static void enableProject(Project project){
        zem.startTransaction();
        project.setActive(true); //Enable project
        zem.endTransaction();
    }

    /**
     * Assign an active employee to the project
     * @param project project where the employee will be assigned
     * @param employee employee who should be assigned to the project
     * @param hourlyRate hourly rate of the employee in the project
     * @return assignment of the employee in the project, if null, the employee is already assigned to the project
     */
    public static ProjectCommitment addProjectCommitment(Project project, Employee employee, double hourlyRate){
        List<ProjectCommitment> pcFromProject = zem.getProjectCommitmentWithProject(project.getId()); //list of active assignments
        for(ProjectCommitment pc : pcFromProject){
            if(pc.getEmployee().getId() == employee.getId()){ //Check if employee has already an active assignment
                return null;
            }
        }
    	ProjectCommitment projectCommitment = new ProjectCommitment(project, employee, hourlyRate);
        zem.persistObject(projectCommitment); //Save in database
        return projectCommitment;
    }

    /**
     * Inactivate a projectCommitment (assignment from an employee to the project). The activities will be remained
     * (if not removed by employee) and billed to the client. The employee can no more add activities to the project.
     * @param pc projectCommitment that has to be disabled
     */
    public static void inactivateProjectCommitment(ProjectCommitment pc){
        zem.startTransaction();
        pc.setActive(false); //End assignment to the project
        zem.endTransaction();
    }

    /**
     * Find a projectCommitment (project assignment from an employee) with his id
     * @param idProjectCommitment id from projectCommitment
     * @return the projectCommitment with the given id
     */
    public static ProjectCommitment getProjectCommitment(long idProjectCommitment){
        return (ProjectCommitment)zem.findObject(ProjectCommitment.class, idProjectCommitment);
    }

    /**
     * Get the projectCommitment from a given project where employees and projectCommitment are active
     * @param idProject id from the given project
     * @return a list of the active projectCommitment (the employee muss also be active)
     */
    public static List<ProjectCommitment> getProjectCommitmentList(long idProject){
        return zem.getProjectCommitmentWithProject(idProject);
    }

    /**
     * Get the activities from a given project
     * @param idProject id from the given project
     * @return a list of the activities who is ordered by the begin date (descending)
     */
    public static List<Activity> getActivitiesFromProject(long idProject){
        return zem.getAllActivitiesFromProject(idProject);
    }

    /**
     * Get the oldest activity (showing beginDate) from a project
     * @param idProject id from the given project
     * @return the oldest activity from the project
     */
    public static Activity getOldestActiviteFromProject(long idProject){
        return zem.getOldestActivityFromProject(idProject);
    }

    /**
     * Get the last activity (showing endDate) from a project
     * @param idProject id from the given project
     * @return the last activity from the project
     */
    public static Activity getLastActiviteFromProject(long idProject){
        return zem.getLastActivityFromProject(idProject);
    }

    /**
     * Get the activities from a project from a given month and year (endDate is showed)
     * @param idProject id from the given project
     * @param month wished month for the activities
     * @param year wished year for the activities
     * @return a list of the monthly activities who is ordered by employee and projectCommitment (ascending)
     */
    public static List<Activity> getMonthlyActivitiesFromProject(long idProject, int month, int year){
        return zem.getMonthlyActivitiesFromProject(idProject, month, year);
    }

    /**
     * Inactive activities from a given month and year (when activities are billed that user can't modify there entries)
     * @param monthlyActivities list of the activities that should be inactivated
     */
    public static void inactiveMonthlyActivitiesFromProject(List<Activity> monthlyActivities){
        zem.startTransaction(); //begin modifications in database
        for(Activity a : monthlyActivities){
            a.setActive(false); //inactive activity
        }
        zem.endTransaction(); //end modifications in database
    }

    /**
     * Get the costs (material costs, ... entered by an admin) from a project
     * @param idProject id from the given project
     * @return a list of costs ordered by date (descending)
     */
    public static List<Cost> getCosts(long idProject){
        return zem.getCosts(idProject);
    }

    /**
     * Add a cost to a project
     * @param name name of the cost
     * @param date date of the cost
     * @param price price of the cost
     * @param project project where the cost should be added
     * @param description description of the project
     * @return the new cost
     */
    public static Cost addCost(String name, Date date, double price, Project project, String description){
        Cost cost = new Cost(name, date, price, project, description);
        zem.persistObject(cost); //Save in database
        return cost;
    }

    /**
     * Remove a cost from a project. They will not be billed anymore to the client.
     * @param cost the cost that should be removed
     */
    public static void removeCost(Cost cost){
        zem.removeElement(cost); //Remove the cost from database
    }

    /**
     * Get the costs from a project from a given month and year
     * @param idProject id from the given project
     * @param month wished month for the costs
     * @param year wished year for the costs
     * @return a list of the monthly costs ordered by date (descending)
     */
    public static List<Cost> getMonthlyCosts(long idProject, int month, int year){
        return zem.getMonthlyCost(idProject, month, year);
    }

    /**
     * Get the sum of the costs that are given in parameter
     * @param costs costs that need to be summed up
     * @return the sum of the given costs
     */
    public static double getSumCosts(List<Cost> costs){
        //TODO use bigDecimal???
        double sum = 0;
        for (Cost c : costs){
            sum += c.getPrice(); //Sum up the price of the cost
        }
        return sum;
    }

    /**
     * Create BillingEmployeeItems from a given month and year. Each item will be a line in the bill of the month
     * that will be send to the client. They store the work done (hours and price) by each employee.
     * @param activitiesFromMonth list of the activities from a given month and year
     * @return list of the billingEmployeeItems that will be used to make the bill for the client.
     */
    public static List<BillingEmployeeItem> getBillingEmployeeItems(List<Activity> activitiesFromMonth){
        //TODO use bigDecimal???
        List<BillingEmployeeItem> billingList = new ArrayList<>(); //Creation of the billingEmployeeItem list
        if(activitiesFromMonth.size() == 0){ //Show if nothing has been done during that month
            return billingList; //return an empty list
        }

        ProjectCommitment currentPC = activitiesFromMonth.get(0).getProjectCommitment(); //get the first project assignement of the activity list
        double cost=0; //Initialise the price of the work done by the employee (projectCommitment)
        double totalHoures = 0; //Initialise the hours of work done by the employee (projectCommitment)
        double hourlyRate = activitiesFromMonth.get(0).getProjectCommitment().getHourlyRate(); //Get the hourly rate from the employee ((projectCommitment)
        for (Activity activity : activitiesFromMonth) {
            if (currentPC.getId() != activity.getProjectCommitment().getId()) { //Check if it is another employee (projectCommitment)
                billingList.add(new BillingEmployeeItem(currentPC, cost, totalHoures)); //Add work by employee (projectCommitment) for the bill
                cost=0; //Initialise the price of the work for the next employee (projectCommitment)
                totalHoures = 0; //Initialise the hours of work for the next employee (projectCommitment)
                currentPC = activity.getProjectCommitment(); //Get project commitment from next employee
                hourlyRate = activity.getProjectCommitment().getHourlyRate(); // Get hourly rate from next employee
            }

            //Calculate hours of work for the given activity
            double dif= ((double)(activity.getEndDate().getTime()-activity.getBeginDate().getTime()))/3600000;
            totalHoures+=dif; //Add to the hours of work done by the employee in the month
            cost+=dif*hourlyRate; //Add the price of the activity to the total sum
        }

        //Add work done for the last employee (projectCommitment)
        billingList.add(new BillingEmployeeItem(activitiesFromMonth.get(activitiesFromMonth.size()-1).getProjectCommitment(), cost, totalHoures));
        return billingList;

    }

    /**
     * Get the sum of the billingEmployeeItems given in parameter
     * @param billingEmployeeItems billingEmployeeItems that need to be summed up
     * @return the sum of the given billingEmployeeItems
     */
    public static double getSumBillingEmployeeItems(List<BillingEmployeeItem> billingEmployeeItems){
        //TODO use bigDecimal???
        double sum = 0;
        for (BillingEmployeeItem bei : billingEmployeeItems){
            sum += bei.getCost(); //Sum up the price of the work done by the employee (projectCommitment)
        }
        return sum;
    }

}
