package ui.views.Genre;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import core.entity.Genre;
import core.service.GenreService;

import java.util.List;

public class GenreAddAndUpdatePage extends VerticalLayout {
    Window genreUpdateWindow;

    GenreService genreService;
    Genre genreBindObject;

    @PropertyId("id")
    TextField idTextField;
    @PropertyId("name")
    TextField nameTextField;
    Button genreOperationsButton;

    Table table;
    IndexedContainer indexedContainer;

    private BeanFieldGroup<Genre> binder;

    public GenreAddAndUpdatePage(Genre genre){
        genreBindObject = genre;
        buildMainLayout();
        setWidth("350px");
        setHeight("300px");

        genreOperationsButton.setIcon(FontAwesome.PENCIL);
        genreOperationsButton.setCaption("Güncelle");
        genreOperationsButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        editUpdateForm(genre);
        setMargin(true);
        Page.getCurrent().setTitle("Tür Güncelle");
    }

    public GenreAddAndUpdatePage(){
        genreBindObject = new Genre();
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

        nameTextField = new TextField("Tür adı");
        addComponent(nameTextField);
        createGenreOperationsButton();
        setMargin(true);
        setSpacing(true);
    }

    private void createGenreOperationsButton(){
        genreOperationsButton = new Button("Ekle");
        addComponent(genreOperationsButton);

        genreOperationsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    runBinder();
                    genreService = new GenreService();
                    if (genreBindObject.getId() != null){
                        genreService.update(genreBindObject);
                        Page.getCurrent().setTitle("Tür güncelle");
                        Notification.show(" ✔️",genreBindObject.getName() + "  adlı tür güncellendi !", Notification.TYPE_HUMANIZED_MESSAGE);
                        UI.getCurrent().getUI().removeWindow(getGenreUpdateWindow());
                    }else {
                        if (genreBindObject.getName() == null || genreBindObject.getName().equals("")){
                            Notification.show("✖","Lütfen tür adı giriniz.", Notification.TYPE_ERROR_MESSAGE);
                        }else{
                            Notification.show(" ✔️", genreBindObject.getName() + " adlı tür eklendi  ! ", Notification.TYPE_HUMANIZED_MESSAGE);
                            genreService.add(genreBindObject);
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
        indexedContainer.addContainerProperty("update", Button.class, null);
        indexedContainer.addContainerProperty("delete", Button.class, null);
    }

    private void buildTable(){
        table = new Table();
        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("ID", "Tür", "Güncelle", "Sil");
        table.setWidth("450px");
    }

    private void fillTable(){
        indexedContainer.removeAllItems();
        genreService = new GenreService();
        List<Genre> genres = genreService.findAll();
        for (Genre genre : genres) {
            Item item = indexedContainer.addItem(genre);

            Button updateButton = new Button();
            updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
            updateButton.setIcon(FontAwesome.PENCIL);
            updateGenre(genre, updateButton);

            Button deleteButton = new Button();
            deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
            deleteButton.setIcon(FontAwesome.TRASH);
            deleteGenre(genre, deleteButton);

            fillColumns(genre, item, updateButton, deleteButton);
        }
    }

    private void fillColumns(Genre genre, Item item, Button updateButton, Button deleteButton){
        item.getItemProperty("id").setValue(genre.getId());
        item.getItemProperty("name").setValue(genre.getName());
        item.getItemProperty("update").setValue(updateButton);
        item.getItemProperty("delete").setValue(deleteButton);
    }

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


    // BINDER
    private void buildBinder(){
        binder = new BeanFieldGroup<Genre>(Genre.class);
        binder.setItemDataSource(genreBindObject);
        binder.bindMemberFields(this);
    }

    private void runBinder(){
        try {
            binder.commit();
        }catch (FieldGroup.CommitException e){
            Notification.show("Lütfen form alanlarını eksiksiz doldurun!", Notification.TYPE_ERROR_MESSAGE);
        }
    }

    public Window getGenreUpdateWindow() {
        return genreUpdateWindow;
    }

    public void setGenreUpdateWindow(Window genreUpdateWindow) {
        this.genreUpdateWindow = genreUpdateWindow;
    }
}
