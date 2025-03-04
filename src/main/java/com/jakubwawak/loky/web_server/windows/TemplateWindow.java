/**
 * @author Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.loky.web_server.windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Template for creating new windows
 */
public class TemplateWindow extends Dialog{

    String width = "50%";
    String height = "50%";

    VerticalLayout content;

    /**
     * Constructor for TemplateWindow
     */
    public TemplateWindow(String windowName) {
        super();
        addClassName("window");
        setHeaderTitle(windowName);
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
    }

    /**
     * Creates the content of the window
     */
    public void createContent() {
        
    }
}
