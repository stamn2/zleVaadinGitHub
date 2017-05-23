package com.project1.view.admin;

import com.project1.controller.ClientController;
import com.project1.controller.ProjectController;
import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class ClientEditorView extends CustomComponent implements View  {
    public static final String NAME = "ClientEditorView";

    private TextField idField, companyName, firstname, lastname, street, plz, city, email, tel;
    private Button save, logout, back;
    private VerticalLayout viewLayout;

    private Client client;
    private boolean newClient = true;

    public ClientEditorView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });


        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(ClientOverView.NAME);
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        Label titleLabel = new Label("Zeit und Leistungserfassung - ClientEditor");
        titleLabel.addStyleName("titles");

        idField = new TextField("ID:");
        idField.setWidth("100%");

        companyName = new TextField("company name:");
        companyName.setWidth("100%");
        companyName.setRequired(true);
        companyName.setInputPrompt("company name of the client");
        companyName.setInvalidAllowed(false);

        firstname = new TextField("firstName:");
        firstname.setWidth("100%");
        firstname.setRequired(true);
        firstname.setInputPrompt("firstName of the client");
        firstname.setInvalidAllowed(false);

        lastname = new TextField("lastName:");
        lastname.setWidth("100%");
        lastname.setRequired(true);
        lastname.setInputPrompt("lastName of the client");
        lastname.setInvalidAllowed(false);

        street = new TextField("street:");
        street.setWidth("100%");
        street.setRequired(true);
        street.setInputPrompt("street of the client");
        street.setInvalidAllowed(false);

        //TODO: validate plz
        plz = new TextField("plz:");
        plz.setWidth("100%");
        plz.setRequired(true);
        plz.setInputPrompt("plz of the client");
        plz.setInvalidAllowed(false);

        city = new TextField("city:");
        city.setWidth("100%");
        city.setRequired(true);
        city.setInputPrompt("city of the client");
        city.setInvalidAllowed(false);

        HorizontalLayout plzCity = new HorizontalLayout(plz, city);
        plzCity.setSpacing(true);
        plzCity.setWidth("100%");

        email = new TextField("email:");
        email.setWidth("100%");
        email.setRequired(true);
        //TODO : catch error?
        email.addValidator(new EmailValidator(
                "Must be a valid email!"));
        email.setInputPrompt("email of the client");
        email.setInvalidAllowed(false);

        //TODO: validate phone number
        tel = new TextField("tel:");
        tel.setWidth("100%");
        tel.setRequired(true);
        tel.setInputPrompt("tel of the client");
        tel.setInvalidAllowed(false);

        save = new Button("SAVE");
        save.addClickListener(e -> {

            if(!companyName.isValid() || !firstname.isValid() || !lastname.isValid() || !street.isValid() || !plz.isValid() || !city.isValid()
                    || !email.isValid() || !tel.isValid()){
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
                return;
            }
            if(newClient){
                ClientController.addClient(companyName.getValue(), firstname.getValue(), lastname.getValue(), street.getValue(),
                        plz.getValue(), city.getValue(), email.getValue(), tel.getValue());
                getUI().getNavigator().navigateTo(ClientOverView.NAME);
            }
            else{
                ClientController.updateClient(client, companyName.getValue(), firstname.getValue(), lastname.getValue(), street.getValue(),
                        plz.getValue(), city.getValue(), email.getValue(), tel.getValue());
                getUI().getNavigator().navigateTo(ClientOverView.NAME);
            }
        });

        // Add both to a panel
        VerticalLayout fields = new VerticalLayout(titleLabel, idField, companyName, firstname, lastname, street, plzCity, email, tel, save);
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
        getUI().getPage().setTitle("Client editor");

        if(!event.getParameters().equals("")){

            try{
                client = ClientController.getClient(Long.parseLong(event.getParameters()));
            }
            catch(NumberFormatException e){
                getUI().getNavigator().navigateTo(ClientOverView.NAME);
                Notification.show("URL is not valid");
                return;
            }

            if(client == null){
                getUI().getNavigator().navigateTo(ClientOverView.NAME);
                Notification.show("URL is not valid");
                return;
            }

            idField.setValue(String.valueOf(client.getId()));
            idField.setReadOnly(true);
            companyName.setValue(client.getCompanyName());
            firstname.setValue(client.getFirstname());
            lastname.setValue(client.getLastname());
            street.setValue(client.getStreet());
            plz.setValue(client.getPlz());
            city.setValue(client.getCity());
            email.setValue(client.getEmail());
            tel.setValue(client.getTel());

            newClient = false;
        }

        setCompositionRoot(viewLayout);
    }

}
