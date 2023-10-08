package ui.views.Member;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import core.dao.util.ByteArrayStreamResource;
import core.entity.Member;
import core.service.MemberService;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

public class MemberAddAndUpdatePage extends VerticalLayout {
    @PropertyId("id")
    TextField idTextField;
    @PropertyId("firstName")
    TextField firstNameTextField;
    @PropertyId("lastName")
    TextField lastNameTextField;
    @PropertyId("age")
    TextField ageTextField;

    Table table;
    IndexedContainer indexedContainer;
    Button memberOperationsButton;
    MemberService memberService;
    Member memberBindObject;
    Window memberUpdateWindow;

    private BeanFieldGroup<Member> binder;

    private Upload photoUpload;
    private Image uploadedPhoto;


    public MemberAddAndUpdatePage(Member member){
        memberBindObject = member;
        buildMainLayout();
        setWidth("350px");
        setHeight("300px");

        memberOperationsButton.setIcon(FontAwesome.PENCIL);
        memberOperationsButton.setCaption("Güncelle");
        memberOperationsButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        editUpdateForm();
        this.setMargin(true);
        Page.getCurrent().setTitle("Üye Güncelle | "+member.getFirstName() + " " + member.getLastName() );
    }

    public MemberAddAndUpdatePage(){
        memberBindObject = new Member();
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

    private void buildForm() {
        firstNameTextField = new TextField("Ad");
        lastNameTextField = new TextField("Soyad");
        ageTextField = new TextField("Yaş");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        photoUpload = new Upload("Fotoğraf Yükle", new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                return outputStream;
            }
        });
        photoUpload.addSucceededListener(new Upload.SucceededListener() {
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                byte[] photoData = outputStream.toByteArray();
                memberBindObject.setPhoto(photoData);
                uploadedPhoto.setSource(new ByteArrayStreamResource(photoData, "photo.jpg"));
            }
        });

        uploadedPhoto = new Image("Yüklenen Fotoğraf");
        uploadedPhoto.setVisible(false);

        createMemberOperationsButton();

        addComponents(firstNameTextField, lastNameTextField, ageTextField, photoUpload, uploadedPhoto, memberOperationsButton);

        setMargin(true);
        setSpacing(true);
    }


    private void createMemberOperationsButton(){
        memberOperationsButton = new Button("Ekle");
        addComponent(memberOperationsButton);

        memberOperationsButton.addClickListener(clickEvent -> {
            try {
                runBinder();
                memberService = new MemberService();
                if (memberBindObject.getId() != null){
                    memberService.update(memberBindObject);
                    Page.getCurrent().setTitle("Yazar Güncelle | "+memberBindObject.getFirstName());
                    Notification.show(" ✔️",memberBindObject.getFirstName() + "  adlı üye güncellendi !", Notification.TYPE_HUMANIZED_MESSAGE);
                    UI.getCurrent().getUI().removeWindow(getMemberUpdateWindow());
                }
                else{
                    if (memberBindObject.getFirstName() == null || memberBindObject.getFirstName().isEmpty()){
                        Notification.show("✖","Lütfen yazar adı giriniz.", Notification.TYPE_ERROR_MESSAGE);
                        firstNameTextField.clear();
                    }else if(memberBindObject.getLastName() == null || memberBindObject.getLastName().isEmpty()){
                        Notification.show("✖","Lütfen yazar soyadı giriniz.", Notification.TYPE_ERROR_MESSAGE);
                        lastNameTextField.clear();
                    }else{
                        Notification.show(" ✔️", memberBindObject.getFirstName() + " adlı yazar eklendi  ! ", Notification.TYPE_HUMANIZED_MESSAGE);
                        memberService.add(memberBindObject);
                        fillTable();
                        firstNameTextField.clear();
                        lastNameTextField.clear();
                        ageTextField.clear();
                    }
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    private void buildTableIndexedContainer() {
        indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("id", Long.class, null);
        indexedContainer.addContainerProperty("firstName", String.class, null);
        indexedContainer.addContainerProperty("lastName", String.class, null);
        indexedContainer.addContainerProperty("age", Integer.class, null);
        indexedContainer.addContainerProperty("delete", Button.class, null);
        indexedContainer.addContainerProperty("update", Button.class, null);
    }

    private void buildTable(){
        table = new Table();
        table.setContainerDataSource(indexedContainer);
        table.setColumnHeaders("Id", "Ad", "Soyad", "Yaş" , "Güncelle", "Sil");
    }

    private void fillTable(){
        indexedContainer.removeAllItems();
        memberService = new MemberService();
        List<Member> members = memberService.findAll();
        for (Member member : members) {
            Item item = indexedContainer.addItem(member);

            Button deleteButton = new Button();
            deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
            deleteButton.setIcon(FontAwesome.TRASH);
            deleteMember(member, deleteButton);

            Button updateButton = new Button();
            updateButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
            updateButton.setIcon(FontAwesome.PENCIL);
            updateMember(member, updateButton);

            fillColumns(member, item, deleteButton, updateButton);
        }
    }

    private void fillColumns(Member member, Item item, Button deleteButton, Button updateButton){
        item.getItemProperty("id").setValue(member.getId());
        item.getItemProperty("firstName").setValue(member.getFirstName());
        item.getItemProperty("lastName").setValue(member.getLastName());
        item.getItemProperty("age").setValue(member.getAge());
        item.getItemProperty("delete").setValue(deleteButton);
        item.getItemProperty("update").setValue(updateButton);
    }

    private void editUpdateForm(){
        idTextField.setVisible(true);
        idTextField.setEnabled(false);
        this.setMargin(true);
    }

    private void deleteMember(Member member, Button deleteButton){
        deleteButton.addClickListener(clickEvent -> {
            Window deleteWindow = new Window("Yazar silme");
            deleteWindow.center();
            deleteWindow.setWidth("25%");
            deleteWindow.setHeight("220px");


            MemberDeletePage deleteMember = new MemberDeletePage(member);
            deleteMember.setMyDeleteWindow(deleteWindow);
            deleteWindow.setContent(deleteMember);
            deleteMember.setSpacing(true);
            deleteMember.setMargin(true);


            deleteWindow.addCloseListener( closeEvent -> fillTable() );
            UI.getCurrent().addWindow(deleteWindow);
        });
    }

    private void updateMember(Member member, Button updateButton){
        updateButton.addClickListener(clickEvent -> {
            Window updateWindow = new Window("Güncelleme");

            MemberAddAndUpdatePage selectedMemberUpdateWindow = new MemberAddAndUpdatePage(member);
            selectedMemberUpdateWindow.setMemberUpdateWindow(updateWindow);
            selectedMemberUpdateWindow.setMargin(true);
            selectedMemberUpdateWindow.setSpacing(true);

            updateWindow.setContent(selectedMemberUpdateWindow);
            selectedMemberUpdateWindow.setMargin(true);
            updateWindow.center();
            updateWindow.addCloseListener(closeEvent -> fillTable());
            UI.getCurrent().addWindow(updateWindow);
        });

    }

    private void buildBinder(){
        binder = new BeanFieldGroup<>(Member.class);
        binder.setItemDataSource(memberBindObject);
        binder.bindMemberFields(this);
    }

    private void runBinder(){
        try {
            binder.commit();
        }catch (FieldGroup.CommitException e){
            Notification.show("Lütfen form alanlarını eksiksiz doldurun!");
        }
    }

    public Window getMemberUpdateWindow() {
        return memberUpdateWindow;
    }

    public void setMemberUpdateWindow(Window authorUpdateWindow) {
        this.memberUpdateWindow = authorUpdateWindow;
    }
}
