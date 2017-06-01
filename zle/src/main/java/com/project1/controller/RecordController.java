package com.project1.controller;

import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.Date;
import java.util.List;

/**
 * @author Rosalie Truong & Nils Stampfli
 */
public class RecordController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    /**
     * Get the projectCommitment (project assignments) from the employee where projectCommitment and project are active.
     * The real time assignment (system project) will not be in the list because employee should not assign work done in that project.
     * @param idEmployee id from the given employee
     * @return a list of the ProjectCommitment from the employee
     */
    public static List<ProjectCommitment> getProjectCommitmentListFromEmployee(long idEmployee){
        return zem.getProjectCommitmentWithEmployee(idEmployee);
    }

    /**
     * Add a new activity in the system
     * @param dateBegin begin date of the activity
     * @param dateEnd end date of the activity
     * @param comment comment of the activity
     * @param project projectCommitment where the activity should be assigned
     * @return the new activity
     */
    public static Activity addActivity(Date dateBegin, Date dateEnd, String comment, ProjectCommitment project){
        Activity activity = new Activity(dateBegin, dateEnd, comment, project);
        zem.persistObject(activity); //Save in database
        return activity;
    }

    /**
     * Find an activity with his id
     * @param activityId id from the activity
     * @return the activity with the given id
     */
	public static Activity getActivity(long activityId) {
        return (Activity)zem.findObject(Activity.class, activityId);
	}

    /**
     * Get all active activities from active project and projectCommitment. If an activity is being real time recording, it will not be in the list.
     * @param idEmployee id from the given employee
     * @return a list of the activities ordered by begin date (descending)
     */
    public static List<Activity> getActivitiesFromEmployee(long idEmployee){
        return zem.getActivitiesFromEmployee(idEmployee);
    }

    /**
     * Update the details from an activity
     * @param activity activity that need to be updated
     * @param dateBegin begin date of the activity
     * @param dateEnd end date of the activity
     * @param comment comment of the activity
     * @param project projectCommitment of the activity
     */
    public static void updateActivity(Activity activity, Date dateBegin, Date dateEnd, String comment, ProjectCommitment project){
        zem.startTransaction(); //begin modifications in database
        activity.setBeginDate(dateBegin);
        activity.setEndDate(dateEnd);
        activity.setComment(comment);
        activity.setProjectCommitment(project);
        zem.endTransaction(); //end modifications in database
    }

    /**
     * Remove an activity from a projectCommitment. It will not be billed to the client.
     * @param activity the activity that should be removed
     */
    public static void removeActivity(Activity activity){
        zem.removeElement(activity); //Remove the activity from database
    }

    /**
     * Get the activity from an employee that is real time recoding
     * @param idEmployee id from the given employee
     * @return the activity that is real time recording
     */
	public static Activity getRealTimeRecordActivity(long idEmployee) {
		return zem.getRealTimeRecordActivity(idEmployee);
	}

    /**
     * Begin a real time recording activity for an employee
     * @param emp Given employee who will do a real time recording
     * @return the activity that is real time recording
     */
	public static Activity startRealTimeRecording(Employee emp) {
        Project project= ProjectController.getRealTimeRecordingProject(); //Get the real time recording project

        if(project == null){ //If project was not found (should normally not happen) doesn't begin real time recording
            return null;
        }

        //Get assignment to the project
        ProjectCommitment pc = zem.getProjectCommitmentWithProjectAndEmployee(emp.getId(), project.getId());
        Activity activity = addActivity(new Date(), new Date(), "", pc); //Add an real time recording activity

		zem.startTransaction(); //begin modifications in database
		activity.setRealTimeRecord(true); //Indicate that the activity is performing a real time recording
		zem.endTransaction(); //end modifications in database

        return activity;
	}

    /**
     * End a real time recording activity for an employee
     * @param emp Given employee who will stop the real time recording
     * @return the activity that as done a real time recording
     */
	public static Activity stopRealTimeRecording(Employee emp) {
        Activity activity = RecordController.getRealTimeRecordActivity(emp.getId()); //Get real time recording activity

        if(activity == null) { //If no real time recording activity was found doesn't end real time recording
            return null;
        }

        zem.startTransaction(); //begin modifications in database
        activity.setEndDate(new Date()); //Give end date of the real time recording
		activity.setRealTimeRecord(false); //End real time recording
		zem.endTransaction(); //end modifications in database

        return activity;
	}
}
