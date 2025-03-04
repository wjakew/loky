/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.loky.web_server.pages.anonymous_access;

import com.jakubwawak.loky.LokyApplication;
import com.jakubwawak.loky.web_server.windows.AdminPasswordWindow;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route("welcome")
@RouteAlias(value = "/")
@PageTitle("loky - <3")
/**
 * WelcomePage is the main home page of the application.
 */
public class WelcomePage extends VerticalLayout {

    HorizontalLayout header;

    Button admin_login_button;
    

    /**
     * Constructor for the WelcomePage class.
     */
    public WelcomePage() {
        addClassName("page");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        prepareHeader();
        prepareMainContent();
    }

    /**
     * Function for preparing the header of the page.
     */
    void prepareHeader(){

        admin_login_button = new Button("Admin Portal",VaadinIcon.KEY.create(),this::adminLoginButtonClick);
        admin_login_button.addClassName("button-primary");


        header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.CENTER);
        header.setWidthFull();
        header.setHeight("100px");
        header.setPadding(true);
        header.setSpacing(true);
        header.setMargin(true);

        FlexLayout header_left = new FlexLayout();
        header_left.setAlignItems(Alignment.START);
        header_left.setJustifyContentMode(JustifyContentMode.START);
        header_left.setWidth("80%");
        header_left.add(new H6(LokyApplication.loky_configuration.getString("loky_instance_name")+" ("+LokyApplication.build+")"));

        FlexLayout header_right = new FlexLayout();
        header_right.setAlignItems(Alignment.END);
        header_right.setJustifyContentMode(JustifyContentMode.END);
        header_right.add(admin_login_button);

        header.add(header_left);
        header.add(header_right);
    }

    /**
     * Function for preparing the main content of the page.
     */
    void prepareMainContent(){
        add(header);

        VerticalLayout main_content = new VerticalLayout();
        main_content.setAlignItems(Alignment.CENTER);
        main_content.setJustifyContentMode(JustifyContentMode.CENTER);
        main_content.setSizeFull();

        H1 title = new H1("loky <3");
        Icon icon = VaadinIcon.LOCK.create();
        icon.addClassName("welcome-page-icon");

        main_content.add(icon,title);

        add(main_content);
    }

    /**
     * Function for handling the admin login button click event.
     * @param event
     */
    private void adminLoginButtonClick(ClickEvent<Button> event){
        AdminPasswordWindow admin_password_window = new AdminPasswordWindow();
        add(admin_password_window);
        admin_password_window.open();
    }
}
