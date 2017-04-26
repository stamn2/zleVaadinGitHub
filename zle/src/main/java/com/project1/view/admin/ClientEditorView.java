package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.view.LoginView;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class ClientEditorView extends CustomComponent implements View  {
    public static final String NAME = "ClientEditorView";

    private TextField idField, companyName, firstname, lastname, street, plz, city, email, tel;
    private Label titleLabel;
    private Button save, logout;
    private VerticalLayout fields;
    private VerticalLayout viewLayout;

    public ClientEditorView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener( e -> {
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        titleLabel = new Label("Zeit und Leistungserfassung - ClientEditor");
        titleLabel.addStyleName("titles");

        idField = new TextField("ID:");
        idField.setWidth("100%");
        idField.setReadOnly(true);

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
            }
            else{
                ProjectController.addClient(companyName.getValue(), firstname.getValue(), lastname.getValue(), street.getValue(),
                        plz.getValue(), city.getValue(), email.getValue(), tel.getValue());
                getUI().getNavigator().navigateTo(ClientOverView.NAME);
            }
        });

        // Add both to a panel
        fields = new VerticalLayout(titleLabel, idField, companyName, firstname, lastname, street, plzCity, email, tel, save);
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
        Notification.show("enter the Client-Editor");

    }

}
