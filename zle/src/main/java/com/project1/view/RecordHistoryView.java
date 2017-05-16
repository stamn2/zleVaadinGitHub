package com.project1.view;

import com.project1.controller.ProjectController;
import com.project1.controller.RecordController;
import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.domain.ProjectCommitment;
import com.project1.view.LoginView;
import com.project1.view.admin.AdminHomepageView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.derby.iapi.services.io.ArrayUtil;

public class RecordHistoryView extends CustomComponent implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7120911827497326373L;

	public static final String NAME = "recordHistoryView";

    private final Button logout, back;
    private Grid projectsGrid;
    private HorizontalLayout topLayer;

    private GeneratedPropertyContainer gpc;

    public RecordHistoryView(){

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });


        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            if(((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
                getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
            }
            else{
                getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            }
        });

        topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        
    }

    private void showActivity(Object e){
        Activity p = (Activity)e;
        getUI().getNavigator().navigateTo(ActivityRecordView.NAME + "/"+ p.getId());
    }

    private void removeActivity(Object e){
        Activity activity = (Activity)e;
        RecordController.removeActivity(activity);
        gpc.removeItem(activity);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        getUI().getPage().setTitle("History");
    	List<Activity> activityList = RecordController.getActivitiesFromEmployee(((Employee) getUI().getSession().getAttribute("user")).getId());
    	
        BeanItemContainer<Activity> ds = new BeanItemContainer<>(Activity.class, activityList);
        ds.addNestedContainerBean("projectCommitment");
        ds.addNestedContainerBean("projectCommitment.project");
        gpc = new GeneratedPropertyContainer(ds);
        gpc.removeContainerProperty("active");
        gpc.removeContainerProperty("realTimeRecord");
        gpc.removeContainerProperty("projectCommitment.id");
        gpc.removeContainerProperty("projectCommitment.employee");
        gpc.removeContainerProperty("projectCommitment.hourlyRate");
        gpc.removeContainerProperty("projectCommitment.active");
        gpc.removeContainerProperty("projectCommitment.project.id");
        gpc.removeContainerProperty("projectCommitment.project.active");
        gpc.removeContainerProperty("projectCommitment.project.client");
        gpc.addGeneratedProperty("edit",
                new PropertyValueGenerator<String>() {
                    @Override
                    public String getValue(Item item, Object itemId,
                                           Object propertyId) {
                        return "Edit"; // The caption
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });
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

        projectsGrid = new Grid("History of activities", gpc);
        projectsGrid.setColumnOrder("id","beginDate","endDate", "projectCommitment.project.name", "comment","edit", "remove");
        projectsGrid.setWidth("100%");
        projectsGrid.getColumn("comment").setExpandRatio(1);
        projectsGrid.getColumn("edit")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        showActivity(e.getItemId())));
        projectsGrid.getColumn("remove")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        removeActivity(e.getItemId())));

        VerticalLayout viewLayout = new VerticalLayout(topLayer, projectsGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
    }
}
