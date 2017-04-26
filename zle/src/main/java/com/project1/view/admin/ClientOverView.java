package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Client;
import com.project1.view.LoginView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;

public class ClientOverView extends CustomComponent implements View {

    public static final String NAME = "clientOverview";

    private final Button addClient, logout;
    private final Grid clientsGrid;

    public ClientOverView(){
        addClient = new Button("Add Client");
        addClient.setWidth("15%");
        addClient.addClickListener(e -> {
            getUI().getNavigator().navigateTo(ClientEditorView.NAME);
        });

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        List<Client> clientList = ProjectController.getClients();
        BeanItemContainer<Client> ds = new BeanItemContainer<>(Client.class, clientList);
        // Generate button caption column
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(ds);


        clientsGrid = new Grid("Clients", gpc);
        clientsGrid.setWidth("100%");

        VerticalLayout viewLayout = new VerticalLayout(logout, addClient, clientsGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //TODO -> BIC.refresh()
        Notification.show("enter the client overview");
    }


}
