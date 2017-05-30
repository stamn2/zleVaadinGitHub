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

import java.util.List;

public class ProjectArchiveView extends CustomComponent implements View {

    public static final String NAME = "projectArchiveView";

    private final Button back, logout;
    private final Grid projectsGrid;

    public ProjectArchiveView(){
        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
        });

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        List<Project> projectList = ProjectController.getArchivedProjects();
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

        projectsGrid = new Grid("Archived Projects", gpc);
        Grid.HeaderRow hr = projectsGrid.prependHeaderRow();
        hr.join("id", "name").setHtml("<b>Project</b>");
        hr.join("client.companyName", "client.firstname", "client.lastname").setHtml("<b>Client</b>");
        projectsGrid.setWidth("100%");
        projectsGrid.getColumn("show")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        showProject(e.getItemId())));

        VerticalLayout viewLayout = new VerticalLayout(topLayer, projectsGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
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
            return;
        }
        getUI().getPage().setTitle("Archived Projects");
    }
}