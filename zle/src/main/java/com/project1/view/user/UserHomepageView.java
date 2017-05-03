package com.project1.view.user;

import com.project1.view.ActivityRecordView;
import com.project1.view.LoginView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class UserHomepageView extends CustomComponent implements View {
    public static final String NAME = "userHomepage";

    private final Button start, stop, manualEntry, history, logout;

    public UserHomepageView(){
        start = new Button("Start");
        start.setWidth("70%");
        stop = new Button("Stop");
        stop.setWidth("70%");
        stop.setVisible(false);
        start.addClickListener( e -> {
            start.setVisible(false);
            stop.setVisible(true);
        });

        manualEntry = new Button("Manual Entry");
        manualEntry.setWidth("70%");
        manualEntry.addClickListener( e ->{
            getUI().getNavigator().navigateTo(ActivityRecordView.NAME);
        });
        history = new Button("History");
        history.setWidth("70%");

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        VerticalLayout viewLayout = new VerticalLayout(logout, start, stop, manualEntry, history);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification.show("enter the homepage view");
    }
}
