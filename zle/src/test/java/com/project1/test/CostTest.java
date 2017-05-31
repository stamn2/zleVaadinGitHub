package com.project1.test;

import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Test;
import com.project1.domain.Cost;
import com.project1.domain.Project;

public class CostTest {
	
	Cost cost; 
	Date date;
	double price;
	Project project;
	
	public CostTest() {
		date=new Date();
		price=10;
		project = new Project();
		cost = new Cost("test",date, price, project,"test");
	}
	
	@Test
	public void getId(){
		assertTrue(cost.getId()>=0);
	}
	
	@Test
	public void getName(){
		assertEquals(cost.getName(), "test");
	}
	
	@Test
	public void getDate(){
		assertEquals(cost.getDate(), date);
	}
	
	@Test
	public void getPrice(){
		assertTrue(cost.getPrice()==price);
	}
	
	@Test
	public void getDescription(){
		assertEquals(cost.getDescription(), "test");
	}
	
	@Test
	public void getProject(){
		assertEquals(cost.getProject(), project);
	}
}
