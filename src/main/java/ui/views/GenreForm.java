package ui.views;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.*;
import core.entity.Genre;
import core.service.GenreService;

import java.util.List;

public class GenreForm extends VerticalLayout {
    @PropertyId("id")
    TextField idTextField;
    @PropertyId("name")
    TextField nameTextField;
    Table table;
    IndexedContainer indexedContainer;
    private BeanFieldGroup<Genre> binder;
    Button genreOperationsButton;
    GenreService genreService;
    Genre genreBindObject;

    public GenreForm() {
        genreBindObject = new Genre();
        Page.getCurrent().setTitle("Tür ekle");
        nameTextField.setValue("");
        genreOperationsButton.setCaption("Ekle");

        buildMainLayout();
        buildTableIndexedContainer();
        buildTable();
        fillTable();

        Label lblTableTitle = new Label();
        lblTableTitle.setValue("Türler");
        addComponent(lblTableTitle);
        addComponent(table);
    }

    private void buildMainLayout() {
        formBuild();
        buildBinder();
    }

    private void formBuild() {
        idTextField = new TextField("Tür Id");
        idTextField.setValue("0");
        addComponent(idTextField);
        idTextField.setVisible(false);

        nameTextField = new TextField("Tür adı");
        addComponent(nameTextField);

        createGenreOperationsButton();
        this.setMargin(true);
    }

    private void createGenreOperationsButton() {
        genreOperationsButton = new Button();
        addComponent(genreOperationsButton);

        genreOperationsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    runBinder();
                    genreService = new GenreService();
                    if (genreBindObject.getName() == null || genreBindObject.getName().equals("")) {
                        Notification.show("✖", "Lütfen tür adı giriniz !", Notification.TYPE_ERROR_MESSAGE);
                        nameTextField.clear();
                    } else {
                        Notification.show(" ✔️", genreBindObject.getName() + " adlı tür eklendi  ! ", Notification.TYPE_HUMANIZED_MESSAGE);
                        genreService.add(genreBindObject);
                        fillTable();
                        nameTextField.clear();
                    }

                    genreBindObject = new Genre();
                    binder.setItemDataSource(genreBindObject);

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
    }



    private void fillTable() {
        indexedContainer.removeAllItems();
        GenreService genreService = new GenreService();
        List<Genre> genres = genreService.findAll();
        for (Genre genre : genres) {
            Item item = indexedContainer.addItem(genre);

            fillColumns(genre, item);
        }
    }

    private void fillColumns(Genre genre, Item item) {
        item.getItemProperty("id").setValue(genre.getId());
        item.getItemProperty("name").setValue(genre.getName());
    }

    private void buildTable() {
        table = new Table();
        table.setContainerDataSource(indexedContainer);

        table.setWidth("100%");
        table.setSelectable(true);
        table.setMultiSelectMode(MultiSelectMode.SIMPLE);
        table.setMultiSelect(false);
        table.setColumnHeaders( "Id", "Ad");


        table.setColumnWidth("id",30);
        table.setColumnWidth("name",300);
    }

    private void buildTableIndexedContainer() {
        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id", Long.class, null);
        indexedContainer.addContainerProperty("name", String.class, null);
    }


    private void buildBinder() {
        binder = new BeanFieldGroup<Genre>(Genre.class);
        binder.setItemDataSource(genreBindObject);
        binder.bindMemberFields(this);
    }

    private void runBinder() {
        try {
            binder.commit();
        } catch (FieldGroup.CommitException e) {
            Notification.show("Lütfen form alanlarını eksiksiz doldurun");
        }
    }
}
