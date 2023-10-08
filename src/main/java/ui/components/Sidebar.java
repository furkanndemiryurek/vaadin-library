package ui.components;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class Sidebar extends VerticalLayout {
    public Sidebar(){
        Label title = new Label("Menu");
        title.addStyleName(ValoTheme.MENU_TITLE);

        Button view1 = new Button("View 1");
        Button view2 = new Button("View 2");

        VerticalLayout menu = new VerticalLayout(title, view1, view2);
        menu.addStyleName(ValoTheme.MENU_ROOT);

        menu.setSizeFull();
        addComponent(menu);
    }
}
