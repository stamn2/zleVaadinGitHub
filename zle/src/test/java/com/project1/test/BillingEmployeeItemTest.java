package com.project1.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.project1.domain.ProjectCommitment;
import com.project1.zle.BillingEmployeeItem;

public class BillingEmployeeItemTest {
	BillingEmployeeItem billEmpItem;
	double cost, hours;
	ProjectCommitment pc;
	
	public BillingEmployeeItemTest() {
		cost = 1;
		hours = 1;
		pc = new ProjectCommitment();
		billEmpItem = new BillingEmployeeItem(pc, cost, hours);
	}
	
	@Test
	public void getPc(){
		assertTrue(billEmpItem.getPc().getId()==pc.getId());
	}
	
	@Test
	public void getHours(){
		assertTrue(billEmpItem.getHours()==1);
	}
	
	@Test
	public void getCost(){
		assertTrue(billEmpItem.getCost()==1);
	}
}
