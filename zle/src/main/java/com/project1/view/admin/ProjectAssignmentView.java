package com.project1.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.project1.controller.ProjectController;
import com.project1.controller.UserController;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.validator.RegexpValidator;
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
import com.vaadin.ui.renderers.ButtonRenderer;

public class ProjectAssignmentView extends CustomComponent implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1269545402609103007L;
	
	public static final String NAME = "projectAssignmentView";
	
    private TextField hourlyRate;
    private ComboBox employee;
    private Button addAssignment, logout, back, home;
    private HorizontalLayout fields,topLayer;
    private VerticalLayout viewLayout;
    private Grid projectsGrid;
    private Project project;
    private List<ProjectCommitment> projectCommitment;
    private GeneratedPropertyContainer gpc;
    private BeanItemContainer<ProjectCommitment> dsEmps;


    //TODO doesn't show inactive employee
	public ProjectAssignmentView() {
		
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
			getUI().getSession().setAttribute("user", null);
			getUI().getNavigator().navigateTo(LoginView.NAME);
        });
        
        back = new Button("Back");
        back.setWidth("100%");

        home = new Button("Home");
        home.setWidth("100%");
        home.addClickListener(e -> {
            getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
        });

        HorizontalLayout navigationLayer = new HorizontalLayout(back, home);
        navigationLayer.setSpacing(true);
        navigationLayer.setWidth("35%");

        topLayer = new HorizontalLayout(navigationLayer, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(navigationLayer, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);
		
        hourlyRate = new TextField("Hourly Rate:");
        hourlyRate.setWidth("100%");
        hourlyRate.setRequired(true);
        hourlyRate.setInvalidAllowed(false);
        hourlyRate.addValidator(new RegexpValidator("^[0-9]*\\.?[0-9]{0,2}$",
                "It must be a number of max 2 digits after comma"));


        List<Employee> AllEmpsList = UserController.getActivesEmployees();
	    BeanItemContainer<Employee> dsEmp = new BeanItemContainer<>(Employee.class, AllEmpsList);	        

        employee = new ComboBox("Employee: ");
        employee.setWidth("100%");
        employee.setRequired(true);
        employee.setInvalidAllowed(false);
        employee.setContainerDataSource(dsEmp);
        employee.setItemCaptionPropertyId("email");

        addAssignment = new Button("ADD");
        addAssignment.addClickListener(e -> {
            if (!hourlyRate.isValid() || !employee.isValid()) {
                Notification.show("Form is not filled correctly");
            } else {
                ProjectCommitment pc = ProjectController.addProjectCommitment(project, (Employee) employee.getValue(), Double.valueOf(hourlyRate.getValue()).doubleValue());
                if (pc == null) {
                    Notification.show("Employee is already assigned to the project");
                } else {
                    Page.getCurrent().reload();
                }
            }
        });
              
        fields = new HorizontalLayout(employee, hourlyRate, addAssignment);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		 if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
	         getUI().getNavigator().navigateTo(UserHomepageView.NAME);
			 return;
		 }
         getUI().getPage().setTitle("Project Assignment");
        try{
            project = ProjectController.getProject(Long.parseLong(event.getParameters()));
        }
        catch(NumberFormatException e){
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
            Notification.show("URL is not valid");
            return;
        }

        if(project == null){
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
            Notification.show("URL is not valid");
            return;
        }

		 projectCommitment = ProjectController.getProjectCommitmentList(project.getId());
		 
	        List<Employee> empList = new ArrayList<>();
	        projectCommitment.forEach(e->{
	        	empList.add(e.getEmployee());
	        });
	        
	        dsEmps = new BeanItemContainer<>(ProjectCommitment.class, projectCommitment);
	        dsEmps.addNestedContainerBean("employee");
	        // Generate button caption column
	        gpc = new GeneratedPropertyContainer(dsEmps);
	        gpc.removeContainerProperty("project");
	        gpc.removeContainerProperty("active");
	        gpc.removeContainerProperty("activities");
	        gpc.removeContainerProperty("employee.id");
	        gpc.removeContainerProperty("employee.street");
	        gpc.removeContainerProperty("employee.plz");
	        gpc.removeContainerProperty("employee.city");
	        gpc.removeContainerProperty("employee.tel");
	        gpc.removeContainerProperty("employee.active");
	        gpc.removeContainerProperty("employee.admin");
	        gpc.removeContainerProperty("employee.changePassword");
	        gpc.addGeneratedProperty("inactivate",
	                new PropertyValueGenerator<String>() {
	                    @Override
	                    public String getValue(Item item, Object itemId,
	                                           Object propertyId) {
	                        return "Inactivate"; // The caption
	                    }

	                    @Override
	                    public Class<String> getType() {
	                        return String.class;
	                    }
	                });
	        
	        
	        
	        back.addClickListener(e ->{
				getUI().getNavigator().navigateTo(ProjectDetailView.NAME+ "/"+ project.getId());
	        });


	        projectsGrid = new Grid("ProjectCommitments", gpc);
	        projectsGrid.setColumnOrder("id", "employee.email","employee.firstname" ,"employee.lastname"  , "hourlyRate");
	        projectsGrid.setWidth("100%");
	        
	        projectsGrid.getColumn("inactivate")
            .setRenderer(new ButtonRenderer(e ->{ // Java 8      		
                    ProjectController.inactivateProjectCommitment((ProjectCommitment)e.getItemId());
                    //gpc.removeItem(e);
                    Page.getCurrent().reload();
                    }));
		 
	        // The view root layout
	        viewLayout = new VerticalLayout(topLayer, fields, projectsGrid);
	        viewLayout.setMargin(true);
	        viewLayout.setSpacing(true);
	        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

            if(!project.isActive()){
                addAssignment.setEnabled(false);
                projectsGrid.getColumn("inactivate").setHidden(true);
            }
	        setCompositionRoot(viewLayout);
	}
	

}
