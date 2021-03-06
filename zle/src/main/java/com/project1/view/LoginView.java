package com.project1.view;

import com.project1.controller.LoginController;
import com.project1.domain.Employee;
import com.project1.view.admin.AdminHomepageView;
import com.project1.view.admin.EmployeeEditorView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class LoginView extends CustomComponent implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8797094554963923839L;
	
	public static final String NAME = "";

	private final TextField userField;
	private final PasswordField passwordField;
	private final Button loginButton;
	private final Label titleLabel;
	private Image image;
	private VerticalLayout fields;
	private VerticalLayout viewLayout;

	public LoginView(){
		titleLabel = new Label("Zeit und Leistungserfassung - Login");
		titleLabel.addStyleName("titles");
		image = new Image(null, new ThemeResource("images/login.jpg"));
		image.setWidth("100%");

		// Create the user input field
		userField = new TextField("User:");
		userField.setWidth("100%");
		userField.setRequired(true);
		userField.setInputPrompt("Your username");
		userField.setValue("admin"); //TODO remove value for final version
		userField.setInvalidAllowed(false);
		// Create the password input field
		passwordField = new PasswordField("Password:");
		passwordField.setWidth("100%");
		passwordField.setRequired(true);
        passwordField.setInputPrompt("Your password");
		passwordField.setValue("1234Hallo"); //TODO remove value for final version
		passwordField.setInvalidAllowed(false);

		// Create login button
		loginButton = new Button("Login");
		loginButton.addClickListener( e -> {

			
			if(!userField.isValid() || !passwordField.isValid()){
				//Notification.show("Form is not filled correctly");
				return;
			}
			Employee emp = LoginController.login(userField.getValue(), passwordField.getValue());
			if(emp == null){
				Notification.show("Login was not successfull");
				return;
			}
			getUI().getSession().setAttribute("user", emp);
            if(emp.isChangePassword()){
                getUI().getNavigator().navigateTo(FirstLoginView.NAME);
            }
            else{
				if(emp.isAdmin()){
					getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
				}
                else{
					getUI().getNavigator().navigateTo(UserHomepageView.NAME);
				}
            }
		});

		// Add both to a panel
		fields = new VerticalLayout(titleLabel, image, userField, passwordField, loginButton);
		fields.setSpacing(true);
		fields.setWidth("50%");
		fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		// The view root layout
		viewLayout = new VerticalLayout(fields);
		viewLayout.setMargin(true);
		viewLayout.setSpacing(true);
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);

        setCompositionRoot(viewLayout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
        getUI().getPage().setTitle("Login");
	}

}
