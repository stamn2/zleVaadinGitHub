package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Activity;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillingView extends CustomComponent implements View {

    public static final String NAME = "BillingView";

    private final Button logout, back, preview;
    private ComboBox month, year;
    private VerticalLayout viewLayout;
    private Project project;

    public BillingView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });


        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/" + project.getId());
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        ArrayList<Integer> monthsList = new ArrayList<>();
        for(int i= 1; i<=12; i++){
            monthsList.add(i);
        }
        BeanItemContainer<Integer> monthsContainer = new BeanItemContainer<>(Integer.class, monthsList);

        month = new ComboBox("Month: ");
        month.setWidth("100%");
        month.setRequired(true);
        month.setInvalidAllowed(false);
        month.setContainerDataSource(monthsContainer);

        year = new ComboBox("Year: ");
        year.setWidth("100%");
        year.setRequired(true);
        year.setInvalidAllowed(false);

        preview = new Button("Preview");
        preview.setWidth("100%");
        preview.addClickListener(e ->{
            //TODO
        });

        HorizontalLayout billingLayer = new HorizontalLayout(month, year, preview);
        billingLayer.setSpacing(true);
        billingLayer.setWidth("60%");
        billingLayer.setMargin(true);
        billingLayer.setSpacing(true);

        viewLayout = new VerticalLayout(topLayer, billingLayer);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(billingLayer, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Billing");
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

        //TODO activity null
        Activity firstActivity = ProjectController.getOldestActiveActiviteFromProject(project.getId());
        Activity lastActivity = ProjectController.getLastActiveActiviteFromProject(project.getId());

        ArrayList<Integer> yearsList = new ArrayList<>();

        if(firstActivity != null && lastActivity != null){
            int yearBegin = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(firstActivity.getBeginDate())).getYear();
            int yearEnd = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(lastActivity.getEndDate())).getYear();
            for(int i= yearBegin; i<=yearEnd; i++){
                yearsList.add(i);
            }
        }
        else{
            Notification.show("Nothing to bill!");
        }

        BeanItemContainer<Integer> yearsContainer = new BeanItemContainer<>(Integer.class, yearsList);
        year.setContainerDataSource(yearsContainer);

        if(!project.isActive()){
            //TODO
        }

        setCompositionRoot(viewLayout);
    }

}
