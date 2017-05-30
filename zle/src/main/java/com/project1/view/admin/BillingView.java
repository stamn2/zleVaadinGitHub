package com.project1.view.admin;

import com.project1.controller.ProjectController;
import com.project1.controller.RecordController;
import com.project1.controller.UserController;
import com.project1.domain.Activity;
import com.project1.domain.Client;
import com.project1.domain.Cost;
import com.project1.domain.Employee;
import com.project1.domain.Project;
import com.project1.view.LoginView;
import com.project1.view.user.UserHomepageView;
import com.project1.zle.BillingEmployeeItem;
import com.project1.zle.PDFCreater;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.FooterRow;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillingView extends CustomComponent implements View {

    public static final String NAME = "BillingView";

    private final Button logout, back, preview;
    private ComboBox month, year;
    private VerticalLayout viewLayout;
    private Project project;
    private Grid employeeCostGrid;
	private Grid matCostGrid;
    private GeneratedPropertyContainer gpc, gpc2;
    private Button printPDF;
    private List<BillingEmployeeItem> billingList;
    private List<Cost> costList;
    private Label totalCost;

    public BillingView(){
        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });


        back = new Button("Back");
        back.setWidth("15%");
        back.addClickListener(e ->{
            getUI().getNavigator().navigateTo(ProjectDetailView.NAME + "/" + project.getId());
        });

        HorizontalLayout topLayer = new HorizontalLayout(back, logout);
        topLayer.setSpacing(true);
        topLayer.setWidth("100%");
        topLayer.setComponentAlignment(back, Alignment.TOP_LEFT);
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
        

        viewLayout = new VerticalLayout(topLayer, billingLayer);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(billingLayer, Alignment.MIDDLE_CENTER);
    }

    private void showCost(int month, int year) {
    	createEmployeeGrid(month,year);
    	createMatGrid(month,year);
    	double total= ProjectController.getSumBillingEmployeeItems(billingList)+ProjectController.getSumCosts(costList);
    	totalCost = new Label("TotalCost: "+total+"[CHF]");
        printPDF = new Button("Print PDF");
        printPDF.setWidth("80%");
        printPDF.addClickListener(e -> {
            PDFCreater.createPdf(billingList, costList, total, month, year, project);
            PDFCreater.openPDF();
        });
    	viewLayout.addComponent(employeeCostGrid);
    	viewLayout.addComponent(matCostGrid);
    	viewLayout.addComponent(totalCost);
    	viewLayout.addComponent(printPDF);
    	setCompositionRoot(viewLayout);
	}

	private void createEmployeeGrid(int month, int year) {
        billingList = ProjectController.getBillingEmployeeItems(project.getId(), month, year);
        BeanItemContainer<BillingEmployeeItem> ds = new BeanItemContainer<>(BillingEmployeeItem.class, billingList);
        ds.addNestedContainerBean("pc");
        ds.addNestedContainerBean("pc.employee");
        gpc = new GeneratedPropertyContainer(ds);
        
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

        //TODO activity null
        Activity firstActivity = ProjectController.getOldestActiveActiviteFromProject(project.getId());
        Activity lastActivity = ProjectController.getLastActiveActiviteFromProject(project.getId());

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
            //TODO
        }
        setCompositionRoot(viewLayout);
    }

}
