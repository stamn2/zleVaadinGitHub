package com.project1.view;

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
        oldPassword.setValue("");
        oldPassword.setNullRepresentation("");

        //TODO : check password security
        // Create the first new password input field
        newPassword1 = new PasswordField("New password:");
        newPassword1.setWidth("100%");
        newPassword1.setRequired(true);
        newPassword1.setValue("");
        newPassword1.setNullRepresentation("");

        // Create the second new password input field
        newPassword2 = new PasswordField("Confirme new password:");
        newPassword2.setWidth("100%");
        newPassword2.setRequired(true);
        newPassword2.setValue("");
        newPassword2.setNullRepresentation("");

        // Create login button
        login = new Button("Login");

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
