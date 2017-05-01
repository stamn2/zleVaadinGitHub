package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Employee;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class ProjectEditorView extends CustomComponent implements View {

    public static final String NAME = "ProjectEditorView";

    private TextField name;
    private ComboBox client;
    private Button save, logout;
    private VerticalLayout fields;
    private VerticalLayout viewLayout;

    public ProjectEditorView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener( e -> {
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        name = new TextField("company name:");
        name.setWidth("100%");
        name.setRequired(true);
        name.setInputPrompt("company name of the client");
        name.setInvalidAllowed(false);

        client = new ComboBox("Client: ");
        client.setWidth("100%");
        client.setRequired(true);
        client.setInvalidAllowed(false);
        client.addItem(ProjectController.getClients()); //TODO : choose client

        save = new Button("SAVE");
        save.addClickListener(e -> {
            if(!name.isValid()){
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
            }
            else{
                //TODO
                //ProjectController.addClient(name.getValue(), client);
                //getUI().getNavigator().navigateTo(ProjectDetailView.NAME);
            }
        });

        // Add both to a panel
        fields = new VerticalLayout(name, client);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // The view root layout
        viewLayout = new VerticalLayout(logout, fields);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        setCompositionRoot(viewLayout);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
        }
    }

}
