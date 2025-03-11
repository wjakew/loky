/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all right reserved
 */
package com.jakubwawak.loky.web_server.pages.locked_access;

import com.jakubwawak.loky.LokyApplication;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 * AdminPage is the page for the admin panel.
 */
@Route("admin")
@PageTitle("Admin Panel")
public class AdminPage extends VerticalLayout {

    /**
     * Constructor for AdminPage
     */
    public AdminPage(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        verifyAdminCookie();
    }

    /**
     * Prepare the admin page
     */
    private void verifyAdminCookie(){
        String adminCookie = VaadinSession.getCurrent().getAttribute("admin_cookie").toString();
        LokyApplication.database.log("ADMIN_ACCESS_ATTEMPT", "Admin portal access attempt (" + adminCookie + ")");
        if(adminCookie == null){
            UI.getCurrent().navigate("/welcome");
            LokyApplication.showNotification("You are not authorized to access this page");
        }
        else{
            if ( LokyApplication.database.verifyAdminCookie(adminCookie) == 1 ){
                prepareAdminPage();
            }
            else{
                UI.getCurrent().navigate("/welcome");
                LokyApplication.showNotification("You are not authorized to access this page");
            }
        }
    }

    /**
     * Prepare the admin page
     */
    void prepareAdminPage(){

    }

    
}
