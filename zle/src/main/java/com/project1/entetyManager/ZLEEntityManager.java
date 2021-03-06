package com.project1.entetyManager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.project1.domain.*;


public class ZLEEntityManager {
	
	private static final String PERSISTENCE_UNIT_NAME = "zleDB";
    private static EntityManagerFactory factory; 
    private EntityManager em;
	
	public ZLEEntityManager() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    em = factory.createEntityManager();
	}

	public ZLEEntityManager(String PERSISTENCE_UNIT_NAME) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    em = factory.createEntityManager();
	}
	
	public Employee getEmployee(String email){
		if (email == null)
			throw new IllegalArgumentException("email is null");
		Query q = em.createQuery("select o from Employee o where o.email='"+email+"' and o.active = true");
		try {
			Employee employee = (Employee) q.getSingleResult();
			em.refresh(employee);
			return employee;
		} catch (NoResultException e) {
			return null; 
		}
	}

	public List<Employee> getActiveEmployees(){
		Query q = em.createQuery("select o from Employee o where o.active = true");
		List<Employee> employeeList = q.getResultList();
		for(Employee e : employeeList) {
			em.refresh(e);
		}
		return employeeList;
	}

	public List<Employee> getInactiveEmployees(){
		Query q = em.createQuery("select o from Employee o where o.active = false");
		List<Employee> employeeList = q.getResultList();
		for(Employee e : employeeList) {
			em.refresh(e);
		}
		return employeeList;
	}

	public List<Client> getClients(){
		Query q = em.createQuery("select o from Client o where o.email<>\"system\"");
		List<Client> clientList = q.getResultList();
		for(Client c : clientList) {
			em.refresh(c);
		}
		return clientList;
	}

	public List<Project> getActivePojects(){
		Query q = em.createQuery("select o from Project o where o.active = true and o.client.email<>\"system\"");
		List<Project> projectList = q.getResultList();
		for(Project p : projectList) {
			em.refresh(p);
		}
		return projectList;
	}

	public List<Project> getInactivePojects(){
		Query q = em.createQuery("select o from Project o where o.active = false and o.client.email<>\"system\"");
		List<Project> projectList = q.getResultList();
		for(Project p : projectList) {
			em.refresh(p);
		}
		return projectList;
	}

	public List<Project> getSystemProjects(){
		Query q = em.createQuery("select o from Project o where o.client.email=\"system\"");
		List<Project> projectList = q.getResultList();
		for(Project p : projectList) {
			em.refresh(p);
		}
		return projectList;
	}
	
	public List<ProjectCommitment> getProjectCommitmentWithProject(long idProject){
		Query q = em.createQuery("select o from ProjectCommitment o where o.active=true AND o.employee.active=true AND o.project.id ="+idProject);
		List<ProjectCommitment> projectCommitmenttList = q.getResultList();
		for(ProjectCommitment p : projectCommitmenttList) {
			em.refresh(p);
		}
		return projectCommitmenttList;
	}

	public List<ProjectCommitment> getProjectCommitmentWithEmployee(long idEmployee){
		Query q = em.createQuery("select o from ProjectCommitment o where o.active=true AND o.project.active=true AND o.employee.id ="+idEmployee + " AND NOT (o.project.client.email=\"system\" AND o.project.name=\"RealTimeRecording\")");
		List<ProjectCommitment> projectCommitmenttList = q.getResultList();
		for(ProjectCommitment p : projectCommitmenttList) {
			em.refresh(p);
		}
		return projectCommitmenttList;
	}

	public ProjectCommitment getProjectCommitmentWithProjectAndEmployee(long idEmp, long idProject) {
		Query q = em.createQuery("select o from ProjectCommitment o where o.active=true AND o.project.active = true AND o.employee.id = "+idEmp+" AND o.project.id = "+idProject);
		try {
			ProjectCommitment projectCommitment = (ProjectCommitment) q.getSingleResult();
			em.refresh(projectCommitment);
			return projectCommitment;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Activity> getAllActivitiesFromProject(long idProject){
		Query q = em.createQuery("select a from Activity a where a.projectCommitment.project.id ="+idProject+ " ORDER BY a.beginDate DESC");
		List<Activity> activityList = q.getResultList();
		for(Activity a : activityList) {
			em.refresh(a);
		}
		return activityList;
	}

	public List<Activity> getMonthlyActivitiesFromProject(long idProject, int month, int year){
		Query q = em.createQuery("select a from Activity a where a.projectCommitment.project.id ="+idProject+ " AND FUNC('MONTH',a.endDate)="+month+ " AND FUNC('YEAR',a.endDate)="+year+ " ORDER BY a.projectCommitment.employee.id ASC, a.projectCommitment.id ASC");
		List<Activity> activityList = q.getResultList();
		for(Activity a : activityList) {
			em.refresh(a);
		}
		return activityList;
	}

	public Activity getOldestActivityFromProject(long idProject){
		Query q = em.createQuery("select a from Activity a where a.projectCommitment.project.id ="+idProject+ " ORDER BY a.beginDate ASC");
		q.setMaxResults(1);
		List<Activity> activityList = q.getResultList();
		if(activityList.size() == 1){
			Activity a = activityList.get(0);
			em.refresh(a);
			return a;
		}
		return null;
	}

	public Activity getLastActivityFromProject(long idProject){
		Query q = em.createQuery("select a from Activity a where a.projectCommitment.project.id ="+idProject+ " ORDER BY a.endDate DESC");
		q.setMaxResults(1);
		List<Activity> activityList = q.getResultList();
		if(activityList.size() == 1){
			Activity a = activityList.get(0);
			em.refresh(a);
			return a;
		}
		return null;
	}

	public List<Activity> getActivitiesFromEmployee(long idEmployee){
		Query q = em.createQuery("select a from Activity a where a.active = true AND a.projectCommitment.project.active = true AND a.realTimeRecord=false AND a.projectCommitment.employee.id ="+idEmployee+ " ORDER BY a.beginDate DESC");
		List<Activity> activityList = q.getResultList();
		for(Activity a : activityList) {
			em.refresh(a);
		}
		return activityList;
	}
	
	public Activity getRealTimeRecordActivity(long idEmployee) {
		Query q = em.createQuery("select a from Activity a where a.realTimeRecord=true AND a.projectCommitment.employee.id ="+idEmployee);
		try {
			Activity activity = (Activity) q.getSingleResult();
			em.refresh(activity);
			return activity;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Cost> getCosts(long idProject){
		Query q = em.createQuery("select c from Cost c where c.project.id="+idProject+ " ORDER BY c.date DESC");
		List<Cost> costList = q.getResultList();
		for(Cost c : costList) {
			em.refresh(c);
		}
		return costList;
	}

	public List<Cost> getMonthlyCost(long idProject, int month, int year){
		Query q = em.createQuery("select c from Cost c where c.project.id="+idProject+ " AND FUNC('MONTH',c.date)="+month+ " AND FUNC('YEAR',c.date)="+year+" ORDER BY c.date DESC");
		List<Cost> costList = q.getResultList();
		for(Cost c : costList) {
			em.refresh(c);
		}
		return costList;
	}

	public void persistObject(Object o){
		if (o == null)
			throw new IllegalArgumentException("Object is null");
		startTransaction();
		em.persist(o);
		em.getTransaction().commit();

	}
	
	public void startTransaction(){
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
	}

	
	public void endTransaction(){
		if(em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}

	
	public Object findObject(Class c, Long id){
		Object o = em.find(c, id);
		if(o != null){
			em.refresh(o);
		}
		return o;
	}

	public void removeElement(Object o) {
		if (o == null)
			throw new IllegalArgumentException("object is null");
		startTransaction();
		em.remove(o);
		em.getTransaction().commit();
	}
}
