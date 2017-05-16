package com.project1.view.admin;

import java.util.Date;

import com.project1.controller.ProjectController;
import com.project1.controller.RecordController;
import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.view.ActivityRecordView;
import com.project1.view.FirstLoginView;
import com.project1.view.LoginView;
import com.project1.view.RecordHistoryView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class AdminHomepageView extends CustomComponent implements View {
    public static final String NAME = "adminHomepage";

    private final Button projects, clients, employees;
    private final Button start, stop, manualEntry, history, logout;

    public AdminHomepageView(){
        projects = new Button("Projects");
        projects.setWidth("80%");
        projects.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
        });
        clients = new Button("Clients");
        clients.setWidth("80%");
        clients.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ClientOverView.NAME);
        });
        employees = new Button("Employees");
        employees.setWidth("80%");
        employees.addClickListener(e -> {
            getUI().getNavigator().navigateTo(EmployeeOverView.NAME);
        });
        
        
        start = new Button("Start");
        start.setWidth("80%");
        stop = new Button("Stop");
        stop.setWidth("80%");
        stop.setVisible(false);
        
        start.addClickListener( e -> {
            start.setVisible(false);
            stop.setVisible(true);
            RecordController.startRealTimeRecording((Employee) getUI().getSession().getAttribute("user"));
        });
        stop.addClickListener(e ->{
        	Activity activity = RecordController.stopRealTimeRecording((Employee) getUI().getSession().getAttribute("user"));
        	if(activity != null) {
                getUI().getNavigator().navigateTo(ActivityRecordView.NAME + "/" + activity.getId());
            }
        });

        manualEntry = new Button("Manual Entry");
        manualEntry.setWidth("80%");
        manualEntry.addClickListener( e ->{
            getUI().getNavigator().navigateTo(ActivityRecordView.NAME);
        });
        history = new Button("History");
        history.setWidth("80%");
        history.addClickListener( e ->{
            getUI().getNavigator().navigateTo(RecordHistoryView.NAME);
        });
        
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
			getUI().getSession().setAttribute("user", null);
			getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        VerticalLayout adminButtons = new VerticalLayout(projects, clients, employees);
        adminButtons.setSpacing(true);
        adminButtons.setWidth("100%");
        adminButtons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout userButtons = new VerticalLayout(start, stop, manualEntry, history);
        userButtons.setSpacing(true);
        userButtons.setWidth("100%");
        userButtons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        HorizontalLayout homeLayout = new HorizontalLayout(adminButtons, userButtons);
        homeLayout.setSpacing(true);
        homeLayout.setWidth("100%");
        homeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout viewLayout = new VerticalLayout(logout, homeLayout);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Homepage");
        
        if(RecordController.getRealTimeRecordActivity(((Employee)getUI().getSession().getAttribute("user")).getId())!=null){
            start.setVisible(false);
            stop.setVisible(true);
        }
    }

}
