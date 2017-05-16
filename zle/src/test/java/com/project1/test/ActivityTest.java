package com.project1.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.domain.ProjectCommitment;

public class ActivityTest {
	
	Activity activity;
	ProjectCommitment projectCommitment, projectCommitment2;
	Date begin, end, begin2, end2;
	String comment, comment2;
	
	public ActivityTest() {
		projectCommitment = new ProjectCommitment();
		begin = new Date();
		end = new Date();
		comment = "comment";
	}
	
	@Before
	public void setUp(){
		activity = new Activity(begin, end, comment, projectCommitment);
	} 
	
	@Test
	public void getId(){
		assertTrue(activity.getId()>=0);
	}
	
	@Test
	public void isActive() {
		assertTrue(activity.isActive());
	}
	
	@Test
	public void getBeginDate() {
		assertEquals(begin, activity.getBeginDate());
	}
	
	@Test
	public void getEndDate() {
		assertEquals(end, activity.getEndDate());
	}
	
	@Test
	public void getComment() {
		assertEquals(comment, activity.getComment());
	}
	
	@Test
	public void getProjectCommitment() {
		assertEquals(projectCommitment, activity.getProjectCommitment());
	}
	
	@Test
	public void setProjectCommitmen() {
		projectCommitment2 = new ProjectCommitment();
		activity.setProjectCommitment(projectCommitment2);
		assertEquals(projectCommitment2,activity.getProjectCommitment());
	}
	
	@Test
	public void setBeginDate() {
		begin2 = new Date();
		activity.setBeginDate(begin2);
		assertEquals(begin2, activity.getBeginDate());
	}
	
	@Test
	public void setEndDate() {
		end2 = new Date();
		activity.setEndDate(end2);
		assertEquals(end2, activity.getEndDate());
	}
	
	@Test
	public void setComment() {
		comment2= "newComment";
		activity.setComment(comment2);
		assertEquals(comment2, activity.getComment());
	}
	
	@Test
	public void isSetRealTimeRecord() {
		assertFalse(activity.isRealTimeRecord());
	}
	
	@Test
	public void setRealTimeRecord() {
		activity.setRealTimeRecord(true);
		assertTrue(activity.isRealTimeRecord());
	}
	
}
