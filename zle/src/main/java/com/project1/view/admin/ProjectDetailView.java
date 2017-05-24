package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;

import javax.persistence.NoResultException;

public class ProjectDetailView extends CustomComponent implements View{

    /**
     *
     */
    private static final long serialVersionUID = -3779898619518576881L;

    public static final String NAME = "projectDetailView";

    private final Button cost, history, employees, editProject, endProject;
    private final Button logout, back;
    private Project project;
    private Label projectName;
    private TextArea infoText;
    private VerticalLayout viewLayout;

    public ProjectDetailView() {
        //TODO correct all navigateTo
        employees = new Button("Employees");
        employees.setWidth("80%");
        employees.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ProjectAssignmentView.NAME+ "/"+ project.getId());
        });

        history = new Button("History");
        history.setWidth("80%");
        history.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ProjectHistoryView.NAME+ "/"+ project.getId());
        });

        cost = new Button("Cost");
        cost.setWidth("80%");
        cost.addClickListener(e -> {
            getUI().getNavigator().navigateTo(CostOverView.NAME+ "/"+ project.getId());
        });

        editProject = new Button("Edit Project");
        editProject.setWidth("80%");
        editProject.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ProjectEditorView.NAME+ "/"+ project.getId());
        });

        endProject = new Button("End Project");
        endProject.setWidth("80%");
        endProject.addClickListener(e -> {
            ProjectController.endProject(project);
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
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
            if(project.isActive()){
                getUI().getNavigator().navigateTo(ProjectOverView.NAME);
            }
            else{
                getUI().getNavigator().navigateTo(ProjectArchiveView.NAME);
            }
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        VerticalLayout adminButtons = new VerticalLayout(employees,history,cost,editProject,endProject);
        adminButtons.setSpacing(true);
        adminButtons.setWidth("100%");
        adminButtons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        projectName = new Label();
        projectName.setHeight("2em");

        infoText = new TextArea();
        infoText.setHeight("250px");
        infoText.setWidth("100%");

        VerticalLayout projectInfo = new VerticalLayout(projectName, infoText);
        projectInfo.setSpacing(true);
        projectInfo.setWidth("100%");
        projectInfo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        HorizontalLayout homeLayout = new HorizontalLayout(adminButtons, projectInfo);
        homeLayout.setSpacing(true);
        homeLayout.setWidth("100%");
        homeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        viewLayout = new VerticalLayout(topLayer, homeLayout);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeEvent event) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Project Detail");
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

        projectName.setValue(project.getName());
        Client c = project.getClient();
        //TODO : show cost
        infoText.setValue("Project is active: " + project.isActive() + "\n" +
                        "Client: " + c.getCompanyName() + "\n" +
                        c.getFirstname() + " " + c.getLastname() + "\n" +
                        c.getStreet() + "\n" +
                        c.getPlz() + " " + c.getCity() + "\n" +
                        c.getEmail() + "\n" +
                        c.getTel() + "\n" +
                        "num. of Employees: " + ProjectController.getProjectCommitmentList(project.getId()).size() + "\n" +
                        "Cost: "
        );
        infoText.setReadOnly(true);

        if(!project.isActive()){
            editProject.setEnabled(false);
            endProject.setEnabled(false);
        }

        setCompositionRoot(viewLayout);
    }

}
