package com.project1.view.admin;

import com.project1.controller.ClientController;
import com.project1.controller.ProjectController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
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

public class ClientOverView extends CustomComponent implements View {

    public static final String NAME = "clientOverview";

    private final Button addClient, logout, back;
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


        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        List<Client> clientList = ClientController.getClients();
        BeanItemContainer<Client> ds = new BeanItemContainer<>(Client.class, clientList);
        // Generate button caption column
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(ds);

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


        clientsGrid = new Grid("Clients", gpc);
        clientsGrid.setWidth("100%");
        clientsGrid.getColumn("edit")
                .setRenderer(new ButtonRenderer(e ->{ // Java 8
                    Client client = (Client)e.getItemId();
                    getUI().getNavigator().navigateTo(ClientEditorView.NAME + "/" + client.getId());}));
        clientsGrid.setColumnOrder("id", "companyName", "firstname", "lastname", "street", "plz", "city", "email", "tel", "edit");

        VerticalLayout viewLayout = new VerticalLayout(topLayer, addClient, clientsGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Clients");
    }


}
