package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;

public class ProjectOverView extends CustomComponent implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7120911827497326373L;

	public static final String NAME = "projectOverview";

    private final Button addProject, logout, link;
    private final Grid projectsGrid;

    public ProjectOverView(){
        link = new Button("link");
        link.setWidth("15%");
        link.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ProjectDetailView.NAME);
        });
    	
        addProject = new Button("Add Project");
        addProject.setWidth("15%");
        addProject.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ProjectEditorView.NAME);
        });

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getNavigator().navigateTo(ProjectEditorView.NAME);
        });

        List<Project> projectList = ProjectController.getProjects();
        BeanItemContainer<Project> ds = new BeanItemContainer<>(Project.class, projectList);
        // Generate button caption column
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(ds);


        projectsGrid = new Grid("Projects", gpc);
        projectsGrid.setWidth("100%");

        VerticalLayout viewLayout = new VerticalLayout(link, logout, addProject, projectsGrid);
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
        }
    }
}
