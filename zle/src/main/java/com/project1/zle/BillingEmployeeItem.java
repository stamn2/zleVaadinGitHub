package com.project1.zle;

import com.project1.domain.ProjectCommitment;

public class BillingEmployeeItem {

	private ProjectCommitment pc;
	private double cost;
	private double hours;
	
	public BillingEmployeeItem(ProjectCommitment pc, double cost, double hours) {
		this.pc = pc;
		this.cost = cost;
		this.hours = hours;
	}

	public ProjectCommitment getPc() {
		return pc;
	}

	public double getCost() {
		return cost;
	}

	public double getHours() {
		return hours;
	}

}
