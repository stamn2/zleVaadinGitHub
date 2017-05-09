package com.project1.view;

import com.project1.controller.ProjectController;
import com.project1.controller.RecordController;
import com.project1.domain.Activity;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.ProjectCommitment;
import com.project1.view.admin.AdminHomepageView;
import com.project1.view.admin.ProjectOverView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;

import java.util.Date;
import java.util.List;

public class ActivityRecordView extends CustomComponent implements View {

    public static final String NAME = "ActivityRecordView";

    private final Button logout, back, commit;
    private DateField dateBegin, dateEnd;
    private ComboBox project;
    private TextArea comment;
    private VerticalLayout viewLayout;
    private Activity activity;
    private ProjectCommitment projectCommitment;
    
    public ActivityRecordView(){
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

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
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

        commit = new Button("Commit"); //TODO check if begin date is after end date
        commit.setWidth("70%");
        commit.addClickListener(e -> {

            if (!dateBegin.isValid() || !dateEnd.isValid() || !project.isValid()) {
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
            } else {
                RecordController.addActivity(dateBegin.getValue(), dateEnd.getValue(),(ProjectCommitment) project.getValue() , comment.getValue());
            }
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
        long idEmployee = ((Employee)getUI().getSession().getAttribute("user")).getId();
        List<ProjectCommitment> projectList = RecordController.getProjectCommitmentListFromEmployee(idEmployee);
        BeanItemContainer<ProjectCommitment> ds = new BeanItemContainer<>(ProjectCommitment.class, projectList);
        ds.addNestedContainerBean("project");
        project.setContainerDataSource(ds);
        project.setItemCaptionPropertyId("project.name");
        
        activity = RecordController.getActivity(Long.parseLong(event.getParameters()));
        
        //TODO in ZLEEntityManager, correct query!
        //projectCommitment = ProjectController.getProjectCommitmentWithActivity(activity.getId());
        //project.setValue(projectCommitment.getProject());
        
        dateBegin.setValue(activity.getBeginDate());
        dateEnd.setValue(activity.getEndDate());
        comment.setValue(activity.getComment());


        setCompositionRoot(viewLayout);
    }


}
