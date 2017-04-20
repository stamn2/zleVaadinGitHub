package com.project1.view.admin;

import com.project1.controller.UserController;
import com.project1.domain.Employee;
import com.project1.view.LoginView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;

public class EmployeeOverView extends CustomComponent implements View {

    public static final String NAME = "employeeOverview";

    private final Button addEmployee, logout;
    private final Grid employeesGrid;

    public EmployeeOverView(){
        addEmployee = new Button("Add Employee");
        addEmployee.setWidth("15%");
        addEmployee.addClickListener(e -> {
            getUI().getNavigator().navigateTo(EmployeeEditorView.NAME);
        });

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
			getUI().getSession().setAttribute("user", null);
			getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        List<Employee> employeeList = UserController.getActivesEmployees();
        BeanItemContainer<Employee> ds = new BeanItemContainer<Employee>(Employee.class, employeeList);
        employeesGrid = new Grid("Employees", ds);
        employeesGrid.setWidth("100%");


        VerticalLayout viewLayout = new VerticalLayout(logout, addEmployee, employeesGrid);
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
