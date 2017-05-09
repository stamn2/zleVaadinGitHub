package com.project1.controller;

import com.project1.domain.Activity;
import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.Date;
import java.util.List;

public class RecordController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static List<ProjectCommitment> getProjectCommitmentListFromEmployee(long idEmployee){
        return zem.getProjectCommitmentWithEmployee(idEmployee);
    }

    public static void addActivity(Date dateBegin, Date dateEnd, String comment, ProjectCommitment project){
        Activity activity = new Activity(dateBegin, dateEnd, comment, project);
        zem.persistObject(activity);
    }

	public static Activity getActivity(long activityId) {
		return zem.getActivity(activityId);
	}

    public static List<Activity> getActivitiesFromEmployee(long idEmployee){
        return zem.getActivitiesFromEmployee(idEmployee);
    }

    public static void updateActivity(Activity activity, Date dateBegin, Date dateEnd, String comment, ProjectCommitment project){
        zem.startTransaction();
        activity.setBeginDate(dateBegin);
        activity.setEndDate(dateEnd);
        activity.setComment(comment);
        activity.setProjectCommitment(project);
        zem.endTransaction();
    }
	
}
