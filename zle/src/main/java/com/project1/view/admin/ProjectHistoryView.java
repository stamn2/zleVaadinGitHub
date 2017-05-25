package com.project1.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.project1.controller.ProjectController;
import com.project1.controller.RecordController;
import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class ProjectHistoryView extends CustomComponent implements View{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1235868544381734095L;

	public static final String NAME = "projectHistoryView";
	

    private Button logout, back;
    private HorizontalLayout fields,topLayer;
    private VerticalLayout viewLayout;
    private Grid projectsGrid;
    private Project project;
    private List<ProjectCommitment> projectCommitment;

    public ProjectHistoryView() {
    	 logout = new Button("Logout");
         logout.setWidth("15%");
         logout.addClickListener(e ->{
 			getUI().getSession().setAttribute("user", null);
 			getUI().getNavigator().navigateTo(LoginView.NAME);
         });
         
         
         back = new Button("Back");
         back.setWidth("15%");

         
         topLayer = new HorizontalLayout(back, logout);
         topLayer.setSpacing(true);
         topLayer.setWidth("100%");
         topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
         topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);
 	}

 	@Override
 	public void enter(ViewChangeEvent event) {
 		 getUI().getPage().setTitle("History");
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

		 List<Activity> activityList = ProjectController.getActivitiesFromProject(project.getId());

 	     BeanItemContainer<Activity> ds = new BeanItemContainer<>(Activity.class, activityList);
 	     ds.addNestedContainerBean("projectCommitment");
 	     ds.addNestedContainerBean("projectCommitment.employee");
 	     GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(ds);
 	     gpc.removeContainerProperty("projectCommitment.id");
 	     gpc.removeContainerProperty("projectCommitment.hourlyRate");
 	     gpc.removeContainerProperty("projectCommitment.active");
 	     gpc.removeContainerProperty("projectCommitment.project");
 	     gpc.removeContainerProperty("projectCommitment.employee.active");
 	     gpc.removeContainerProperty("projectCommitment.employee.admin");
 	     gpc.removeContainerProperty("projectCommitment.employee.changePassword");
 	     gpc.removeContainerProperty("projectCommitment.employee.city");
 	     gpc.removeContainerProperty("projectCommitment.employee.firstname");
 	     gpc.removeContainerProperty("projectCommitment.employee.lastname");
 	     gpc.removeContainerProperty("projectCommitment.employee.id");
 	     gpc.removeContainerProperty("projectCommitment.employee.plz");
 	     gpc.removeContainerProperty("projectCommitment.employee.street");
 	     gpc.removeContainerProperty("projectCommitment.employee.tel");
 	     gpc.removeContainerProperty("active");
 	     gpc.removeContainerProperty("id");
 	     gpc.removeContainerProperty("realTimeRecord");
 	        
 	        
 	     back.addClickListener(e ->{
			 getUI().getNavigator().navigateTo(ProjectDetailView.NAME+ "/"+ project.getId());
 	     });


 	     projectsGrid = new Grid("Activities", gpc);
 	     projectsGrid.setColumnOrder("beginDate", "endDate","projectCommitment.employee.email" ,"comment");
 	     projectsGrid.setWidth("100%");
 		 
 	     // The view root layout
 	     viewLayout = new VerticalLayout(topLayer, projectsGrid);
 	     viewLayout.setMargin(true);
 	     viewLayout.setSpacing(true);
 	     setCompositionRoot(viewLayout);
 	}

}
