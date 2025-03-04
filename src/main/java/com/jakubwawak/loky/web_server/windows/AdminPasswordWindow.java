package com.jakubwawak.loky.web_server.windows;

/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Template for creating new windows
 */
public class AdminPasswordWindow extends Dialog{

    String width = "50%";
    String height = "30%";

    PasswordField password_field;
    Button unlock_button;

    VerticalLayout content;

    /**
     * Constructor for TemplateWindow
     */
    public AdminPasswordWindow() {
        super();
        addClassName("window");
        setHeaderTitle("Admin Portal Access");
        setWidth(width);
        setHeight(height);

        content = new VerticalLayout();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.setMargin(true);

        createContent();

        add(content);
    }

    /**
     * Creates the content of the window
     */
    public void createContent() {
        password_field = new PasswordField("Admin Password");
        password_field.setPlaceholder("secret password");
        password_field.setWidthFull();

        unlock_button = new Button("Unlock",VaadinIcon.KEY.create());
        unlock_button.setWidthFull();
        unlock_button.addClassName("button-primary-black");

        content.add(VaadinIcon.LOCK.create());
        content.add(new H6("Admin Portal is Locked"));
        content.add(password_field);
        content.add(unlock_button);

    }
}
