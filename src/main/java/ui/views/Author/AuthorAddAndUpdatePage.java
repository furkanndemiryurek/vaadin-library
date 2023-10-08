package ui.views.Author;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import core.entity.Author;
import core.service.AuthorService;

import java.util.List;


public class AuthorAddAndUpdatePage extends VerticalLayout{
    @PropertyId("id")
    TextField idTextField;
    @PropertyId("firstName")
    TextField firstNameTextField;
    @PropertyId("lastName")
    TextField lastNameTextField;
    @PropertyId("age")
    TextField ageTextField;
    @PropertyId("country")
    TextField countryTextField;

    Table table;
    IndexedContainer indexedContainer;
    Button authorOperationsButton;
    AuthorService authorService;
    Author authorBindObject;
    Window authorUpdateWindow;

    private BeanFieldGroup<Author> binder;

    public AuthorAddAndUpdatePage(Author author){
        this.authorBindObject = author;
        buildMainLayout();
        setWidth("350px");
        setHeight("300px");

        authorOperationsButton.setIcon(FontAwesome.PENCIL);
        authorOperationsButton.setCaption("Güncelle");
        authorOperationsButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        editUpdateForm(author);
        this.setMargin(true);
        Page.getCurrent().setTitle("Yazar Güncelle | "+author.getFirstName() + " " + author.getLastName() );
    }

    public AuthorAddAndUpdatePage(){
        authorBindObject = new Author();
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

        firstNameTextField = new TextField("Yazar adı");
        lastNameTextField = new TextField("Yazar soyadı");
        ageTextField = new TextField("Yazar yaşı");
        countryTextField = new TextField("Yazar ülkesi");
        addComponents(firstNameTextField, lastNameTextField, ageTextField, countryTextField);

        firstNameTextField.clear();
        lastNameTextField.clear();
        ageTextField.clear();
        countryTextField.clear();
        createAuthorOperationsButton();
        setMargin(true);
        setSpacing(true);
    }

    private void createAuthorOperationsButton(){
        authorOperationsButton = new Button("Ekle");
        addComponent(authorOperationsButton);

        authorOperationsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    runBinder();
                    authorService = new AuthorService();
                    if (authorBindObject.getId() != null){
                        authorService.update(authorBindObject);
                        Page.getCurrent().setTitle("Yazar Güncelle | "+authorBindObject.getFirstName());
                        Notification.show(" ✔️",authorBindObject.getFirstName() + "  adlı yazar güncellendi !", Notification.TYPE_HUMANIZED_MESSAGE);
                        UI.getCurrent().getUI().removeWindow(getAuthorUpdateWindow());
                    }
                    else{
                        if (authorBindObject.getFirstName() == null || authorBindObject.getFirstName().equals("")){
                            Notification.show("✖","Lütfen yazar adı giriniz.", Notification.TYPE_ERROR_MESSAGE);
                            firstNameTextField.clear();
                        }else if(authorBindObject.getLastName() == null || authorBindObject.getLastName().equals("")){
                            Notification.show("✖","Lütfen yazar soyadı giriniz.", Notification.TYPE_ERROR_MESSAGE);
                            lastNameTextField.clear();
                        }
                        else if(authorBindObject.getCountry() == null || authorBindObject.getCountry().equals("")){
                            Notification.show("✖","Lütfen geçerli bir ülke giriniz.", Notification.TYPE_ERROR_MESSAGE);
                            countryTextField.clear();
                        }else{
                            Notification.show(" ✔️", authorBindObject.getFirstName() + " adlı yazar eklendi  ! ", Notification.TYPE_HUMANIZED_MESSAGE);
                            authorService.add(authorBindObject);
                            fillTable();
                            firstNameTextField.clear();
                            lastNameTextField.clear();
                            ageTextField.clear();
                            countryTextField.clear();
                        }
                    }
                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
    }

    private void buildTableIndexedContainer() {
        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id", Long.class, null);
        indexedContainer.addContainerProperty("firstName", String.class, null);
        indexedContainer.addContainerProperty("lastName", String.class, null);
        indexedContainer.addContainerProperty("age", Integer.class, null);
        indexedContainer.addContainerProperty("country", String.class, null);
        indexedContainer.addContainerProperty("delete", Button.class, null);
        indexedContainer.addContainerProperty("update", Button.class, null);
    }

    private void buildTable(){
        table = new Table();
        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("Id", "Yazar adı", "Yazar soyadı", "Yazar yaşı" ,"Yazar ülkesi", "Sil", "Güncelle");
    }

    private void fillTable(){
        indexedContainer.removeAllItems();
        authorService = new AuthorService();
        List<Author> authors = authorService.findAll();
        for (Author author : authors) {
            Item item = indexedContainer.addItem(author);

            Button deleteButton = new Button();
            deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
            deleteButton.setIcon(FontAwesome.TRASH);
            deleteAuthor(author, deleteButton);

            Button updateButton = new Button();
            updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
            updateButton.setIcon(FontAwesome.PENCIL);
            updateAuthor(author, updateButton);

            fillColumns(author, item, deleteButton, updateButton);
        }
    }

    private void fillColumns(Author author, Item item, Button deleteButton, Button updateButton){
        item.getItemProperty("id").setValue(author.getId());
        item.getItemProperty("firstName").setValue(author.getFirstName());
        item.getItemProperty("lastName").setValue(author.getLastName());
        item.getItemProperty("age").setValue(author.getAge());
        item.getItemProperty("country").setValue(author.getCountry());
        item.getItemProperty("delete").setValue(deleteButton);
        item.getItemProperty("update").setValue(updateButton);
    }



    private void editUpdateForm(Author author){
        idTextField.setVisible(true);
        idTextField.setEnabled(false);
        this.setMargin(true);
    }

    //DELETE

    private void deleteAuthor(Author author, Button deleteButton){
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window deleteWindow = new Window("Yazar silme");
                deleteWindow.center();
                deleteWindow.setWidth("25%");
                deleteWindow.setHeight("220px");


                AuthorDeletePage deleteAuthor = new AuthorDeletePage(author);
                deleteAuthor.setMyDeleteWindow(deleteWindow);
                deleteWindow.setContent(deleteAuthor);
                deleteAuthor.setSpacing(true);
                deleteAuthor.setMargin(true);


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

    private void updateAuthor(Author author, Button updateButton){
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window updateWindow = new Window("Güncelleme");

                AuthorAddAndUpdatePage selectedAuthorUpdateWindow = new AuthorAddAndUpdatePage(author);
                selectedAuthorUpdateWindow.setAuthorUpdateWindow(updateWindow);
                selectedAuthorUpdateWindow.setMargin(true);
                selectedAuthorUpdateWindow.setSpacing(true);

                updateWindow.setContent(selectedAuthorUpdateWindow);
                selectedAuthorUpdateWindow.setMargin(true);
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


    //BINDER


    private void buildBinder(){
        binder = new BeanFieldGroup<Author>(Author.class);
        binder.setItemDataSource(authorBindObject);
        binder.bindMemberFields(this);
    }

    private void runBinder(){
        try {
            binder.commit();
        }catch (FieldGroup.CommitException e){
            Notification.show("Lütfen form alanlarını eksiksiz doldurun!");
        }
    }

    public Window getAuthorUpdateWindow() {
        return authorUpdateWindow;
    }

    public void setAuthorUpdateWindow(Window authorUpdateWindow) {
        this.authorUpdateWindow = authorUpdateWindow;
    }
}
