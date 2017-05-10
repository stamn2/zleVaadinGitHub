package com.project1.view.user;

import com.project1.view.ActivityRecordView;
import com.project1.view.LoginView;
import com.project1.view.RecordHistoryView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class UserHomepageView extends CustomComponent implements View {
    public static final String NAME = "userHomepage";

    private final Button start, stop, manualEntry, history, logout;

    public UserHomepageView(){
        start = new Button("Start");
        start.setWidth("100%");
        stop = new Button("Stop");
        stop.setWidth("100%");
        stop.setVisible(false);
        start.addClickListener( e -> {
            start.setVisible(false);
            stop.setVisible(true);
        });

        manualEntry = new Button("Manual Entry");
        manualEntry.setWidth("100%");
        manualEntry.addClickListener( e ->{
            getUI().getNavigator().navigateTo(ActivityRecordView.NAME);
        });
        history = new Button("History");
        history.setWidth("100%");
        history.addClickListener( e ->{
            getUI().getNavigator().navigateTo(RecordHistoryView.NAME);
        });

        logout = new Button("Logout");
        logout.setWidth("15%");
        logout.addClickListener(e ->{
            getUI().getSession().setAttribute("user", null);
            getUI().getNavigator().navigateTo(LoginView.NAME);
        });

        VerticalLayout menu = new VerticalLayout(start, stop, manualEntry, history);
        menu.setSpacing(true);
        menu.setWidth("60%");
        menu.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout viewLayout = new VerticalLayout(logout, menu);
        viewLayout.setMargin(true);
        viewLayout.setSpacing(true);
        viewLayout.setComponentAlignment(menu, Alignment.MIDDLE_CENTER);
        viewLayout.setComponentAlignment(logout, Alignment.TOP_RIGHT);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        getUI().getPage().setTitle("Homepage");
    }
}
