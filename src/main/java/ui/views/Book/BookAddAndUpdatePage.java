package ui.views.Book;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import core.entity.Book;
import core.entity.Genre;
import core.service.BookService;
import core.service.GenreService;

import java.util.List;

public class BookAddAndUpdatePage extends VerticalLayout {
    Window bookUpdateWindow;

    BookService bookService;
    Book bookBindObject;

    @PropertyId("id")
    TextField idTextField;
    @PropertyId("name")
    TextField nameTextField;
    @PropertyId("publicationYear")
    TextField publicationYearTextField;
    @PropertyId("stockQuantity")
    TextField stockQuantityTextField;
    @PropertyId("genre.name")
    ComboBox genreCombo;
    @PropertyId("author.firstName")
    TextField authorFirstNameTextField;
    @PropertyId("author.lastName")
    TextField authorLastNameTextField;
    @PropertyId("publisher.name")
    TextField publisherNameTextField;

    GenreService genreService;


    Button bookOperationsButton;

    Table table;
    IndexedContainer indexedContainer;

    private BeanFieldGroup<Book> binder;

    public BookAddAndUpdatePage(Book book){
        bookBindObject = book;
        buildMainLayout();
        setWidth("350px");
        setHeight("300px");

        bookOperationsButton.setIcon(FontAwesome.PENCIL);
        bookOperationsButton.setCaption("Güncelle");
        bookOperationsButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        //editUpdateForm(book);
        setMargin(true);
        Page.getCurrent().setTitle("Kitap Güncelle");
    }

    public BookAddAndUpdatePage(){
        bookBindObject = new Book();
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
        idTextField.setVisible(false);

        nameTextField = new TextField("Kitap adı");
        authorFirstNameTextField = new TextField("Yazar adı");
        authorLastNameTextField = new TextField("Yazar soyadı");
        publisherNameTextField = new TextField("Yayıncı");
        publicationYearTextField = new TextField("Basım yılı");
        stockQuantityTextField = new TextField("Stok adedi");


        genreCombo = new ComboBox("Tür");
        genreService = new GenreService();
        List<Genre> genres = genreService.findAll();
        genres.forEach(genre -> genreCombo.addItem(genre.getName()));


        addComponents(idTextField, nameTextField, genreCombo, authorFirstNameTextField,
                authorLastNameTextField, publisherNameTextField, publicationYearTextField, stockQuantityTextField);
        createGenreOperationsButton();
        setMargin(true);
        setSpacing(true);
    }

    private void createGenreOperationsButton(){
        bookOperationsButton = new Button("Ekle");
        addComponent(bookOperationsButton);

        bookOperationsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    runBinder();
                    bookService = new BookService();
                    if (bookBindObject.getId() != null){
                        bookService.update(bookBindObject);
                        Page.getCurrent().setTitle("Tür güncelle");
                        Notification.show(" ✔️",bookBindObject.getName() + "  adlı tür güncellendi !", Notification.TYPE_HUMANIZED_MESSAGE);
                        //UI.getCurrent().getUI().removeWindow(getGenreUpdateWindow());
                    }else {
                        if (bookBindObject.getName() == null || bookBindObject.getName().equals("")){
                            Notification.show("✖","Lütfen kitap adı giriniz.", Notification.TYPE_ERROR_MESSAGE);
                        }else{
                            Notification.show(" ✔️", bookBindObject.getName() + " adlı kitap eklendi  ! ", Notification.TYPE_HUMANIZED_MESSAGE);
                            bookService.add(bookBindObject);
                            fillTable();

                        }
                        nameTextField.clear();
                    }
                }catch (Exception e){
                    Notification.show(e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
    }

    private void buildTableIndexedContainer(){
        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id", Long.class, null);
        indexedContainer.addContainerProperty("name", String.class, null);
        indexedContainer.addContainerProperty("genre.name", String.class, null);
        indexedContainer.addContainerProperty("author.firstName", String.class, null);
        indexedContainer.addContainerProperty("author.lastName", String.class, null);
        indexedContainer.addContainerProperty("publisher.name", String.class, null);
        indexedContainer.addContainerProperty("publicationYear", Integer.class, null);
        indexedContainer.addContainerProperty("stockQuantity", Integer.class, null);
    }

    private void buildTable(){
        table = new Table();
        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("ID", "Ad", "Tür", "Yazar Adı", "Yazar Soyadı","Yayın","Yayın Yılı", "Stok Adedi");
        table.setSizeUndefined();
        table.setImmediate(true);
    }

    private void fillTable(){
        indexedContainer.removeAllItems();
        bookService = new BookService();
        List<Book> books = bookService.findAll();
        for (Book book : books) {
            Item item = indexedContainer.addItem(book);

            /*
            Button updateButton = new Button();
            updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
            updateButton.setIcon(FontAwesome.PENCIL);
            updateGenre(genre, updateButton);

            Button deleteButton = new Button();
            deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
            deleteButton.setIcon(FontAwesome.TRASH);
            deleteGenre(genre, deleteButton);*/

            fillColumns(book, item);
        }
    }

    private void fillColumns(Book book, Item item){
        item.getItemProperty("id").setValue(book.getId());
        item.getItemProperty("name").setValue(book.getName());
        item.getItemProperty("genre.name").setValue(book.getGenre().getName());
        item.getItemProperty("author.firstName").setValue(book.getAuthor().getFirstName());
        item.getItemProperty("author.lastName").setValue(book.getAuthor().getLastName());
        item.getItemProperty("publisher.name").setValue(book.getPublisher().getName());
        item.getItemProperty("publicationYear").setValue(book.getPublicationYear());
        item.getItemProperty("stockQuantity").setValue(book.getStockQuantity());

    }

    /*
    private void deleteGenre(Genre genre, Button deleteButton){
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window deleteWindow = new Window("Tür silme");
                deleteWindow.center();
                deleteWindow.setWidth("25%");
                deleteWindow.setHeight("220px");

                GenreDeletePage deleteGenre = new GenreDeletePage(genre);
                deleteGenre.setMyDeleteWindow(deleteWindow);
                deleteWindow.setContent(deleteGenre);
                deleteGenre.setSpacing(true);
                deleteGenre.setMargin(true);

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

    private void editUpdateForm(Genre genre){
        idTextField.setVisible(true);
        idTextField.setEnabled(false);
        setMargin(true);
    }

    private void updateGenre(Genre genre, Button updateButton){
        updateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window updateWindow = new Window("Güncelleme");

                GenreAddAndUpdatePage selectedGenreUpdateWindow = new GenreAddAndUpdatePage(genre);
                selectedGenreUpdateWindow.setGenreUpdateWindow(updateWindow);
                selectedGenreUpdateWindow.setMargin(true);
                selectedGenreUpdateWindow.setSpacing(true);

                updateWindow.setContent(selectedGenreUpdateWindow);
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
*/

    // BINDER
    private void buildBinder(){
        binder = new BeanFieldGroup<Book>(Book.class);
        binder.setItemDataSource(bookBindObject);
        binder.bindMemberFields(this);
    }

    private void runBinder(){
        try {
            binder.commit();
        }catch (FieldGroup.CommitException e){
            Notification.show("Lütfen form alanlarını eksiksiz doldurun!", Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public Window getBookUpdateWindow() {
        return bookUpdateWindow;
    }

    public void setBookUpdateWindow(Window bookUpdateWindow) {
        this.bookUpdateWindow = bookUpdateWindow;
    }

}
