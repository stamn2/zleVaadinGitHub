package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Cost;
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
import com.vaadin.ui.renderers.DateRenderer;

import java.util.List;
import java.util.Locale;

public class CostOverView  extends CustomComponent implements View {

    public static final String NAME = "costOverview";

    private final Button addCost, logout, back, home;
    private HorizontalLayout topLayer;

    private GeneratedPropertyContainer gpc;

    private Project project;

    public CostOverView(){
        addCost = new Button("Add Cost");
        addCost.setWidth("15%");
        addCost.addClickListener(e -> {
            getUI().getNavigator().navigateTo(CostEditorView.NAME+ "/"+ project.getId());
        });

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        back = new Button("Back");
        back.setWidth("100%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/" + project.getId());
        });

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
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Costs");
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

        List<Cost> costList = ProjectController.getCosts(project.getId());
        BeanItemContainer<Cost> ds = new BeanItemContainer<>(Cost.class, costList);
        // Generate button caption column
        gpc = new GeneratedPropertyContainer(ds);
        gpc.removeContainerProperty("project");

        gpc.addGeneratedProperty("remove",
                new PropertyValueGenerator<String>() {
                    @Override
                    public String getValue(Item item, Object itemId,
                                           Object propertyId) {
                        return "Remove"; // The caption
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });

        Grid costGrid = new Grid("Costs", gpc);
        costGrid.setWidth("100%");
        costGrid.setColumnOrder("id", "date", "name", "price", "description");
        costGrid.getColumn("date").setRenderer(new DateRenderer("%1$td %1$tb %1$tY"));
        costGrid.getColumn("remove")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        removeCost(e.getItemId())));

        VerticalLayout viewLayout = new VerticalLayout(topLayer, addCost, costGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        if(!project.isActive()){
            addCost.setEnabled(false);
            costGrid.getColumn("remove").setHidden(true);
        }

        setCompositionRoot(viewLayout);
    }

    private void removeCost(Object e){
        Cost cost = (Cost)e;
        ProjectController.removeCost(cost);
        gpc.removeItem(cost);
    }

}
