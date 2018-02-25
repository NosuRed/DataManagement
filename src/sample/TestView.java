package sample;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestView extends Application {
        private Controller controller = new Controller();
        private static final String title = "Document Management System";

        private final TableView<Document> documentsTable = new TableView<>();
        private final TableView<Keyword> keywordTable = new TableView<>();
        private int docID = -1;

        @Override
        public void start(Stage primaryStage) throws Exception {
            VBox root = new VBox();
            GridPane gridPaneMainWindow = new GridPane();
            Separator separator = new Separator();
            separator.setMinHeight(5);

            gridPaneMainWindow.setPadding(new Insets(10, 10, 10, 10));
            gridPaneMainWindow.setVgap(10);
            gridPaneMainWindow.setHgap(10);

            controller.startDocumentTableQuery();
            controller.startKeywordTableQuery();


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
            documentsTable.setMinHeight(100);

            documentsTable.getColumns().addAll(idColumn, bookTitle, authorName);
            documentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            TableColumn<Keyword, String> keywordName = new TableColumn<>("Keyword");
            keywordName.setCellValueFactory(new PropertyValueFactory<>("keyword"));
            keywordName.setMinWidth(110.0);

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


            Label idTextLabel = new Label("ID");
            TextField idTextField = new TextField();
            GridPane.setConstraints(idTextLabel, 0,0);
            GridPane.setConstraints(idTextField, 1, 0);
            gridPaneMainWindow.getChildren().addAll(idTextLabel, idTextField);

            Label titleLabel = new Label("Title");
            TextField titleTextField = new TextField();
            GridPane.setConstraints(titleLabel, 0,1);
            GridPane.setConstraints(titleTextField, 1, 1);
            gridPaneMainWindow.getChildren().addAll(titleLabel, titleTextField);


            Label authorLabel = new Label("Author");
            TextField authorTextField = new TextField();
            GridPane.setConstraints(authorLabel, 0,2);
            GridPane.setConstraints(authorTextField, 1,2);
            gridPaneMainWindow.getChildren().addAll(authorLabel, authorTextField);


            TextArea keywordsTextArea = new TextArea();
            Label keywordDisplayLabel = new Label("Keywords");
            keywordsTextArea.setEditable(false);
            keywordsTextArea.setWrapText(true);
            Button openKeywordsWindowBtn = new Button("Keywords");
            openKeywordsWindowBtn.setMaxSize(100,25);
            openKeywordsWindowBtn.setMinSize(100, 25);

            GridPane.setConstraints(keywordDisplayLabel, 0,3);
            GridPane.setConstraints(keywordsTextArea, 1,3);
            GridPane.setConstraints(openKeywordsWindowBtn, 2, 3);
            VBox.setVgrow(keywordsTextArea, Priority.ALWAYS);
            gridPaneMainWindow.getChildren().addAll(keywordDisplayLabel, keywordsTextArea, openKeywordsWindowBtn);



            // elements for the reference display in the main window
            Button openReferenceWindowBtn = new Button("References");
            openReferenceWindowBtn.setMaxSize(100,25);
            openReferenceWindowBtn.setMinSize(100, 25);
            openReferenceWindowBtn.setDisable(true);
            TextArea refereceTextArea = new TextArea();
            Label referenceDisplayLabel = new Label("Reference");
            refereceTextArea.setEditable(false);
            refereceTextArea.setWrapText(true);


            GridPane.setConstraints(referenceDisplayLabel, 0, 4);
            GridPane.setConstraints(refereceTextArea, 1, 4);
            GridPane.setConstraints(openReferenceWindowBtn, 2, 4);
            gridPaneMainWindow.getChildren().addAll(referenceDisplayLabel, refereceTextArea, openReferenceWindowBtn);


            GridPane.setHgrow(idTextField, Priority.ALWAYS);
            root.getChildren().addAll(documentsTable, separator,addAndDeleteHbox, gridPaneMainWindow);
            VBox.setVgrow(documentsTable, Priority.ALWAYS);
            root.setSpacing(5);
            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root, 650, 500));
            primaryStage.setMinHeight(450);
            primaryStage.setMinWidth(250);
            primaryStage.show();


            openKeywordsWindowBtn.setDisable(true);

            // stage for the Keywords when the button is clicked
            openKeywordsWindowBtn.setOnAction(keywordWindowOpenEvent -> {
                keywordTable.setMinHeight(100);
                VBox keyWordsVBox = new VBox();
                Separator keywordWndSep = new Separator();

                openKeywordsWindowBtn.setDisable(true);

                HBox keywordToDocHBox = new HBox();
                keywordToDocHBox.setSpacing(10);
                Pane spacerForDocHbox = new Pane();
                Pane spacerForDocHbox1 = new Pane();
                Label addKeywordToDocLabel = new Label("Add to Document:");
                Label deleteKeywordFromDocLabel = new Label("Delete from Document:");


                Button deleteConnectionToDocumentBtn = new Button("Disconnect");
                deleteConnectionToDocumentBtn.setMinSize(60,25);

                Button addKeywordToDocumentsBtn = new Button("Connect");
                addKeywordToDocumentsBtn.setMinSize(60,25);

                addKeywordToDocumentsBtn.setDisable(true);
                deleteConnectionToDocumentBtn.setDisable(true);
                keywordToDocHBox.getChildren().addAll(spacerForDocHbox, addKeywordToDocLabel, addKeywordToDocumentsBtn, deleteKeywordFromDocLabel, deleteConnectionToDocumentBtn, spacerForDocHbox1);
                HBox.setHgrow(spacerForDocHbox, Priority.ALWAYS);
                HBox.setHgrow(spacerForDocHbox1, Priority.ALWAYS);



                Label keywordLabel = new Label("Add a Keyword: ");
                HBox keywordBtnBox = new HBox();

                Button addKeywordBtn = new Button("+");
                addKeywordBtn.setMinSize(30, 30);
                addKeywordBtn.setDisable(true);
                Button deleteKeywordBtn = new Button("-");
                deleteKeywordBtn.setMinSize(30, 30);
                deleteKeywordBtn.setDisable(true);
                keywordBtnBox.setSpacing(10);
                Pane spacerForKeywordBtnBox = new Pane();
                Pane spacerForKeywordBtnBox1 = new Pane();
                keywordBtnBox.getChildren().addAll(spacerForKeywordBtnBox ,addKeywordBtn, deleteKeywordBtn, spacerForKeywordBtnBox1);

                HBox.setHgrow(spacerForDocHbox, Priority.ALWAYS);
                HBox.setHgrow(spacerForDocHbox1, Priority.ALWAYS);

                TextField keywordTextField = new TextField();
                HBox keywordTextFieldHBox = new HBox();
                keywordTextFieldHBox.getChildren().addAll(keywordTextField);
                keywordTextFieldHBox.setPadding(new Insets(10));
                HBox.setHgrow(keywordTextField, Priority.ALWAYS);
                keyWordsVBox.getChildren().addAll(keywordTable, keywordWndSep,keywordToDocHBox, keywordLabel, keywordTextFieldHBox, keywordBtnBox);
                VBox.setVgrow(keywordTable, Priority.ALWAYS);


                try {
                    // Creating a new stage for the Keyword window
                    Stage keywordStage = new Stage();
                    keywordStage.setTitle("Keywords");
                    keywordStage.setScene(new Scene(keyWordsVBox, 200, 200));
                    keywordStage.setMinWidth(300);
                    keywordStage.setMinHeight(300);
                    keywordStage.initModality(Modality.WINDOW_MODAL);
                    keywordStage.initOwner(primaryStage);
                    keywordStage.show();


                    // when the keywordstage is closed, the keyword btn gets enabled again
                    keywordStage.setOnCloseRequest(keywordStageCloseEvent -> {
                        openKeywordsWindowBtn.setDisable(false);
                        keywordTable.getSelectionModel().clearSelection();
                    });


                    keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.length() > 0) {
                            addKeywordBtn.setDisable(false);
                        } else
                            addKeywordBtn.setDisable(true);

                    });

                    keywordTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
                        if (!keywordTable.getSelectionModel().isEmpty()) {
                            keywordTextField.setText(keywordTable.getSelectionModel().getSelectedItem().getKeyword());
                            deleteKeywordBtn.setDisable(false);
                        } else {
                            deleteKeywordBtn.setDisable(true);
                        }
                        updateKeywordAddDeleteBtn(keywordTable, documentsTable, addKeywordToDocumentsBtn, deleteConnectionToDocumentBtn);
                    });

                /*
                //Dead code, kept for safety
                documentsTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
                    updateKeywordAddDeleteBtn(keywordTable, documentsTable, addKeywordToDocumentsBtn, deleteConnectionToDocumentBtn);
                });*/


                    // When a keyword is put into the text field, it will be added to the table view and displayed, queries are are called to update
                    addKeywordBtn.setOnAction(add -> {
                        try {
                            // Getting the keyword from the text field
                            String value = keywordTextField.getText();
                            //If the text field is not empty, the keyword can be added
                            if (!value.isEmpty()) {
                                try {
                                    controller.addKeyword(new Keyword(value));
                                    controller.startKeywordTableQuery();
                                } catch (ErrorExceptionThrow errorThrow) {
                                    Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                                    errorAlert.setTitle("Keyword already exists error!");
                                    errorAlert.setHeaderText(null);
                                    errorAlert.setContentText("This keyword already exists! " + "\n" + "Please select another keyword!");
                                    errorAlert.showAndWait();
                                }

                            } else {
                                System.out.println("No Name specified");
                            }
                        } catch (NullPointerException z) {
                            System.out.println(z + "null");
                        }
                    });

                    // When a keyword is selected and this button is pressed, the selected keyword will be deleted
                    deleteKeywordBtn.setOnAction(event -> {
                        Keyword keyword = keywordTable.getSelectionModel().getSelectedItem();
                        try {
                            if (null == keyword) {
                                System.out.println("No keyword has been selected!");
                            } else {
                                System.out.println("Keyword has been deleted");
                                controller.deleteKeyword(keyword.getKeywordID());
                                controller.deleteKeywordFromActiveKeywordQuery(keyword.getKeywordID());
                                controller.startKeywordTableQuery();
                                updateKeywordTextAreaDisplay(deleteDocumentBtn, keywordsTextArea);
                            }
                            controller.startKeywordTableQuery();
                        } catch (NullPointerException x) {
                            System.out.println(x + "x");
                        }
                    });


                    /**
                     * When the add button is clicked, a keyword will be connected to a document
                     * The document id and the keywordID are saved in the ActiveKeywords table
                     */
                    addKeywordToDocumentsBtn.setOnAction(d -> {
                        try {
                            int documentID = documentsTable.getSelectionModel().getSelectedItem().getID();
                            int keyword = keywordTable.getSelectionModel().getSelectedItem().getKeywordID();
                            controller.connectKeywordToDocument(documentID, keyword);
                            updateKeywordTextAreaDisplay(deleteDocumentBtn, keywordsTextArea);
                        } catch (NullPointerException a) {
                            System.out.println(a + "Please select something");
                        }

                    });


                    // Deletes a keyword from a document
                    deleteConnectionToDocumentBtn.setOnAction(k -> {
                        try {

                            int id = documentsTable.getSelectionModel().getSelectedItem().getID();
                            int keyword = keywordTable.getSelectionModel().getSelectedItem().getKeywordID();
                            controller.deleteSingleKeyword(id, keyword);
                            updateKeywordTextAreaDisplay(deleteDocumentBtn, keywordsTextArea);
                            System.out.println("Keyword connected to the document has been deleted");
                        } catch (NullPointerException e) {
                            System.out.println(" delete connection to Document " + e);
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e + "error while deleting the connection between the document and the keyword");
                }
            });


            // reference stage when the button is clicked
            openReferenceWindowBtn.setOnAction(referenceWindowOpenEvent -> {
                openReferenceWindowBtn.setDisable(true);
                VBox referenceWindowVBox = new VBox();

                TabPane referenceTabPane = new TabPane();
                HBox urlHBox = new HBox();
                urlHBox.setPadding(new Insets(5));
                Label urlLabel = new Label("URL: ");
                Pane spacePaneHbox = new Pane();
                TextField urlTextfield = new TextField();
                Button urlConfirmBtn = new Button("OK");
                HBox.setHgrow(urlTextfield, Priority.ALWAYS);
                urlHBox.setSpacing(5);
                urlHBox.getChildren().addAll(urlLabel, urlTextfield, spacePaneHbox, urlConfirmBtn);

                Tab urlTab = new Tab();
                urlTab.setText("URL");
                urlTab.setClosable(false);
                urlTab.setContent(urlHBox);

                HBox filePathHBox = new HBox();
                filePathHBox.setPadding(new Insets(5));
                Label filePathLabel = new Label("File Path: ");
                TextField filePathTextfield = new TextField();
                filePathTextfield.setEditable(false);
                Button filePathOpenBtn = new Button("Select");
                filePathHBox.setSpacing(5);
                filePathHBox.getChildren().addAll(filePathLabel, filePathTextfield, filePathOpenBtn);
                HBox.setHgrow(filePathTextfield, Priority.ALWAYS);
                Tab filePathTab = new Tab();
                filePathTab.setText("File");
                filePathTab.setClosable(false);
                filePathTab.setContent(filePathHBox);

                GridPane archiveGrid = new GridPane();
                archiveGrid.setPadding(new Insets(5,5,5,5));
                VBox archiveVBox = new VBox();

                Label archiveShedLabel = new Label("Shed: ");
                TextField shedTextField = new TextField();


                GridPane.setConstraints(archiveShedLabel, 0,0);
                GridPane.setConstraints(shedTextField, 1,0);
                archiveGrid.getChildren().addAll(archiveShedLabel, shedTextField);


                Label archiveRackLabel = new Label("Rack: ");
                TextField rackTextField = new TextField();


                GridPane.setConstraints(archiveRackLabel, 0,1);
                GridPane.setConstraints(rackTextField,1,1);
                archiveGrid.getChildren().addAll(archiveRackLabel, rackTextField);


                Label archiveFolderLabel = new Label("Folder: ");
                TextField folderTextField = new TextField();
                GridPane.setConstraints(archiveFolderLabel, 0, 2);
                GridPane.setConstraints(folderTextField, 1,2);
                archiveGrid.getChildren().addAll(archiveFolderLabel, folderTextField);

                Button archiveConfirmBtn = new Button("OK");
                archiveConfirmBtn.setDisable(true);
                GridPane.setConstraints(archiveConfirmBtn, 0,4);
                archiveGrid.getChildren().addAll(archiveConfirmBtn);
                archiveVBox.getChildren().addAll(archiveGrid);

                GridPane.setHgrow(rackTextField, Priority.ALWAYS);

                Tab archiveTab = new Tab();
                archiveTab.setText("Archive");
                archiveTab.setClosable(false);
                archiveTab.setContent(archiveVBox);

                referenceTabPane.getTabs().addAll(urlTab, filePathTab, archiveTab);


                referenceWindowVBox.getChildren().addAll(referenceTabPane);
                Stage referenceStage = new Stage();
                referenceStage.setTitle("References");
                referenceStage.setScene(new Scene(referenceWindowVBox, 300, 175));
                referenceStage.setMinWidth(300);
                referenceStage.setMinHeight(175);
                referenceStage.setMaxHeight(175);
                referenceStage.initModality(Modality.WINDOW_MODAL);
                referenceStage.initOwner(primaryStage);
                referenceStage.show();

                referenceStage.setOnCloseRequest(closeRequest -> {
                    openReferenceWindowBtn.setDisable(false);
                });


                RefFilePath refFilePath = controller.selectFilePath(docID);
                if (null != refFilePath) {
                    System.out.println(refFilePath.filePathStr + " " + refFilePath.getFileNameStr());

                }

                shedTextField.textProperty().addListener(changeEvent ->{
                   updateArchiveBtn(shedTextField, rackTextField, folderTextField, archiveConfirmBtn);
                });

                rackTextField.textProperty().addListener(eventChange -> {
                    updateArchiveBtn(shedTextField, rackTextField, folderTextField, archiveConfirmBtn);
                });

                folderTextField.textProperty().addListener(event ->{
                    updateArchiveBtn(shedTextField, rackTextField, folderTextField, archiveConfirmBtn);
                });

                urlConfirmBtn.setDisable(true);
                urlTextfield.textProperty().addListener(userEvent -> {
                    if (!urlTextfield.getText().isEmpty()){
                        urlConfirmBtn.setDisable(false);
                    }else{
                        urlConfirmBtn.setDisable(true);
                    }
                });


                filePathOpenBtn.setOnAction(openEvent -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select a file");
                    File chosenFile = fileChooser.showOpenDialog(referenceStage);
                    if (null != chosenFile) {
                        controller.getAddIdToReferenceTable(docID);
                        String chosenFilePath = chosenFile.getPath();
                        String chosenFileName = chosenFile.getName();
                        try {
                            controller.getAddFileToPathTable(chosenFilePath, chosenFileName);
                            refereceTextArea.setText(controller.selectReference(controller.selectReferenceID(docID)));
                            referenceStage.close();
                        } catch (ErrorExceptionThrow error) {
                            Alert errorDialog = new Alert(Alert.AlertType.WARNING);
                            errorDialog.setTitle("File already Exists error");
                            errorDialog.setHeaderText(null);
                            errorDialog.setContentText("The selected Path already exists! " + "\n" + "Please select another File!");
                            errorDialog.showAndWait();
                        }


                    }

                });

                urlConfirmBtn.setOnAction(confirmEvent -> {
                    controller.getAddIdToReferenceTable(docID);
                    String urlText = urlTextfield.getText();
                    try {
                        controller.getAddUrlToUrlTable(urlText);
                        refereceTextArea.setText(controller.selectReference(controller.selectReferenceID(docID)));
                        referenceStage.close();
                    } catch (ErrorExceptionThrow error) {
                        Alert errorAlert = new Alert(Alert.AlertType.WARNING);
                        errorAlert.setTitle("Url already exists error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("This url already exists! " + "\n" + "Please select another url!");
                        errorAlert.showAndWait();
                    }


                });

                archiveConfirmBtn.setOnAction(confirmEvent -> {
                    controller.getAddIdToReferenceTable(docID);
                    String shed = shedTextField.getText();
                    String rack = rackTextField.getText();
                    String folder = folderTextField.getText();
                    controller.addToArchive(shed, rack, folder);
                    refereceTextArea.setText(controller.selectReference(controller.selectReferenceID(docID)));
                    referenceStage.close();

                });

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
            addDocumentBtn.setOnAction(c -> {
                Integer intValue;
                try {
                    intValue = Integer.parseInt(idTextField.getText());
                    controller.addDocument(new Document(intValue, titleTextField.getText(), authorTextField.getText()));
                    controller.startDocumentTableQuery();
                } catch (NumberFormatException e) {
                    System.out.println("That is not a Number you twat!" + e);
                } catch (ErrorExceptionThrow errorEvent) {
                    Alert errorAlter = new Alert(Alert.AlertType.INFORMATION);
                    errorAlter.setHeaderText(null);
                    errorAlter.setTitle("Unique ID error");
                    errorAlter.setContentText("This ID is already used! " + "\n" + "Please select another one.");
                    errorAlter.showAndWait();
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
            deleteDocumentBtn.setOnAction(c -> {
                try {
                    Document document = documentsTable.getSelectionModel().getSelectedItem();
                    if (document == null) {
                        System.out.println("No document Selected! ");
                    } else {
                        controller.deletedConnectedKeywordFromDocument(document.getID());
                        controller.deleteDocument(document.getID());

                        keywordsTextArea.clear();
                        controller.startDocumentTableQuery();
                    }
                } catch (NullPointerException b) {
                    System.out.println(b + "this is b");
                }
            });


            refereceTextArea.textProperty().addListener(changeEvent -> {
                if (!refereceTextArea.getText().isEmpty()) {
                    openReferenceWindowBtn.setDisable(true);
                } else {
                    openReferenceWindowBtn.setDisable(false);
                }
            });

            // Adds the keyword to the text area designated for the keyword display
            documentsTable.getSelectionModel().selectedItemProperty().addListener((Observable observable) -> {
                if (!documentsTable.getSelectionModel().isEmpty()) {
                    Document documentInformationStr = documentsTable.getSelectionModel().getSelectedItem();
                    idTextField.setText(Integer.toString(documentInformationStr.getID()));
                    titleTextField.setText(documentInformationStr.getTitle());
                    authorTextField.setText(documentInformationStr.getAuthor());
                    this.docID = documentsTable.getSelectionModel().getSelectedItem().getID();
                    openKeywordsWindowBtn.setDisable(false);
                    openReferenceWindowBtn.setDisable(false);
                    refereceTextArea.setText(controller.selectReference(controller.selectReferenceID(docID)));
                } else {
                    refereceTextArea.clear();
                    openKeywordsWindowBtn.setDisable(true);
                    openReferenceWindowBtn.setDisable(true);

                }

                //TODO do the same for the Reference text Area to display
                updateKeywordTextAreaDisplay(deleteDocumentBtn, keywordsTextArea);

            });
            controller.startKeywordTableQuery();
        }


        /**
         * Method to change the add button for the documents
         * if every field is filled the "add button" will be usable
         *
         * @param id     takes the input of the text field for the id
         * @param title  takes the input of the text field for the titel
         * @param author takes the input of the text field for the author
         * @param add    gets the add button
         */
        private void updateAddBtn(TextField id, TextField title, TextField author, Button add) {
            if (!id.getText().isEmpty() && !title.getText().isEmpty() && !author.getText().isEmpty()) {
                add.setDisable(false);
            } else {
                add.setDisable(true);
            }
        }

        /**
         * This method updates the buttons for connecting a keyword to a document and for deleting a document
         *
         * @param keywordTable                  is the tableView for the Keywords. Is used to check if something is selected
         * @param documentsTable                is the tableView for the Documents. Is used to check if something is selected
         * @param addKeywordToDocumentsBtn      is the add button that has to be enabled when both tableViews have a selected property
         * @param deleteConnectionToDocumentBtn is the delete button that has to be enabled when both tableViews have selected property
         */
        private void updateKeywordAddDeleteBtn(TableView keywordTable, TableView documentsTable, Button addKeywordToDocumentsBtn, Button deleteConnectionToDocumentBtn) {
            if (!keywordTable.getSelectionModel().isEmpty() && !documentsTable.getSelectionModel().isEmpty()) {
                addKeywordToDocumentsBtn.setDisable(false);
                deleteConnectionToDocumentBtn.setDisable(false);
            } else {
                addKeywordToDocumentsBtn.setDisable(true);
                deleteConnectionToDocumentBtn.setDisable(true);
            }
        }

        private void updateKeywordTextAreaDisplay(Button deleteDocumentBtn, TextArea keywordsTextArea) {
            if (!documentsTable.getSelectionModel().isEmpty()) {
                controller.startKeywordIDQuery(documentsTable.getSelectionModel().getSelectedItem().getID());
                deleteDocumentBtn.setDisable(false);
                List<Integer> keywordIDArray = controller.getKeywordIdArray();
                for (int i = 0; i < keywordIDArray.size(); i++) {
                    System.out.println(keywordIDArray.get(i));
                    controller.startKeywordDisplayQuery(keywordIDArray.get(i));
                }
                controller.getKeywordIdArray().clear();
                ArrayList<String> keywordArrayList = controller.getDisplayKeywordsArray();

                keywordsTextArea.clear();
                for (int i = 0; i < keywordArrayList.size(); i++) {
                    keywordsTextArea.appendText(keywordArrayList.get(i) + " ");
                }
                keywordArrayList.clear();
            } else {
                deleteDocumentBtn.setDisable(true);
            }
        }

        private void updateArchiveBtn(TextField shedField, TextField rackField, TextField folderField, Button archiveOKBtn){
            if (!shedField.getText().isEmpty() && !rackField.getText().isEmpty() && !folderField.getText().isEmpty()){
                archiveOKBtn.setDisable(false);
            }else{
                archiveOKBtn.setDisable(true);
            }
        }

    public static void main(String[] args) {
        launch(args);
    }

}
