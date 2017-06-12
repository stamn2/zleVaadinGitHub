package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.domain.Activity;
import com.project1.domain.Cost;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.project1.zle.BillingEmployeeItem;
import com.project1.zle.PDFCreater;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.FooterRow;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//TODO : rounding the values
public class BillingView extends CustomComponent implements View {

    public static final String NAME = "BillingView";

    private Button logout, back, home, preview, printPDF;
    private ComboBox month, year;
    private VerticalLayout viewLayout;
    private Grid employeeCostGrid, matCostGrid;
    private GeneratedPropertyContainer gpc, gpc2;
    private Label totalCost;

    private List<Activity> activitiesFromMonth;
    private List<BillingEmployeeItem> billingList;
    private List<Cost> costList;

    private Project project;

    public BillingView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });


        back = new Button("Back");
        back.setWidth("100%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/" + project.getId());
        });

        home = new Button("Home");
        home.setWidth("100%");
        home.addClickListener(e -> {
            getUI().getNavigator().navigateTo(AdminHomepageView.NAME);
        });

        HorizontalLayout navigationLayer = new HorizontalLayout(back, home);
        navigationLayer.setSpacing(true);
        navigationLayer.setWidth("35%");

        HorizontalLayout topLayer = new HorizontalLayout(navigationLayer, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(navigationLayer, Alignment.TOP_LEFT);
        topLayer.setComponentAlignment(logout, Alignment.TOP_RIGHT);

        ArrayList<Integer> monthsList = new ArrayList<>();
        for(int i= 1; i<=12; i++){
            monthsList.add(i);
        }
        BeanItemContainer<Integer> monthsContainer = new BeanItemContainer<>(Integer.class, monthsList);

        month = new ComboBox("Month: ");
        month.setWidth("100%");
        month.setRequired(true);
        month.setInvalidAllowed(false);
        month.setContainerDataSource(monthsContainer);

        year = new ComboBox("Year: ");
        year.setWidth("100%");
        year.setRequired(true);
        year.setInvalidAllowed(false);

        preview = new Button("Preview");
        preview.setWidth("100%");
        preview.addClickListener(e ->{
            showCost((int)month.getValue(),(int)year.getValue());
        });

        HorizontalLayout billingLayer = new HorizontalLayout(month, year, preview);
        billingLayer.setSpacing(true);
        billingLayer.setWidth("60%");
        billingLayer.setMargin(true);
        billingLayer.setSpacing(true);

        employeeCostGrid = new Grid();
        employeeCostGrid.setVisible(false);
        matCostGrid = new Grid();
        matCostGrid.setVisible(false);
        totalCost = new Label();
        totalCost.setVisible(false);
        printPDF = new Button();
        printPDF.setVisible(false);

        viewLayout = new VerticalLayout(topLayer, billingLayer, employeeCostGrid, matCostGrid, totalCost, printPDF);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(billingLayer, Alignment.MIDDLE_CENTER);
    }

    private void showCost(int month, int year) {
        viewLayout.removeComponent(employeeCostGrid);
        viewLayout.removeComponent(matCostGrid);
        viewLayout.removeComponent(totalCost);
        viewLayout.removeComponent(printPDF);

    	createEmployeeGrid(month,year);
    	createMatGrid(month,year);
    	double total= ProjectController.getSumBillingEmployeeItems(billingList)+ProjectController.getSumCosts(costList);
    	totalCost = new Label("TotalCost: "+total+"[CHF]");

        printPDF = new Button("Print PDF");
        printPDF.setWidth("80%");
        printPDF.addClickListener(e -> {
            PDFCreater.createPdf(billingList, costList, total, month, year, project);
            PDFCreater.openPDF();
            ProjectController.inactiveMonthlyActivitiesFromProject(activitiesFromMonth);
        });

        //TODO adapt heigh of the grids
        viewLayout.addComponent(employeeCostGrid);
        viewLayout.addComponent(matCostGrid);
        viewLayout.addComponent(totalCost);
        viewLayout.addComponent(printPDF);

    	setCompositionRoot(viewLayout);
	}

	private void createEmployeeGrid(int month, int year) {
        activitiesFromMonth = ProjectController.getMonthlyActivitiesFromProject(project.getId(), month, year);
        billingList = ProjectController.getBillingEmployeeItems(activitiesFromMonth);
        BeanItemContainer<BillingEmployeeItem> ds = new BeanItemContainer<>(BillingEmployeeItem.class, billingList);
        ds.addNestedContainerBean("pc");
        ds.addNestedContainerBean("pc.employee");
        gpc = new GeneratedPropertyContainer(ds);
        gpc.removeContainerProperty("pc.project");
        gpc.removeContainerProperty("pc.active");
        gpc.removeContainerProperty("pc.id");
        gpc.removeContainerProperty("pc.employee.id");
        gpc.removeContainerProperty("pc.employee.active");
        gpc.removeContainerProperty("pc.employee.changePassword");
        gpc.removeContainerProperty("pc.employee.admin");
        gpc.removeContainerProperty("pc.employee.password");
        gpc.removeContainerProperty("pc.employee.street");
        gpc.removeContainerProperty("pc.employee.plz");
        gpc.removeContainerProperty("pc.employee.city");

        employeeCostGrid = new Grid("Activity", gpc);
        employeeCostGrid.setWidth("100%");

    	FooterRow footer = employeeCostGrid.appendFooterRow();
    	footer.getCell("cost").setText("Total:"+ProjectController.getSumBillingEmployeeItems(billingList));
	}
	
	private void createMatGrid(int month, int year) {
		costList = ProjectController.getMonthlyCosts(project.getId(), month, year);
        BeanItemContainer<Cost> ds = new BeanItemContainer<>(Cost.class, costList);
        gpc2 = new GeneratedPropertyContainer(ds);
        
        matCostGrid = new Grid("Material", gpc2);
        matCostGrid.setWidth("100%");
        matCostGrid.removeColumn("id");
        matCostGrid.removeColumn("project");
        matCostGrid.setColumnOrder("date","name","description","price");
    	FooterRow footer = matCostGrid.appendFooterRow();
    	footer.getCell("price").setText("Total:"+ProjectController.getSumCosts(costList));
	}

	@Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(!((Employee)getUI().getSession().getAttribute("user")).isAdmin()) {
            getUI().getNavigator().navigateTo(UserHomepageView.NAME);
            return;
        }
        getUI().getPage().setTitle("Billing");
        try{
            project = ProjectController.getProject(Long.parseLong(event.getParameters()));
        }
        catch(NumberFormatException e){
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
            Notification.show("URL is not valid");
            return;
        }

        if(project == null){
            getUI().getNavigator().navigateTo(ProjectOverView.NAME);
            Notification.show("URL is not valid");
            return;
        }

        Activity firstActivity = ProjectController.getOldestActiviteFromProject(project.getId());
        Activity lastActivity = ProjectController.getLastActiviteFromProject(project.getId());

        ArrayList<Integer> yearsList = new ArrayList<>();

        if(firstActivity != null && lastActivity != null){
            int yearBegin = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(firstActivity.getBeginDate())).getYear();
            int yearEnd = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(lastActivity.getEndDate())).getYear();
            for(int i= yearBegin; i<=yearEnd; i++){
                yearsList.add(i);
            }
        }
        else{
            Notification.show("Nothing to bill!");
        }

        BeanItemContainer<Integer> yearsContainer = new BeanItemContainer<>(Integer.class, yearsList);
        year.setContainerDataSource(yearsContainer);

        if(!project.isActive()){
            printPDF.setEnabled(false);
        }
        setCompositionRoot(viewLayout);
    }

}
