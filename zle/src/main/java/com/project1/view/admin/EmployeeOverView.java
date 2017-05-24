package com.project1.view.admin;

import com.project1.controller.LoginController;
import com.project1.controller.UserController;
import com.project1.domain.Employee;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

import java.util.List;

import org.apache.derby.tools.sysinfo;

public class EmployeeOverView extends CustomComponent implements View {

    public static final String NAME = "employeeOverview";

    private final Button back, logout, addEmployee, archive;
    private final Grid employeesGrid;
    private GeneratedPropertyContainer gpc;

    public EmployeeOverView(){
        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
        });

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        addEmployee = new Button("Add Employee");
        addEmployee.setWidth("90%");
        addEmployee.addClickListener(e -> {
            getUI().getNavigator().navigateTo(EmployeeEditorView.NAME);
        });

        archive = new Button("Archive");
        archive.setWidth("90%");
        archive.addClickListener(e -> {
            getUI().getNavigator().navigateTo(EmployeeArchiveView.NAME);
        });

        HorizontalLayout actionLayer = new HorizontalLayout(addEmployee, archive);
        actionLayer.setSpacing(true);
        actionLayer.setWidth("40%");

        List<Employee> employeeList = UserController.getActivesEmployees();
        BeanItemContainer<Employee> ds = new BeanItemContainer<>(Employee.class, employeeList);
        gpc = new GeneratedPropertyContainer(ds);
        gpc.removeContainerProperty("password");
        gpc.removeContainerProperty("active");
        gpc.addGeneratedProperty("generate Password",
                new PropertyValueGenerator<String>() {
                    @Override
                    public String getValue(Item item, Object itemId,
                                           Object propertyId) {
                        return "Gen. Password"; // The caption
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });
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
        gpc.addGeneratedProperty("disable",
                new PropertyValueGenerator<String>() {
                    @Override
                    public String getValue(Item item, Object itemId,
                                           Object propertyId) {
                        return "Disable Account"; // The caption
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });


        employeesGrid = new Grid("Employees", gpc);
        employeesGrid.setWidth("100%");

        employeesGrid.getColumn("generate Password")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                	generatePassword((Employee)e.getItemId())));
        employeesGrid.getColumn("edit")
                .setRenderer(new ButtonRenderer(e ->{ // Java 8
                        Employee emp = (Employee)e.getItemId();
                        getUI().getNavigator().navigateTo(EmployeeEditorView.NAME + "/" + emp.getId());}));
        employeesGrid.getColumn("disable")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        disableEmployee(e.getItemId())));

        employeesGrid.setColumnOrder("id", "firstname", "lastname", "street", "plz", "city", "tel", "email","admin", "changePassword", "edit", "disable");

        VerticalLayout viewLayout = new VerticalLayout(topLayer, actionLayer, employeesGrid);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setCompositionRoot(viewLayout);
    }

    public void generatePassword(Employee employee) {
    	UserController.generatedNewPassword(employee);
        Page.getCurrent().reload();
    	}

	@Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Employees");
    }

    private void disableEmployee(Object e){
        Employee employee = (Employee)e;
        UserController.disableEmployeeAccount(employee);
        gpc.removeItem(e);
    }

}
