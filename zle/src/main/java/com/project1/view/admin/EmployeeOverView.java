package com.project1.view.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class EmployeeOverView extends CustomComponent implements View {

    public static final String NAME = "employeeOverview";

    private final Button addEmployee, logout;

    public EmployeeOverView(){
        addEmployee = new Button("Add Employee");
        addEmployee.setWidth("15%");
        addEmployee.addClickListener(e -> {
            getUI().getNavigator().navigateTo(EmployeeEditorView.NAME);
        });

        logout = new Button("Logout");
        logout.setWidth("15%");

        VerticalLayout viewLayout = new VerticalLayout(logout, addEmployee);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("enter the employee overview");
    }

}
