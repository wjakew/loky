/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all right reserved
 */
package com.jakubwawak.loky.web_server;

import com.jakubwawak.loky.LokyApplication;
import com.jakubwawak.loky.web_server.components.AppMenu;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 * AdminPage is the page for the admin panel.
 */
@PageTitle("Template")
@Route(value = "template",layout = AppMenu.class)
public class TemplatePageLogged extends VerticalLayout {

    Button returnButton;

    /**
     * Constructor for AdminPage
     */
    public TemplatePageLogged(){
        addClassName("page");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        verifyAdminCookie();
    }

    /**
     * Prepare the admin page
     */
    private void verifyAdminCookie(){
        prepareComponents();
        Object adminCookieObject = VaadinSession.getCurrent().getAttribute("admin_cookie");
        if(adminCookieObject == null){
            LokyApplication.database.log("ADMIN_ACCESS_ATTEMPT", "Admin portal access attempt (no cookie)");
            prepareBlockedPage();
        }
        else{
            String adminCookie = adminCookieObject.toString();
            LokyApplication.database.log("ADMIN_ACCESS_ATTEMPT", "Admin portal access attempt (" + adminCookie + ")");
            if ( LokyApplication.database.verifyAdminCookie(adminCookie) == 1 ){
                prepareAdminPage();
            }
            else{
                prepareBlockedPage();
            }
        }
    }

    /**
     * Prepare the components
     */
    void prepareComponents(){
        returnButton = new Button("Return to welcome page", VaadinIcon.RECYCLE.create());
        returnButton.addClassName("button-primary-black");
        returnButton.addClickListener(e -> UI.getCurrent().navigate("/welcome"));
    }

    /**
     * Prepare the blocked page
     */
    void prepareBlockedPage(){
        H1 title = new H1("loky <3");
        Icon icon = VaadinIcon.SIGN_OUT.create();
        icon.addClassName("welcome-page-icon");
        add(icon,title,new H6("You are not authorized to access this page"));
        add(returnButton);
    }

    /**
     * Prepare the blank page
     */
    void prepareBlankPage(){
        H1 title = new H1("loky <3");
        Icon icon = VaadinIcon.SIGN_OUT.create();
        icon.addClassName("welcome-page-icon");
        add(icon,title,new H6("No cookie provided."));
        add(returnButton);
    }

    /**
     * Prepare the admin page
     */
    void prepareAdminPage(){

    }

    
}
