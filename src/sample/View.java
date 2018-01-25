package sample;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.security.Key;
import java.util.ResourceBundle;


public class View extends Application {
    private Controller controller = new Controller();
    private static final String title = "Document management system";

    private final TableView<Document> documentsTable = new TableView<>();
    private final TableView<Keyword> keywordTable = new TableView<>();
    private TableColumn<Keyword, Boolean> column;
    @Override
    public void start(Stage primaryStage) throws Exception{


        VBox root = new VBox();
        final String fileName = "bookDatabase.db";
        controller.setFilename(fileName);
        controller.startQuery();
        //ObservableList<Document> data = FXCollections.observableArrayList(controller.getDocumentsDataObList());

        TableColumn<Document, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        idColumn.setMinWidth(110.0);

        TableColumn<Document, String> bookTitle = new TableColumn<>("Title");
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookTitle.setMinWidth(110.0);

        TableColumn<Document, String> authorName = new TableColumn<>("Author");
        authorName.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorName.setMinWidth(110.0);

        documentsTable.setItems(controller.getDocumentsDataObList());
        documentsTable.getColumns().addAll(idColumn, bookTitle, authorName);

        TableColumn<Keyword, String> keywordName = new TableColumn<>("Keyword");
        keywordName.setCellValueFactory(new PropertyValueFactory<>("keyword"));
        keywordName.setMinWidth(110.0);


        TableColumn<Keyword, String> checkBoxName = new TableColumn<>("Select");
        checkBoxName.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        keywordTable.setItems(controller.getKeywordsObList());
        keywordTable.getColumns().addAll(checkBoxName,keywordName);

        // creating the Hbox for the add and delete buttons
        HBox addAndDeleteHbox = new HBox();
        Button addBtn = new Button("+");
        addBtn.setDisable(true);
        Button deleteBtn = new Button("-");

        addBtn.setMinSize(30.0, 30.0);
        deleteBtn.setMinSize(30.0, 30.0);

        Pane leftSpacePane = new Pane();
        Pane rightSpacePane = new Pane();


        HBox.setHgrow(leftSpacePane, Priority.ALWAYS);
        HBox.setHgrow(rightSpacePane, Priority.ALWAYS);
        addAndDeleteHbox.setSpacing(5.0);
        addAndDeleteHbox.getChildren().addAll(leftSpacePane, addBtn, deleteBtn, rightSpacePane);

        // creating the TextFields for the user inputs
        HBox idFields = new HBox();
        Label idTextLabel = new Label("ID");
        TextField idTextField = new TextField();
        idFields.getChildren().addAll(idTextLabel, idTextField);
        idFields.setSpacing(38);

        HBox titleFieldHbox = new HBox();
        Label titleLabel = new Label("Title");
        TextField titleTextField = new TextField();
        titleFieldHbox.getChildren().addAll(titleLabel, titleTextField);
        titleFieldHbox.setSpacing(28);


        HBox authorFieldHbox = new HBox();
        Label authorLabel = new Label("Author");
        TextField authorTextField = new TextField();
        authorFieldHbox.getChildren().addAll(authorLabel, authorTextField);
        authorFieldHbox.setSpacing(15);


        //




        Button openKeywordsWindowBtn = new Button("Keywords");

        root.getChildren().addAll(documentsTable, addAndDeleteHbox, idFields, titleFieldHbox, authorFieldHbox, openKeywordsWindowBtn);

        // new PropertyValueFactory<>("ID") => getID
        // new PropertyValueFactory<>("BookID") => getBookID


        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();







        openKeywordsWindowBtn.setOnAction(c -> {
            VBox keyWordsVbox = new VBox();
            Label keywordLaben = new Label("Keyword: ");
            TextField keywordTextField = new TextField();
            Button addKeywordBtn = new Button("+");
            Button deleteKeywordBtn = new Button("-");
            keyWordsVbox.getChildren().addAll(keywordTable, deleteKeywordBtn, keywordLaben, keywordTextField, addKeywordBtn);
            try{
               Stage keywordStage = new Stage();
               keywordStage.setTitle("Keywords");
               keywordStage.setScene(new Scene(keyWordsVbox,200,200));
               keywordStage.show();


               addKeywordBtn.setOnAction(add ->{

                   String value = keywordTextField.getText();
                   controller.addKeyword(new Keyword(value));
                   controller.startQuery();
               });

               deleteKeywordBtn.setOnAction(event ->{
                   Keyword keyword = keywordTable.getSelectionModel().getSelectedItem();
                   if(null == keyword){
                       System.out.println("No keyword has been selected!");
                   }else{
                       controller.deleteKeyword(keyword.getKeyword());
                       controller.startQuery();
                   }
               });




            }catch (Exception e){
                System.out.println(e + "this error is bad mmkay");
            }
        });


        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddBtn(idTextField, titleTextField, authorTextField, addBtn);
        });


        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddBtn(idTextField, titleTextField, authorTextField, addBtn);
        });

        authorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddBtn(idTextField, titleTextField, authorTextField, addBtn);
        });


        addBtn.setOnAction(c ->{
            Integer intValue;
            try {
                intValue = Integer.parseInt(idTextField.getText());
                controller.getaddDocument(new Document(intValue, titleTextField.getText(), authorTextField.getText()));
                controller.startQuery();
            } catch (NumberFormatException e){
                System.out.println("Thats not a Number you twat!" + e);
            }
        });

        deleteBtn.setOnAction(c ->{
            Document document = documentsTable.getSelectionModel().getSelectedItem();
            if (document == null){
                System.out.println("No document Selected! ");
            }else{
            System.out.println(document.getID());
            controller.deleteDocument(document.getID());
            controller.startQuery();
            }
        });
    }


    private void updateAddBtn(TextField id, TextField titel, TextField author, Button add){
        if (!id.getText().isEmpty() && !titel.getText().isEmpty() && !author.getText().isEmpty()) {
            add.setDisable(false);
        }else{
            add.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
