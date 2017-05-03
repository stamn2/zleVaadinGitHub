package com.project1.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.project1.controller.ProjectController;
import com.project1.controller.UserController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ProjectAssignmentView extends CustomComponent implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1269545402609103007L;
	
	public static final String NAME = "projectAssignmentView";
	
    private TextField hourlyRate;
    private ComboBox employee;
    private Button save, logout, back;
    private HorizontalLayout fields,topLayer;
    private VerticalLayout viewLayout;
    private Grid projectsGrid;
    private Project project;
    private List<ProjectCommitment> projectCommitment;
    
	public ProjectAssignmentView() {
		
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
        
        topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);
		
        hourlyRate = new TextField("Hourly Rate:");
        hourlyRate.setWidth("100%");
        hourlyRate.setRequired(true);
        hourlyRate.setInvalidAllowed(false);


        List<Employee> AllEmpsList = ProjectController.getEmployees();
	    BeanItemContainer<Employee> dsEmp = new BeanItemContainer<>(Employee.class, AllEmpsList);	        

        employee = new ComboBox("Employee: ");
        employee.setWidth("100%");
        employee.setRequired(true);
        employee.setInvalidAllowed(false);
        employee.setContainerDataSource(dsEmp);
        employee.setItemCaptionPropertyId("email");

        save = new Button("ADD");
        save.addClickListener(e -> {
            if(!hourlyRate.isValid() || !employee.isValid()){
                //TODO: IF EMPLOYEE ALREADY ASSIGNET!
                Notification.show("Form is not filled correctly");
            }
            else{
               ProjectController.addProjectCommitment(project, (Employee)employee.getValue(), Double.valueOf(hourlyRate.getValue()).doubleValue());
               Page.getCurrent().reload();
            }
        });
              
        fields = new HorizontalLayout(employee, hourlyRate, save);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		 if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
	            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
		 }
		 project = ProjectController.getProject(Long.parseLong(event.getParameters())); //TODO check id
		 projectCommitment = ProjectController.getProjectCommitmentList(project.getId());
		 
	        List<Employee> empList = new ArrayList<>();
	        projectCommitment.forEach(e->{
	        	empList.add(e.getEmployee());
	        });
	        
	        BeanItemContainer<ProjectCommitment> dsEmps = new BeanItemContainer<>(ProjectCommitment.class, projectCommitment);
	        dsEmps.addNestedContainerBean("employee");
	        // Generate button caption column
	        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(dsEmps);
	        gpc.removeContainerProperty("project");
	        gpc.removeContainerProperty("active");
	        gpc.removeContainerProperty("employee.id");
	        gpc.removeContainerProperty("employee.street");
	        gpc.removeContainerProperty("employee.plz");
	        gpc.removeContainerProperty("employee.city");
	        gpc.removeContainerProperty("employee.tel");
	        gpc.removeContainerProperty("employee.active");
	        gpc.removeContainerProperty("employee.admin");
	        gpc.removeContainerProperty("employee.changePassword");
	        gpc.removeContainerProperty("employee.firstname");
	        gpc.removeContainerProperty("employee.lastname");


	        projectsGrid = new Grid("Projects", gpc);
	        projectsGrid.setColumnOrder("id", "employee.email", "hourlyRate");
	        projectsGrid.setWidth("100%");
		 
	        // The view root layout
	        viewLayout = new VerticalLayout(topLayer, fields, projectsGrid);
	        viewLayout.setMargin(true);
	        viewLayout.setSpacing(true);
	        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
	        setCompositionRoot(viewLayout);
	}

}
