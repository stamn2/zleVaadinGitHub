package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import java.util.Date;

public class CostEditorView extends CustomComponent implements View {

    public static final String NAME = "CostEditorView";

    private final Button logout, back, home, commit;
    private TextField name, price;
    private DateField date;
    private TextArea description;

    private Project project;

    public CostEditorView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        back = new Button("Back");
        back.setWidth("100%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(CostOverView.NAME + "/" + project.getId());
        });

        home = new Button("Home");
        home.setWidth("100%");
        home.addClickListener(e -> {
            getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
        });

        HorizontalLayout navigationLayer = new HorizontalLayout(back, home);
        navigationLayer.setSpacing(true);
        navigationLayer.setWidth("35%");

        HorizontalLayout topLayer = new HorizontalLayout(navigationLayer, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(navigationLayer, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        name = new TextField("Name:");
        name.setWidth("100%");
        name.setRequired(true);
        name.setInvalidAllowed(false);

        date = new DateField("Date:");
        date.setValue(new Date());
        date.setWidth("100%");
        date.setRequired(true);
        date.setInvalidAllowed(false);
        date.setResolution(Resolution.DAY);

        price = new TextField("Price:");
        price.setWidth("100%");
        price.setRequired(true);
        price.setInvalidAllowed(false);
        price.addValidator(new RegexpValidator("^-?[0-9]*\\.?[0-9]{0,2}$",
                "It must be a number of max 2 digits after comma"));

        description = new TextArea("Comment :");
        description.setWidth("100%");

        commit = new Button("Commit");
        commit.setWidth("70%");
        commit.addClickListener(e -> {

            if (!name.isValid() || !date.isValid() || !price.isValid()) {
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
                return;
            }
            //Todo check parsing?
            ProjectController.addCost(name.getValue(), date.getValue(), Double.parseDouble(price.getValue()), project, description.getValue());
            getUI().getNavigator().navigateTo(CostOverView.NAME + "/" + project.getId());
        });

        VerticalLayout fields = new VerticalLayout(name, date, price, description, commit);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setComponentAlignment(commit, Alignment.MIDDLE_CENTER);

        VerticalLayout viewLayout = new VerticalLayout(topLayer, fields);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Cost Editor");
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
        if(!project.isActive()){
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
            Notification.show("Project is ended");
            return;
        }
    }

}
