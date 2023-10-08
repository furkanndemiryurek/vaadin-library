package ui.views.Author;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import core.entity.Author;
import core.entity.Publisher;
import core.service.PublisherService;

public class AuthorDeletePage extends VerticalLayout {
    Button yes;
    Button no;
    Window myDeleteWindow;
    Author author;
    public AuthorDeletePage(Author author){
        this.author = author;
        buildMainLayout();
    }

    private void buildMainLayout() {

        setWidth("98%");
        setHeight("95%");

        VerticalLayout message = new VerticalLayout();

        Label lbl = new Label("");
        lbl.setValue("<p><strong>" + author.getFirstName() + "</strong>" + " adlı yazarı silmek istediğinize emin misiniz? </p> </br></br></br></br>");
        lbl.setContentMode(ContentMode.HTML);
        lbl.setHeight("30%");
        message.addComponent(lbl);
        addComponent(message);

        HorizontalLayout buttons = new HorizontalLayout();
        yes = new Button("Sil");
        yes.setIcon(FontAwesome.TRASH);
        yes.addStyleName(ValoTheme.BUTTON_DANGER);
        yes.setWidth("130px");
        buttons.addComponent(yes);

        no = new Button("Vazgeç");
        no.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        no.setWidth("130px");
        buttons.addComponent(no);

        buttons.setSpacing(true);

        addComponent(buttons);
        message.setSpacing(true);
        this.setMargin(true);
        this.setSpacing(true);

        deleteAuthor(this.author);
    }

    private void deleteAuthor(Author author){
        yes.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    PublisherService publisherService = new PublisherService();
                    publisherService.deleteById(author.getId());
                    Notification.show("✔️", author.getFirstName() + " adlı yazar silindi.", Notification.TYPE_HUMANIZED_MESSAGE);
                    UI.getCurrent().getUI().removeWindow(getMyDeleteWindow());
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }

                no.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        UI.getCurrent().getUI().removeWindow(getMyDeleteWindow());
                    }
                });
            }
        });
    }

    public Window getMyDeleteWindow() {
        return myDeleteWindow;
    }

    public void setMyDeleteWindow(Window myDeleteWindow) {
        this.myDeleteWindow = myDeleteWindow;
    }
}
