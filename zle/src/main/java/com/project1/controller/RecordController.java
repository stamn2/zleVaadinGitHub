package com.project1.controller;

import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.Date;
import java.util.List;

public class RecordController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static List<ProjectCommitment> getProjectCommitmentListFromEmployee(long idEmployee){
        return zem.getProjectCommitmentWithEmployee(idEmployee);
    }

    public static Activity addActivity(Date dateBegin, Date dateEnd, String comment, ProjectCommitment project){
        Activity activity = new Activity(dateBegin, dateEnd, comment, project);
        zem.persistObject(activity);
        return activity;
    }

	public static Activity getActivity(long activityId) {
        return (Activity)zem.findObject(Activity.class, activityId);
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

    public static void removeActivity(Activity activity){
        zem.removeElement(activity);
    }

	public static Activity getRealTimeRecordActivity(long idEmployee) {
		return zem.getRealTimeRecordActivity(idEmployee);
	}

	public static void setRealTimeRecord(Activity realTimeRecord) {
		zem.startTransaction();
		realTimeRecord.setRealTimeRecord(true);
		zem.endTransaction();
	}

	public static void unsetRealTimeRecord(Activity realTimeRecord) {
		zem.startTransaction();
		realTimeRecord.setRealTimeRecord(false);
		zem.endTransaction();
	}
}
