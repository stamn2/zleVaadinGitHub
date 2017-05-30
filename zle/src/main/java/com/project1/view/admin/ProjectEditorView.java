package com.project1.view.admin;

import com.project1.controller.ClientController;
import com.project1.controller.ProjectController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;

public class ProjectEditorView extends CustomComponent implements View {

    public static final String NAME = "ProjectEditorView";

    private TextField name;
    private ComboBox client;
    private Button save, logout, back;
    private VerticalLayout fields;
    private VerticalLayout viewLayout;

    private Project project;
    private boolean newProject = true;

    public ProjectEditorView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });


        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            if(newProject) {
                getUI().getNavigator().navigateTo(ProjectOverView.NAME);
            }
            else{
                getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/" + project.getId());
            }
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        
        
        name = new TextField("project name:");
        name.setWidth("100%");
        name.setRequired(true);
        name.setInputPrompt("name of the project");
        name.setInvalidAllowed(false);

        client = new ComboBox("Client: ");
        client.setWidth("100%");
        client.setRequired(true);
        client.setInvalidAllowed(false);

        save = new Button("SAVE");
        save.addClickListener(e -> {
            if(!name.isValid() || !client.isValid()){
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
                return;
            }
            if(newProject){
                project = ProjectController.addProject(name.getValue(), (Client)client.getValue());
            }
            else{
                ProjectController.updateProject(project, name.getValue(), (Client)client.getValue());
            }
            getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/" + project.getId());
        });

        // Add both to a panel
        fields = new VerticalLayout(name, client, save);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // The view root layout
        viewLayout = new VerticalLayout(topLayer, fields);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Project Editor");

        List<Client> clientList = ClientController.getClients();
        BeanItemContainer<Client> ds = new BeanItemContainer<>(Client.class, clientList);

        client.setContainerDataSource(ds);
        client.setItemCaptionPropertyId("companyName");//TODO : show firstname and lastname

        if(!event.getParameters().equals("")){
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
                //TODO change navigate to
                getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/" + project.getId());
                Notification.show("Project is ended");
                return;
            }

            name.setValue(project.getName());
            //TODO  why doesn't we need to do that in ActivityRecordView?
            for(Client c : clientList){
                if(c.getId() == project.getClient().getId()){
                    client.setValue(c);
                }
            }

            newProject = false;
        }

        setCompositionRoot(viewLayout);
    }

}
