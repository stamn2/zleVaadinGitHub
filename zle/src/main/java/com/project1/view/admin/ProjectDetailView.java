package com.project1.view.admin;

import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ProjectDetailView extends CustomComponent implements View{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3779898619518576881L;

	public static final String NAME = "projectDetailView";

	 private final Button cost, history, employees, editProject, endProject;
	 private final Button logout, back;
	 private Project project;
	 
	public ProjectDetailView() {
		//TODO correct all navigateTo
        employees = new Button("Employees");
        employees.setWidth("80%");
        employees.addClickListener(e -> {
            //getUI().getNavigator().navigateTo(EmployeeOverView.NAME);
        });
		
        history = new Button("History");
        history.setWidth("80%");
        history.addClickListener(e -> {
           // getUI().getNavigator().navigateTo(ProjectOverView.NAME);
        });
        cost = new Button("Cost");
        cost.setWidth("80%");
        cost.addClickListener(e -> {
            //getUI().getNavigator().navigateTo(ClientOverView.NAME);
        });
        
        editProject = new Button("Edit Project");
        editProject.setWidth("80%");
        editProject.addClickListener(e -> {
           //getUI().getNavigator().navigateTo(ClientOverView.NAME);
        });
        
        endProject = new Button("End Project");
        endProject.setWidth("80%");
        endProject.addClickListener(e -> {
           // getUI().getNavigator().navigateTo(ClientOverView.NAME);
        });

        
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
			getUI().getSession().setAttribute("user", null);
			getUI().getNavigator().navigateTo(LoginView.NAME);
        });
        
        
        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
			getUI().getNavigator().navigateTo(ProjectOverView.NAME);
        });
        
        //TODO make buttons-alignment correct -.-
        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(back, Alignment.TOP_RIGHT);
        
        VerticalLayout adminButtons = new VerticalLayout(employees,history,cost,editProject,endProject);
        adminButtons.setSpacing(true);
        adminButtons.setWidth("100%");
        adminButtons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        
        Label projectName = new Label();//TODO real projectName
        //projectName.setValue(project.getName());
        projectName.setValue("ProjectX");
        projectName.setHeight("2em");
        
        TextArea infoText = new TextArea(); //TODO real values of the project
        /*infoText.setValue("State: "+project.getState()+"\n"+
        		"Client: "+project.getClient()+"\n"+
        		"# of Employees: "+project.getNumbOfEmp()+"\n"+
        		"Cost: "+project.getCost()); */
        infoText.setValue("infoText");
        infoText.setReadOnly(true);
        
        
        VerticalLayout projectInfo = new VerticalLayout(projectName, infoText);
        projectInfo.setSpacing(true);
        projectInfo.setWidth("100%");
        projectInfo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        
        HorizontalLayout homeLayout = new HorizontalLayout(adminButtons, projectInfo);
        homeLayout.setSpacing(true);
        homeLayout.setWidth("100%");
        homeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        
        VerticalLayout viewLayout = new VerticalLayout(topLayer, homeLayout);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		 if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
	            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
		 }
	}

}
