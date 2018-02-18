package sample;


import javafx.application.Application;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class View extends Application {
    private Controller controller = new Controller();
    private static final String title = "Document Management System";

    private final TableView<Document> documentsTable = new TableView<>();
    private final TableView<Keyword> keywordTable = new TableView<>();
    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox root = new VBox();
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


        //TableColumn<Keyword, String> checkBoxName = new TableColumn<>("Select");
        //checkBoxName.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        keywordTable.setItems(controller.getKeywordsObList());
        keywordTable.getColumns().add(keywordName);
        keywordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // creating the Hbox for the add and delete buttons
        HBox addAndDeleteHbox = new HBox();
        Button addDocumentBtn = new Button("+");
        addDocumentBtn.setDisable(true);
        Button deleteDocumentBtn = new Button("-");
        deleteDocumentBtn.setDisable(true);
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


        //When the button is pressed, a new window with a new interface is created
        openKeywordsWindowBtn.setOnAction(c -> {
            VBox keyWordsVBox = new VBox();
            Label keywordLabel = new Label("Keyword: ");
            TextField keywordTextField = new TextField();
            Button addKeywordBtn = new Button("+");
            addKeywordBtn.setDisable(true);
            Button deleteKeywordBtn = new Button("-");
            deleteKeywordBtn.setDisable(true);

            Button deleteConnectionToDocumentBtn = new Button("Delete");
            Button addKeywordToDocumentsBtn = new Button("Add");

            keyWordsVBox.getChildren().addAll(keywordTable, addKeywordToDocumentsBtn, deleteConnectionToDocumentBtn,keywordLabel, keywordTextField, addKeywordBtn, deleteKeywordBtn);

            addKeywordToDocumentsBtn.setDisable(true);
            deleteConnectionToDocumentBtn.setDisable(true);
            try{
                // Creating a new stage for the Keyword window
               Stage keywordStage = new Stage();
               keywordStage.setTitle("Keywords");
               keywordStage.setScene(new Scene(keyWordsVBox,200,200));
               keywordStage.setMinWidth(300);
               keywordStage.setMinHeight(300);
               keywordStage.show();

               //When the main window is closed, the keyword window will also be closed
               primaryStage.setOnCloseRequest(closeEvent -> {
                    keywordStage.close();
                });

                keywordStage.setOnCloseRequest(keywordStageCloseEvent -> {
                    keywordTable.getSelectionModel().clearSelection();
                });


                keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.length() > 0){
                        addKeywordBtn.setDisable(false);
                    }else
                        addKeywordBtn.setDisable(true);

                });

                keywordTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
                    if (!keywordTable.getSelectionModel().isEmpty()){
                        deleteKeywordBtn.setDisable(false);
                    }else {
                        deleteKeywordBtn.setDisable(true);
                    }
                  updateKeywordAddDeleteBtn(keywordTable, documentsTable, addKeywordToDocumentsBtn, deleteConnectionToDocumentBtn);
                });

                documentsTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
                    updateKeywordAddDeleteBtn(keywordTable, documentsTable, addKeywordToDocumentsBtn, deleteConnectionToDocumentBtn);
                });


                // When a keyword is put into the text field, it will be added to the table view and displayed, queries are are called to update
                addKeywordBtn.setOnAction(add ->{
                   try {
                       // Getting the keyword from the text field
                       String value = keywordTextField.getText();
                       //If the text field is not empty, the keyword can be added
                       if (!value.isEmpty()) {
                           controller.addKeyword(new Keyword(value));
                           controller.startQuery();
                       } else {
                           System.out.println("No Name specified");
                       }
                   }catch (NullPointerException z){
                       System.out.println(z + "null");
                   }
               });

                 // When a keyword is selected and this button is pressed, the selected keyword will be deleted
                deleteKeywordBtn.setOnAction(event ->{
                   try {
                       Keyword keyword = keywordTable.getSelectionModel().getSelectedItem();
                       if (null == keyword) {
                           System.out.println("No keyword has been selected!");
                       } else {
                           System.out.println("Keyword has been deleted");
                           controller.deleteKeyword(keyword.getKeywordID());
                           controller.deleteKeywordFromActiveKeywordQuery(keyword.getKeywordID());
                           controller.startQuery();
                       }
                   }catch (NullPointerException x){
                       System.out.println(x + "x");
                   }
               });


                /**
                 * When the add button is clicked, a keyword will be connected to a document
                 * The document id and the keywordID are saved in the ActiveKeywords table
                 */
                addKeywordToDocumentsBtn.setOnAction( d ->{
                    try {
                        int documentID = documentsTable.getSelectionModel().getSelectedItem().getID();
                        int keyword = keywordTable.getSelectionModel().getSelectedItem().getKeywordID();
                        controller.connectKeywordToDocument(documentID, keyword);
                        controller.startQuery();
                    }catch (NullPointerException a){
                        System.out.println(a + "Please select something");
                    }

                });


                // Deletes a keyword from a document
                deleteConnectionToDocumentBtn.setOnAction(k ->{
                    try {
                        int id = documentsTable.getSelectionModel().getSelectedItem().getID();
                        int keyword = keywordTable.getSelectionModel().getSelectedItem().getKeywordID();
                        controller.deleteSingleKeyword(id, keyword);

                        System.out.println("Keyword connected to the document has been deleted");
                    }catch (NullPointerException e){
                        System.out.println(" delete connection to Document " + e);
                    }
                });
                }catch (Exception e){
                System.out.println(e + "error while deleting the connection between the document and the keyword");
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
         * Adds a document to the Document table
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

        // Adds the keyword to the text area designated for the keyword display
        documentsTable.getSelectionModel().selectedItemProperty().addListener((Observable observable) -> {
            keywordsTextArea.clear();
            try {
                controller.startKeywordIDQuery(documentsTable.getSelectionModel().getSelectedItem().getID());
            }catch (NullPointerException e){
                System.out.println(e + "This is caught, idk why this error happens, but every still works");
            }
            if (!documentsTable.getSelectionModel().isEmpty()){
                deleteDocumentBtn.setDisable(false);
                List<Integer> keywordIDArray = controller.getKeywordIdArray();
                for (int i = 0; i < keywordIDArray.size(); i++){
                    System.out.println(keywordIDArray.get(i));
                    controller.startKeywordDisplayQuery(keywordIDArray.get(i));
                }controller.getKeywordIdArray().clear();
                ArrayList<String> keywordArrayList = controller.getDisplayKeywordsArray();
                for (int i = 0; i < keywordArrayList.size(); i++){
                    keywordsTextArea.appendText(keywordArrayList.get(i) + " ");
                }
                keywordArrayList.clear();
            } else {
                deleteDocumentBtn.setDisable(true);
            }
        });
        controller.startQuery();


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

    /**
     * This method updates the buttons for connecting a keyword to a document and for deleting a document
     * @param keywordTable is the tableView for the Keywords. Is used to check if something is selected
     * @param documentsTable is the tableView for the Documents. Is used to check if something is selected
     * @param addKeywordToDocumentsBtn is the add button that has to be enabled when both tableViews have a selected property
     * @param deleteConnectionToDocumentBtn is the delete button that has to be enabled when both tableViews have selected property
     */
    private void updateKeywordAddDeleteBtn(TableView keywordTable, TableView documentsTable, Button addKeywordToDocumentsBtn, Button deleteConnectionToDocumentBtn){
        if (!keywordTable.getSelectionModel().isEmpty() && !documentsTable.getSelectionModel().isEmpty()){
            addKeywordToDocumentsBtn.setDisable(false);
            deleteConnectionToDocumentBtn.setDisable(false);
        } else{
            addKeywordToDocumentsBtn.setDisable(true);
            deleteConnectionToDocumentBtn.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
