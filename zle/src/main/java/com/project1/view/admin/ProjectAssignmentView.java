package com.project1.view.admin;

import java.util.List;

import com.project1.controller.ProjectController;
import com.project1.controller.UserController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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
    private HorizontalLayout fields;
    private VerticalLayout viewLayout;
    private final Grid projectsGrid;
    
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
        
        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);
		
        hourlyRate = new TextField("Hourly Rate:");
        hourlyRate.setWidth("100%");
        hourlyRate.setRequired(true);
        hourlyRate.setInvalidAllowed(false);

        List<Employee> empList = UserController.getActivesEmployees();
        BeanItemContainer<Employee> dsEmp = new BeanItemContainer<>(Employee.class, empList);

        employee = new ComboBox("Employee: ");
        employee.setWidth("100%");
        employee.setRequired(true);
        employee.setInvalidAllowed(false);
        employee.setContainerDataSource(dsEmp);
        employee.setItemCaptionPropertyId("email");//TODO : show firstname and lastname

        save = new Button("ADD");
        save.addClickListener(e -> {
            if(!hourlyRate.isValid() || !employee.isValid()){
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
            }
            else{
               //TODO replace-> ProjectController.addProject(hourlyRate.getValue(), (Client)employee.getValue());
                //getUI().getNavigator().navigateTo(ProjectDetailView.NAME); //TODO
            }
        });
        
        List<Employee> empsList = ProjectController.getEmployees();
        BeanItemContainer<Employee> dsEmps = new BeanItemContainer<>(Employee.class, empsList);
        // Generate button caption column
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(dsEmps);


        projectsGrid = new Grid("Projects", gpc);
        projectsGrid.setWidth("100%");

        // Add both to a panel
        
        
        fields = new HorizontalLayout(employee, hourlyRate, save);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // The view root layout
        viewLayout = new VerticalLayout(topLayer, fields, projectsGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

        setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		 if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
	            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
		 }
	}

}
