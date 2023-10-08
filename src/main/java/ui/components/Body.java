package ui.components;

import com.vaadin.ui.*;
import ui.views.Member.MemberAddAndUpdatePage;


public class Body extends VerticalLayout {

    public Body() {
        MemberAddAndUpdatePage memberAddAndUpdatePage = new MemberAddAndUpdatePage();
        addComponent(memberAddAndUpdatePage);
    }

}
