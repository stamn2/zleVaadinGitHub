package com.project1.view;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.project1.controller.ClientController;
import com.project1.controller.ProjectController;
import com.project1.controller.UserController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.admin.*;
import com.project1.view.user.UserHomepageView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;



/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4628885312461454476L;
	

	@Override
    protected void init(VaadinRequest vaadinRequest) {
		
    	getPage().setTitle("MainUI");
    	// i create the navigator, add a view and change/navigate to it
    	new Navigator(this, this);
    	getNavigator().addView("", LoginView.class);
        getNavigator().addView(FirstLoginView.NAME, FirstLoginView.class);
        getNavigator().addView(AdminHomepageView.NAME, AdminHomepageView.class);
		getNavigator().addView(UserHomepageView.NAME, UserHomepageView.class);
		getNavigator().addView(ActivityRecordView.NAME, ActivityRecordView.class);
		getNavigator().addView(RecordHistoryView.NAME, RecordHistoryView.class);
        getNavigator().addView(EmployeeOverView.NAME, EmployeeOverView.class);
		getNavigator().addView(EmployeeEditorView.NAME, EmployeeEditorView.class);
		getNavigator().addView(EmployeeArchiveView.NAME, EmployeeArchiveView.class);
		getNavigator().addView(ClientOverView.NAME, ClientOverView.class);
		getNavigator().addView(ClientEditorView.NAME, ClientEditorView.class);
		getNavigator().addView(ProjectOverView.NAME, ProjectOverView.class);
		getNavigator().addView(ProjectArchiveView.NAME, ProjectArchiveView.class);
		getNavigator().addView(ProjectEditorView.NAME, ProjectEditorView.class);
		getNavigator().addView(ProjectDetailView.NAME, ProjectDetailView.class);
		getNavigator().addView(ProjectAssignmentView.NAME, ProjectAssignmentView.class);
		getNavigator().addView(ProjectHistoryView.NAME, ProjectHistoryView.class);
		getNavigator().addView(CostOverView.NAME, CostOverView.class);
		getNavigator().addView(CostEditorView.NAME, CostEditorView.class);
		getNavigator().addView(BillingView.NAME, BillingView.class);
        
		getNavigator().addViewChangeListener(new ViewChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3549061311437753168L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				boolean isLoggedIn = getSession().getAttribute("user") != null;
				boolean isLoginView = event.getNewView() instanceof LoginView;
				if (!isLoggedIn && !isLoginView) {
					getNavigator().navigateTo(LoginView.NAME);
					return false;
				}else if (isLoggedIn && isLoginView) {
                    if(((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
                        getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
                    }
                    else{
                        getUI().getNavigator().navigateTo(UserHomepageView.NAME);
                    }
					return false;
				}
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});
        
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(900); // auto logout after 15min
    }
	

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        private static final String PERSISTENCE_UNIT_NAME = "zleDB";
        private static EntityManagerFactory factory; 
        private EntityManager em;
        Employee emp;
    	
        @Override
        protected void servletInitialized() throws ServletException {
        	super.servletInitialized();
        	System.out.println("init done...");
        	if(ProjectController.getRealTimeRecordingProject() == null){
                Client systemClient = ClientController.addClient("system", "system", "system", "system", "system", "system", "system", "system");
				ProjectController.addProject("No Project", systemClient);
				ProjectController.addProject("RealTimeRecording", systemClient);
			}
			UserController.addEmployee("admin", "admin", "admin", "admin", "admin", "admin", "admin",true);
		}
    	
    }
}
