package com.project1.controller;

import com.project1.domain.ProjectCommitment;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.List;

public class RecordController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static List<ProjectCommitment> getProjectCommitmentListFromEmployee(long idEmployee){
        return zem.getProjectCommitmentWithEmployee(idEmployee);
    }
	
}
