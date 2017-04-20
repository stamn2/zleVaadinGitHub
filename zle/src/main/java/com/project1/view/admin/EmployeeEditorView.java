package com.project1.view.admin;

import com.project1.controller.UserController;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class EmployeeEditorView extends CustomComponent implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9187631726501669589L;
	public static final String NAME = "EmployeeEditorView";
	
	private TextField idField, firstName, lastName, street, plz, city, email, tel;
	private Label titleLabel;
	private Button save;
	private VerticalLayout fields;
	private VerticalLayout viewLayout;

	public EmployeeEditorView(){
        final VerticalLayout layout = new VerticalLayout();
        Button button = new Button("change to Login-View");
        button.addClickListener( e -> {
        	getUI().getNavigator().navigateTo("");
        });
        
        titleLabel = new Label("Zeit und Leistungserfassung - EmployeeEditor");
        titleLabel.addStyleName("titles");
        
        idField = new TextField("ID:");
        idField.setWidth("100%");
        idField.setReadOnly(true);
        
        firstName = new TextField("firstName:");
        firstName.setWidth("100%");
        firstName.setRequired(true);
        firstName.setInputPrompt("firstName of the Employee");
        firstName.setInvalidAllowed(false);
        
        lastName = new TextField("lastName:");
        lastName.setWidth("100%");
        lastName.setRequired(true);
        lastName.setInputPrompt("lastName of the Employee");
        lastName.setInvalidAllowed(false);
        
        street = new TextField("street:");
        street.setWidth("100%");
        street.setRequired(true);
        street.setInputPrompt("street of the Employee");
        street.setInvalidAllowed(false);

        //TODO: validate plz
        plz = new TextField("plz:");
        plz.setWidth("100%");
        plz.setRequired(true);
        plz.setInputPrompt("plz of the Employee");
        plz.setInvalidAllowed(false);
        
        city = new TextField("city:");
        city.setWidth("100%");
        city.setRequired(true);
        city.setInputPrompt("city of the Employee");
        city.setInvalidAllowed(false);
        
        HorizontalLayout plzCity = new HorizontalLayout(plz, city);
        plzCity.setSpacing(true);
        plzCity.setWidth("100%");
        //plzCity.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        
        email = new TextField("email:");
        email.setWidth("100%");
        email.setRequired(true);
        //TODO : catch error?
            email.addValidator(new EmailValidator(
                    "Must be a valid email!"));
        email.setInputPrompt("email of the Employee");
        email.setInvalidAllowed(false);

        //TODO: validate phone number
        tel = new TextField("tel:");
        tel.setWidth("100%");
        tel.setRequired(true);
        tel.setInputPrompt("tel of the Employee");
        tel.setInvalidAllowed(false);
        
        save = new Button("SAVE");
        save.addClickListener(e -> {
        	
                if(!firstName.isValid() || !lastName.isValid() || !street.isValid() || !plz.isValid() || !city.isValid()
                        || !email.isValid() || !tel.isValid()){
                    //TODO: show wich fields are not valid
                    Notification.show("Form is not filled correctly");
                    return;
                }
                else{
                    if(UserController.addEmployee(email.getValue(), firstName.getValue(), lastName.getValue(), street.getValue(),
                            plz.getValue(), city.getValue(), tel.getValue())){
                            getUI().getNavigator().navigateTo(EmployeeOverView.NAME);}
                    else{
                            Notification.show("An active employee with this email exists already!");
                    }
                }
        });
        
		// Add both to a panel
		fields = new VerticalLayout(titleLabel, idField, firstName, lastName, street, plzCity, email, tel, save);
		fields.setSpacing(true);
		fields.setWidth("50%");
		fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		// The view root layout
		viewLayout = new VerticalLayout(fields);
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        
        
        layout.addComponents(button,viewLayout);
        layout.setMargin(true);
        layout.setSpacing(true);
        setCompositionRoot(layout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("enter the Emp-Editor");
		
	}


}
