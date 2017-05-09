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

    public static void addActivity(Date dateBegin, Date dateEnd, ProjectCommitment project, String comment){
        Activity activity = new Activity(dateBegin, dateEnd, comment);
        zem.persistObject(activity);
        zem.startTransaction();
        project.addActivity(activity);
        zem.endTransaction();
    }

	public static Activity getActivity(long activityId) {
		return zem.getActivity(activityId);
	}
	
}
