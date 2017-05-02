package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

import java.util.Arrays;
import java.util.List;

public class ProjectOverView extends CustomComponent implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7120911827497326373L;

	public static final String NAME = "projectOverview";

    private final Button addProject, logout;
    private final Grid projectsGrid;

    public ProjectOverView(){
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
        ds.addNestedContainerBean("client");
        // Generate button caption column
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(ds);
        gpc.removeContainerProperty("active");
        gpc.removeContainerProperty("client.id");
        gpc.removeContainerProperty("client.street");
        gpc.removeContainerProperty("client.plz");
        gpc.removeContainerProperty("client.city");
        gpc.removeContainerProperty("client.email");
        gpc.removeContainerProperty("client.tel");
        gpc.addGeneratedProperty("show",
                new PropertyValueGenerator<String>() {
                    @Override
                    public String getValue(Item item, Object itemId,
                                           Object propertyId) {
                        return "Show"; // The caption
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });


        projectsGrid = new Grid("Projects", gpc); //TODO show client correctly
        projectsGrid.setWidth("100%");
        projectsGrid.getColumn("show")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        showProject(e.getItemId()))); //TODO edit object

        VerticalLayout viewLayout = new VerticalLayout(logout, addProject, projectsGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        setCompositionRoot(viewLayout);
    }

    private void showProject(Object e){
        Project p = (Project)e;
        getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/"+ p.getId());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
        }
    }
}
