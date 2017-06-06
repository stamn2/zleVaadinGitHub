package com.project1.view.admin;

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
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;

import java.util.List;

public class EmployeeArchiveView extends CustomComponent implements View {
    public static final String NAME = "archivedEmployees";

    private final Button back, home, logout;
    private final Grid archivedEmployeesGrid;
    private GeneratedPropertyContainer gpc;

    public EmployeeArchiveView(){
        back = new Button("Back");
        back.setWidth("100%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(EmployeeOverView.NAME);
        });

        home = new Button("Home");
        home.setWidth("100%");
        home.addClickListener(e -> {
            getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
        });

        HorizontalLayout navigationLayer = new HorizontalLayout(back, home);
        navigationLayer.setSpacing(true);
        navigationLayer.setWidth("35%");

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        HorizontalLayout topLayer = new HorizontalLayout(navigationLayer, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(navigationLayer, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        List<Employee> archivedEmployeeList = UserController.getInactivesEmployees();
        BeanItemContainer<Employee> ds = new BeanItemContainer<>(Employee.class, archivedEmployeeList);
        gpc = new GeneratedPropertyContainer(ds);
        gpc.removeContainerProperty("password");
        gpc.removeContainerProperty("active");
        gpc.addGeneratedProperty("enable",
                new PropertyValueGenerator<String>() {
                    @Override
                    public String getValue(Item item, Object itemId,
                                           Object propertyId) {
                        return "Enable Account"; // The caption
                    }

                    @Override
                    public Class<String> getType() {
                        return String.class;
                    }
                });


        archivedEmployeesGrid = new Grid("Archived Employees", gpc);
        archivedEmployeesGrid.setWidth("100%");

        archivedEmployeesGrid.getColumn("enable")
                .setRenderer(new ButtonRenderer(e -> // Java 8
                        enableEmployee(e.getItemId())));

        archivedEmployeesGrid.setColumnOrder("id", "firstname", "lastname", "street", "plz", "city", "tel", "email","admin", "enable");

        VerticalLayout viewLayout = new VerticalLayout(topLayer, archivedEmployeesGrid);
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
        getUI().getPage().setTitle("Archived Employees");
    }

    private void enableEmployee(Object e){
        Employee employee = (Employee)e;
        boolean succcess = UserController.enableEmployeeAccount(employee);
        if(succcess){
            gpc.removeItem(e);
        }
        else{
            Notification.show("An active employee with this email exists already!");
        }
    }

}
