package ui.views.Publisher;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import core.entity.Publisher;
import core.service.PublisherService;

import java.util.List;


public class PublisherAddAndUpdatePage extends VerticalLayout{
    @PropertyId("id")
    TextField idTextField;
    @PropertyId("name")
    TextField nameTextField;
    @PropertyId("country")
    TextField countryTextField;
    Table table;
    IndexedContainer indexedContainer;
    Button publisherOperationsButton;
    PublisherService publisherService;
    Publisher publisherBindObject;
    Window publisherUpdateWindow;

    private BeanFieldGroup<Publisher> binder;

    public PublisherAddAndUpdatePage(Publisher publisher){
        publisherBindObject = publisher;
        buildMainLayout();
        setWidth("350");
        setHeight("300px");

        publisherOperationsButton.setIcon(FontAwesome.PENCIL);
        publisherOperationsButton.setCaption("Güncelle");
        publisherOperationsButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        editUpdateForm(publisher);
        this.setMargin(true);
        Page.getCurrent().setTitle("Kategori Güncelle | "+publisher.getName() );
    }

    public PublisherAddAndUpdatePage(){
        publisherBindObject = new Publisher();
        buildMainLayout();
        buildTableIndexedContainer();
        buildTable();
        fillTable();
        addComponent(table);
    }

    private void buildMainLayout(){
        buildForm();
        buildBinder();
    }

    private void buildForm(){
        idTextField = new TextField("Id");
        idTextField.setValue("0");
        addComponent(idTextField);
        idTextField.setVisible(false);

        nameTextField = new TextField("Yayıncı adı");
        countryTextField = new TextField("Yayıncı ülkesi");
        addComponents(nameTextField, countryTextField);

        nameTextField.clear();
        countryTextField.clear();
        createPublisherOperationsButton();
        setMargin(true);
        setSpacing(true);
    }

    private void createPublisherOperationsButton(){
        publisherOperationsButton = new Button("Ekle");
        addComponent(publisherOperationsButton);

        publisherOperationsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    runBinder();
                    publisherService = new PublisherService();
                    if (publisherBindObject.getId() != null){
                        Notification.show(" ✔️",publisherBindObject.getName() + "  adlı kategori  güncellendi !", Notification.TYPE_HUMANIZED_MESSAGE);
                        publisherService.update(publisherBindObject);
                        Page.getCurrent().setTitle("Kategori Güncelle | "+publisherBindObject.getName() + " güncellendi");
                        UI.getCurrent().getUI().removeWindow(getPublisherUpdateWindow());
                    }
                    else{
                        if (publisherBindObject.getName() == null || publisherBindObject.getName().equals("")){
                            Notification.show("✖","Lütfen geçerli bir yayıncı ad giriniz.", Notification.TYPE_ERROR_MESSAGE);
                            nameTextField.clear();
                        }else if(publisherBindObject.getCountry() == null || publisherBindObject.getCountry().equals("")){
                            Notification.show("✖","Lütfen geçerli bir ülke giriniz.", Notification.TYPE_ERROR_MESSAGE);
                            countryTextField.clear();
                        }else{
                            Notification.show(" ✔️", publisherBindObject.getName() + " adlı yayıncı eklendi  ! ", Notification.TYPE_HUMANIZED_MESSAGE);
                            publisherService.add(publisherBindObject);
                            fillTable();
                            nameTextField.clear();
                            countryTextField.clear();
                        }
                    }
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
    }

    private void buildTable(){
        table = new Table();
        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("Id", "Yayıncı adı", "Yayıncı ülkesi", "Sil", "Güncelle");
    }

    private void fillTable(){
        indexedContainer.removeAllItems();
        publisherService = new PublisherService();
        List<Publisher> publishers = publisherService.findAll();
        for (Publisher publisher : publishers) {
            Item item = indexedContainer.addItem(publisher);

            Button deleteButton = new Button();
            deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
            deleteButton.setIcon(FontAwesome.TRASH);
            deletePublisher(publisher, deleteButton);

            Button updateButton = new Button();
            updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
            updateButton.setIcon(FontAwesome.PENCIL);
            updatePublisher(publisher, updateButton);

            fillColumns(publisher, item, deleteButton, updateButton);
        }
    }

    private void fillColumns(Publisher publisher, Item item, Button deleteButton, Button updateButton){
        item.getItemProperty("id").setValue(publisher.getId());
        item.getItemProperty("name").setValue(publisher.getName());
        item.getItemProperty("country").setValue(publisher.getCountry());
        item.getItemProperty("delete").setValue(deleteButton);
        item.getItemProperty("update").setValue(updateButton);
    }

    private void buildTableIndexedContainer() {
        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id", Long.class, null);
        indexedContainer.addContainerProperty("name", String.class, null);
        indexedContainer.addContainerProperty("country", String.class, null);
        indexedContainer.addContainerProperty("delete", Button.class, null);
        indexedContainer.addContainerProperty("update", Button.class, null);
    }

    private void editUpdateForm(Publisher publisher){
        idTextField.setVisible(true);
        idTextField.setEnabled(false);
        this.setMargin(true);
    }

    //DELETE

    private void deletePublisher(Publisher publisher, Button deleteButton){
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window deleteWindow = new Window("Yayıncı silme");
                deleteWindow.center();
                deleteWindow.setWidth("25%");
                deleteWindow.setHeight("220px");


                PublisherDeletePage deletePublisher = new PublisherDeletePage(publisher);
                deletePublisher.setMyDeleteWindow(deleteWindow);
                deleteWindow.setContent(deletePublisher);
                deletePublisher.setSpacing(true);
                deletePublisher.setMargin(true);


                deleteWindow.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent closeEvent) {
                        fillTable();
                    }
                });
                UI.getCurrent().addWindow(deleteWindow);
            }
        });
    }

    //UPDATE

    private void updatePublisher(Publisher publisher, Button updateButton){
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window updateWindow = new Window("Güncelleme");

                PublisherAddAndUpdatePage selectedPublisherUpdateWindow = new PublisherAddAndUpdatePage(publisher);
                selectedPublisherUpdateWindow.setPublisherUpdateWindow(updateWindow);
                selectedPublisherUpdateWindow.setMargin(true);

                updateWindow.setContent(selectedPublisherUpdateWindow);
                selectedPublisherUpdateWindow.setMargin(true);
                updateWindow.center();
                updateWindow.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent closeEvent) {
                        fillTable();
                    }
                });
                UI.getCurrent().addWindow(updateWindow);
            }
        });
    }

    private void buildBinder(){
        binder = new BeanFieldGroup<Publisher>(Publisher.class);
        binder.setItemDataSource(publisherBindObject);
        binder.bindMemberFields(this);
    }

    private void runBinder(){
        try {
            binder.commit();
        }catch (FieldGroup.CommitException e){
            Notification.show("Lütfen form alanlarını eksiksiz doldurun!");
        }
    }


    public Window getPublisherUpdateWindow() {
        return publisherUpdateWindow;
    }

    public void setPublisherUpdateWindow(Window publisherUpdateWindow) {
        this.publisherUpdateWindow = publisherUpdateWindow;
    }
}
