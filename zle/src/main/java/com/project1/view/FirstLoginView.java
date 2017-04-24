package com.project1.view;

import com.project1.controller.LoginController;
import com.project1.domain.Employee;
import com.project1.view.admin.AdminHomepageView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

public class FirstLoginView extends CustomComponent implements View{
    public static final String NAME = "firstLogin";

    private final PasswordField oldPassword;
    private final PasswordField newPassword1, newPassword2;
    private final Button login;

    public FirstLoginView(){

        Label titleLabel = new Label("Your password must be changed");
        titleLabel.addStyleName("titles");

        // Create the old password input field
        oldPassword = new PasswordField("Old password:");
        oldPassword.setWidth("100%");
        oldPassword.setRequired(true);
        oldPassword.setInputPrompt("Old password");
        oldPassword.setInvalidAllowed(false);

        //TODO : check password security
        // Create the first new password input field
        newPassword1 = new PasswordField("New password:");
        newPassword1.setWidth("100%");
        newPassword1.setRequired(true);
        newPassword1.setInputPrompt("New Password");
        newPassword1.setInvalidAllowed(false);

        // Create the second new password input field
        newPassword2 = new PasswordField("new password:");
        newPassword2.setWidth("100%");
        newPassword2.setRequired(true);
        newPassword2.setInputPrompt("new password");
        newPassword2.setInvalidAllowed(false);

        // Create login button
        login = new Button("Login");
        login.addClickListener( e -> {
            if(!oldPassword.isValid() || !newPassword1.isValid() || !newPassword2.isValid()){
                //TODO: show wich fields are not valid
                Notification.show("Form is not filled correctly");
                return;
            }
            if(!newPassword1.getValue().equals(newPassword2.getValue())){
                Notification.show("The new password is not repeated correctly");
                return;
            }
        	Employee emp = (Employee) getUI().getSession().getAttribute("user");
            if(LoginController.changePassword(emp, oldPassword.getValue(), newPassword1.getValue())){
                emp = LoginController.getEmployee(emp.getEmail());
            	getUI().getSession().setAttribute("user", emp);
            	if(emp.isAdmin()){
                    getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
                }
                else{
                    getUI().getNavigator().navigateTo(UserHomepageView.NAME);
                }
            }
            else{
                Notification.show("Your old password is false!");
            }

        });

        VerticalLayout fields = new VerticalLayout(titleLabel, oldPassword, newPassword1, newPassword2, login);
        fields.setSpacing(true);
        fields.setWidth("50%");
        fields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("enter the first login view");
    }
}
