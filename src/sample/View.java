package sample;


import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.security.Key;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.ResourceBundle;


public class View extends Application {
    private Controller controller = new Controller();
    private static final String title = "Document Management System";

    private final TableView<Document> documentsTable = new TableView<>();
    private final TableView<Keyword> keywordTable = new TableView<>();
    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox root = new VBox();
        final String fileName = "bookDatabase.db";
        controller.setFilename(fileName);
        controller.startQuery();

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
        documentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Keyword, String> keywordName = new TableColumn<>("Keyword");
        keywordName.setCellValueFactory(new PropertyValueFactory<>("keyword"));
        keywordName.setMinWidth(110.0);


        TableColumn<Keyword, String> checkBoxName = new TableColumn<>("Select");
        checkBoxName.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        keywordTable.setItems(controller.getKeywordsObList());
        keywordTable.getColumns().addAll(checkBoxName,keywordName);
        keywordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // creating the Hbox for the add and delete buttons
        HBox addAndDeleteHbox = new HBox();
        Button addDocumentBtn = new Button("+");
        addDocumentBtn.setDisable(true);
        Button deleteDocumentBtn = new Button("-");

        addDocumentBtn.setMinSize(30.0, 30.0);
        deleteDocumentBtn.setMinSize(30.0, 30.0);

        Pane leftSpacePane = new Pane();
        Pane rightSpacePane = new Pane();


        HBox.setHgrow(leftSpacePane, Priority.ALWAYS);
        HBox.setHgrow(rightSpacePane, Priority.ALWAYS);
        addAndDeleteHbox.setSpacing(5.0);
        addAndDeleteHbox.getChildren().addAll(leftSpacePane, addDocumentBtn, deleteDocumentBtn, rightSpacePane);

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


        HBox keywordsHbox = new HBox();
        TextArea keywordsTextArea = new TextArea();
        Label keywordDisplayLabel = new Label("Keywords");
        keywordsTextArea.setEditable(false);
        Button openKeywordsWindowBtn = new Button("Keywords");

        keywordsHbox.getChildren().addAll(keywordDisplayLabel, keywordsTextArea, openKeywordsWindowBtn);
        keywordsHbox.setSpacing(10.1);


        // new PropertyValueFactory<>("ID") => getID
        // new PropertyValueFactory<>("BookID") => getBookID

        root.getChildren().addAll(documentsTable, addAndDeleteHbox, idFields, titleFieldHbox, authorFieldHbox, keywordsHbox);
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();




        openKeywordsWindowBtn.setOnAction(c -> {
            VBox keyWordsVbox = new VBox();
            Label keywordLaben = new Label("Keyword: ");
            TextField keywordTextField = new TextField();
            Button addKeywordBtn = new Button("+");
            Button deleteKeywordBtn = new Button("-");
            Button deleteConnectionToDocumentBtn = new Button("Delete");
            Button addKeywordsAndDocumentsBtn = new Button("Add");
            keyWordsVbox.getChildren().addAll(keywordTable, addKeywordsAndDocumentsBtn, deleteConnectionToDocumentBtn,keywordLaben, keywordTextField, addKeywordBtn, deleteKeywordBtn);
            //Dialog dialog = new Dialog();
            //dialog.getDialogPane().setContent(keyWordsVbox);

            try{

               Stage keywordStage = new Stage();
               keywordStage.setTitle("Keywords");
               keywordStage.setScene(new Scene(keyWordsVbox,200,200));
               keywordStage.show();
                //dialog.show();

               addKeywordBtn.setOnAction(add ->{
                   try {

                       String value = keywordTextField.getText();
                       if (!value.isEmpty()) {
                           controller.addKeyword(new Keyword(value));
                           controller.startQuery();
                       } else {
                           System.out.println("No Name specified");
                       }
                   }catch (NullPointerException z){
                       System.out.println(z + "This is z");
                   }
               });

               deleteKeywordBtn.setOnAction(event ->{
                   try {

                       Keyword keyword = keywordTable.getSelectionModel().getSelectedItem();
                       if (null == keyword) {
                           System.out.println("No keyword has been selected!");
                       } else {
                           System.out.println("Keyword has been deleted");
                           controller.deleteKeyword(keyword.getKeyword());
                           controller.startQuery();
                       }
                   }catch (NullPointerException x){
                       System.out.println(x + "this is x");
                   }
               });

                addKeywordsAndDocumentsBtn.setOnAction( d ->{
                    try {
                        int documentID = documentsTable.getSelectionModel().getSelectedItem().getID();
                        String keyword = keywordTable.getSelectionModel().getSelectedItem().getKeyword();
                        //System.out.println(test);
                        controller.connectKeywordToDocument(documentID, keyword);
                        controller.startQuery();
                    }catch (NullPointerException a){
                        System.out.println(a + "Please select somethingmymymy");
                    }

                });


                // Deletes a keyword from a document
                deleteConnectionToDocumentBtn.setOnAction(k ->{
                    try {
                        int id = documentsTable.getSelectionModel().getSelectedItem().getID();
                        String keyword = keywordTable.getSelectionModel().getSelectedItem().getKeyword();
                        controller.deleteSingleKeyword(id, keyword);

                        System.out.println("Keyword connected to the document has been deleted");
                    }catch (NullPointerException e){
                        System.out.println(" delete connection to Document " + e);
                    }
                });

            }catch (Exception e){
                System.out.println(e + "this error is bad mmkay");
            }
        });





        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddBtn(idTextField, titleTextField, authorTextField, addDocumentBtn);
        });


        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddBtn(idTextField, titleTextField, authorTextField, addDocumentBtn);
        });

        authorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateAddBtn(idTextField, titleTextField, authorTextField, addDocumentBtn);
        });


        /**
         * Adds a document to the document table
         * starts the query to update
         */
        addDocumentBtn.setOnAction(c ->{
            Integer intValue;
            try {
                intValue = Integer.parseInt(idTextField.getText());
                controller.getaddDocument(new Document(intValue, titleTextField.getText(), authorTextField.getText()));
                controller.startQuery();
            } catch (NumberFormatException e){
                System.out.println("That is not a Number you twat!" + e);
            }
        });

        /**
         * Deletes the document together with every the connected keywords that are connected to it in the tables
         * will only delete the connection between the document and the keywords
         * the keywords are still available in the keyword table window
         * calls the method to delete a selected document
         * calls the method to delete the keywords associated with the document id
         * starts the query to update
         */
        deleteDocumentBtn.setOnAction(c ->{
            try {
                Document document = documentsTable.getSelectionModel().getSelectedItem();
                if (document == null) {
                    System.out.println("No document Selected! ");
                } else {
                    controller.deletedConnectedKeywordFromDocument(document.getID());
                    controller.deleteDocument(document.getID());


                    controller.startQuery();
                }
            }catch (NullPointerException b){
                System.out.println(b + "this is b");
            }
        });
    }


    /**
     * Method to change the add button for the documents
     * if every field is filled the "add button" will be usable
     * @param id takes the input of the text field for the id
     * @param title takes the input of the text field for the titel
     * @param author takes the input of the text field for the author
     * @param add gets the add button
     */
    private void updateAddBtn(TextField id, TextField title, TextField author, Button add){
        if (!id.getText().isEmpty() && !title.getText().isEmpty() && !author.getText().isEmpty()) {
            add.setDisable(false);
        }else{
            add.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
