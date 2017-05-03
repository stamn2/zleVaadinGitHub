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

public class RecordHistoryView extends CustomComponent implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7120911827497326373L;

	public static final String NAME = "recordHistoryView";

    private final Button logout, back;
    private Grid projectsGrid;
    private List<ProjectCommitment> projectCommitmentList;
    private HorizontalLayout topLayer;
    private List<Activity> activityList;
    

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
            getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
        });

        topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        
    }

    private void showProject(Object e){
        Project p = (Project)e;
        getUI().getNavigator().navigateTo(ActivityRecordView.NAME + "/"+ p.getId()); //TODO get&fill with values of THIS activity
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    	projectCommitmentList = RecordController.getProjectCommitmentListFromEmployee(((Employee)getUI().getSession().getAttribute("user")).getId());
    	activityList = new ArrayList<>();
    	projectCommitmentList.forEach(e->{
    		activityList = (List<Activity>)ArrayUtils.addAll(activityList,e.getActivityList());
    	});
        BeanItemContainer<ProjectCommitment> ds = new BeanItemContainer<>(ProjectCommitment.class, projectCommitmentList);
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


        projectsGrid = new Grid("Projects", gpc); //TODO show client correctly
        projectsGrid.setWidth("100%");
        projectsGrid.getColumn("show")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        showProject(e.getItemId()))); //TODO edit object

        VerticalLayout viewLayout = new VerticalLayout(topLayer, projectsGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
    }
}
