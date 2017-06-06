package com.project1.view;

import com.project1.controller.RecordController;
import com.project1.domain.Activity;
import com.project1.domain.Employee;
import com.project1.domain.ProjectCommitment;
import com.project1.view.admin.AdminHomepageView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

public class ActivityRecordView extends CustomComponent implements View {

    public static final String NAME = "ActivityRecordView";

    private final Button logout, back, home, commit;
    private DateField dateBegin, dateEnd;
    private ComboBox project;
    private TextArea comment;
    private VerticalLayout viewLayout;

    private Activity activity;
    private boolean newActivity = true;

    public ActivityRecordView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });


        back = new Button("Back");
        back.setWidth("100%");
        back.addClickListener(e ->{
            if(newActivity){
                if(((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
                    getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
                }
                else{
                    getUI().getNavigator().navigateTo(UserHomepageView.NAME);
                }
            }
            else{
                getUI().getNavigator().navigateTo(RecordHistoryView.NAME);
            }
        });

        home = new Button("Home");
        home.setWidth("100%");
        home.addClickListener(e -> {
            if(((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
                getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
            }
            else{
                getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            }
        });

        HorizontalLayout navigationLayer = new HorizontalLayout(back, home);
        navigationLayer.setSpacing(true);
        navigationLayer.setWidth("35%");

        HorizontalLayout topLayer = new HorizontalLayout(navigationLayer, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(navigationLayer, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        dateBegin = new DateField("Begin Date and Time:");
        dateBegin.setValue(new Date());
        dateBegin.setWidth("100%");
        dateBegin.setRequired(true);
        dateBegin.setInvalidAllowed(false);
        dateBegin.setResolution(Resolution.SECOND);

        dateEnd = new DateField("End Date and Time:");
        dateEnd.setValue(new Date());
        dateEnd.setWidth("100%");
        dateEnd.setRequired(true);
        dateEnd.setInvalidAllowed(false);
        dateEnd.setResolution(Resolution.SECOND);



        project = new ComboBox("Project: ");
        project.setWidth("100%");
        project.setRequired(true);
        project.setInvalidAllowed(false);

        comment = new TextArea("Comment :");
        comment.setWidth("100%");

        commit = new Button("Commit");
        commit.setWidth("70%");
        commit.addClickListener(e -> {

            if (!dateBegin.isValid() || !dateEnd.isValid() || !project.isValid()) {
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
                return;
            }
            else if(!dateBegin.getValue().before(dateEnd.getValue())){
                Notification.show("The begin date is after end date");
                return;
            }
            if(newActivity){
                RecordController.addActivity(dateBegin.getValue(), dateEnd.getValue(), comment.getValue(), (ProjectCommitment) project.getValue());
            }
            else{
                RecordController.updateActivity(activity, dateBegin.getValue(), dateEnd.getValue(), comment.getValue(), (ProjectCommitment) project.getValue());
            }
            getUI().getNavigator().navigateTo(RecordHistoryView.NAME);
        });

        VerticalLayout fields = new VerticalLayout(dateBegin, dateEnd, project, comment, commit);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setComponentAlignment(commit, Alignment.MIDDLE_CENTER);

        viewLayout = new VerticalLayout(topLayer, fields);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        getUI().getPage().setTitle("Activity record");
        long idEmployee = ((Employee)getUI().getSession().getAttribute("user")).getId();
        List<ProjectCommitment> projectList = RecordController.getProjectCommitmentListFromEmployee(idEmployee);
        BeanItemContainer<ProjectCommitment> ds = new BeanItemContainer<>(ProjectCommitment.class, projectList);
        ds.addNestedContainerBean("project");
        project.setContainerDataSource(ds);
        project.setItemCaptionPropertyId("project.name");
        
        if(!event.getParameters().equals("")){

            try{
                activity = RecordController.getActivity(Long.parseLong(event.getParameters()));
            }
            catch(NumberFormatException e){
                getUI().getNavigator().navigateTo(RecordHistoryView.NAME);
                Notification.show("URL is not valid");
                return;
            }

            if(activity == null){
                getUI().getNavigator().navigateTo(RecordHistoryView.NAME);
                Notification.show("URL is not valid");
                return;
            }

            if(activity.getProjectCommitment().getEmployee().getId() != idEmployee){
                getUI().getNavigator().navigateTo(RecordHistoryView.NAME);
                Notification.show("Access is not allowed");
                return;
            }

            dateBegin.setValue(activity.getBeginDate());
            dateEnd.setValue(activity.getEndDate());
            project.setValue(activity.getProjectCommitment());
            comment.setValue(activity.getComment());

            newActivity = false;
        }

        setCompositionRoot(viewLayout);
    }


}
