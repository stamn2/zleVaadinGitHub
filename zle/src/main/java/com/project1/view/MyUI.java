package com.project1.view;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.project1.controller.UserController;
import com.project1.domain.Employee;
import com.project1.view.admin.AdminHomepageView;
import com.project1.view.admin.EmployeeEditorView;
import com.project1.view.admin.EmployeeOverView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
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
        getNavigator().addView(AdminHomepageView.NAME, AdminHomepageView.class);
        getNavigator().addView(EmployeeOverView.NAME, EmployeeOverView.class);
        
        
		getNavigator().addViewChangeListener(new ViewChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3549061311437753168L;

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				boolean isLoggedIn = getSession().getAttribute("user") != null;
				boolean isLoginView = event.getNewView() instanceof LoginView;
				if (!isLoggedIn && !isLoginView) {
					getNavigator().navigateTo(LoginView.NAME);
					return false;
				} /*else if (isLoggedIn && isLoginView) {
					getNavigator().navigateTo(???View???.NAME);
					return false;
				}*/
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {

			}
		});
        
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(900); // auto logout after 15min
    }
	

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        private static final String PERSISTENCE_UNIT_NAME = "zleDB";
        private static EntityManagerFactory factory; 
        private EntityManager em;
        Employee emp;
    	
        @Override
        protected void servletInitialized() throws ServletException {
        	super.servletInitialized();
        	System.out.println("init done...");
    		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    	    em = factory.createEntityManager();
    	    
            emp = new Employee("email@mail.com", "firstname", "lastname", "street", "plz", "city", "tel");
            emp.setIsAdmin(true);
            UserController.addEmployee("email@mail.com", "firstname", "lastname", "street", "plz", "city", "tel");
            
            em.getTransaction().begin();
            em.persist(emp);
            em.getTransaction().commit();
        }
    	
    }
}
