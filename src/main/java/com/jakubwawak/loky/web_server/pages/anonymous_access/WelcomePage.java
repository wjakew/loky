/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.loky.web_server.pages.anonymous_access;

import com.vaadin.flow.component.button.Button;
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
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }

    /**
     * Function for preparing the header of the page.
     */
    void prepareHeader(){
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
        header_left.setJustifyContentMode(JustifyContentMode.CENTER);
        header_left.setWidth("80%");
        header_left.setHeight("100%");

        FlexLayout header_right = new FlexLayout();
        header_right.setAlignItems(Alignment.END);
        header_right.setJustifyContentMode(JustifyContentMode.CENTER);
        header_right.setHeight("100%");

        header.add(header_left);
        header.add(header_right);
    }
}
