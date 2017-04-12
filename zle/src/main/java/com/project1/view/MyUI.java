package com.project1.view;

import javax.servlet.annotation.WebServlet;

import com.project1.view.admin.EmployeeEditorView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;


/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4628885312461454476L;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
    	getPage().setTitle("MainUI");
    	// i create the navigator, add a view and change/navigate to it
    	new Navigator(this, this);
    	getNavigator().addView("", LoginView.class);
		getNavigator().addView(EmployeeEditorView.NAME, EmployeeEditorView.class);
        getNavigator().addView(FirstLoginView.NAME, FirstLoginView.class);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
