package DocumentManagementProject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DocumentData {
    private final ObservableList<Document> documentObservableList = FXCollections.observableArrayList();
    private final ObservableList<Keyword> keywordObservableList = FXCollections.observableArrayList();

    //Returns an Observable list for the keywords
    public ObservableList<Keyword> getKeywordObservableList() {
        return keywordObservableList;
    }

    //Returns an Observable list for the documents
    public ObservableList<Document> getDocumentObservableList() {
        return this.documentObservableList;
    }

    // creates the string for the creation of the database
    private String jdbc = "jdbc:sqlite";
    private String filename = "documentDatabase.db";
    private String url = jdbc + ":" + filename;


    /**
     * Method that creates the data base if none exists
     * creates the tables as well
     * also provides the driver
     */
    protected DocumentData() {
        String driver = "org.sqlite.JDBC";
        try {
            Class.forName(driver);
            createDocumentTable();
            createKeywordsTable();
            createActiveKeywordsTable();
            createArchiveTable();
            createFilePathTable();
            createUrlTable();
            createReferenceTable();
            } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // method to start the keyword queries, used when the table needs to be updated
    public void startKeywordTableQuery() {
        keywordsTableQuery();
    }

    public void startDocumentTableQuery() {
        documentTableQuery();
    }


    /**
     * deletes every trace of the document and it connections
     * selects the reference id
     * if a reference is connected to a document it will delete it from its own table as well
     * @param documentID is the document id that is used to delete every connection to it
     */
    public void deleteDocument(int documentID) {
        int refID = -1;
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement selectRefID = connection.prepareStatement("SELECT ID FROM ReferenceTable WHERE DocumentID == ? ")){
                selectRefID.setInt(1, documentID);
                ResultSet cursor = selectRefID.executeQuery();
                while (cursor.next()) {
                    refID = cursor.getInt(1);
                }
            }
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ReferenceTable WHERE ID == ?;")){
                 deleteStatement.setInt(1, refID);
                 deleteStatement.executeUpdate();
            }
            try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM ArchiveTable WHERE ID ==?")){
                deleteStatement.setInt(1, refID);
                deleteStatement.executeUpdate();
            }
            try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM UrlTable WHERE ID ==?")){
                deleteStatement.setInt(1, refID);
                deleteStatement.executeUpdate();
            }
            try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM FilePathTable WHERE ID ==?")){
                deleteStatement.setInt(1, refID);
                deleteStatement.executeUpdate();
            }
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM Documents WHERE ID == ?;")) {
                deleteStatement.setInt(1, documentID);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * deletes a single keyword that has been connected to a document
     *
     * @param id      is the keyword that will be deleted
     * @param keyword is the id connected to the keyword
     *                the keyword and the id together are used to make sure that only the right keyword is deleted
     */
    public void deleteSingleKeyword(int id, int keyword) {
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ActiveKeywords WHERE ID == ? and Keyword  ==  ?;"
            )) {
                deleteStatement.setInt(1, id);
                //deleteStatement.setString(2, keyword);
                deleteStatement.setInt(2, keyword);
                deleteStatement.execute();
            } catch (SQLException e) {
                System.out.println("Delete Single Keyword Error" + e);
            }

        } catch (SQLException p) {
            System.out.println("Delete Single Keyword Error " + p);
        }
    }

    /**
     * Accesses the ActiveKeywords table
     * When a document is deleted every Keyword that is connected to it needs to be deleted as well
     * This delete statement deletes every entry with the documents id and the keywords connected with it
     *
     * @param id the document id, is used to delete every entry in the ActiveKeywords table
     */
    public void deleteKeywordsFromDeletedDocument(int id) {
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ActiveKeywords WHERE ID == ?;"
            )) {
                deleteStatement.setInt(1, id);
                deleteStatement.execute();
            } catch (SQLException e) {
                System.out.println(e + "error in deleteKeyWord");
            }

        } catch (SQLException c) {
            System.out.println(c);
        }
    }

    /**
     * SQL query to delete a keyword from the keywords table
     *
     * @param keywordID is the keyword that will be deleted from the Keywords table
     */
    public void deleteKeyWord(int keywordID) {
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM Keywords WHERE ID == ?;"
            )) {
                deleteStatement.setInt(1, keywordID);
                deleteStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e + "error in deleteKeyWord");
            }
        } catch (SQLException c) {
            System.out.println(c);
        }
    }


    public void deleteKeywordsFromActiveKeywordTable(int keywordID) {
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ActiveKeywords WHERE keyword == ?;"
            )) {
                deleteStatement.setInt(1, keywordID);
                deleteStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e + "error in deleteKeyWord");
                System.out.println();
            }
        } catch (SQLException c) {
            System.out.println(c);
        }
    }


    /**
     * SQL query to add a keyword to the keywords table
     * @param keyword is a Keyword object that contains the keyword
     *                the keyword will be added into the Keywords table
     */
    public void addKeyword(String keyword) throws ErrorExceptionThrow{
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement insertIntoStatement = connection.prepareStatement(
                    "INSERT INTO Keywords(Keyword) VALUES (?)")) {
                insertIntoStatement.setString(1, keyword);
                insertIntoStatement.execute();
            }
        } catch (SQLException e) {
            throw new ErrorExceptionThrow();

        }
    }

    /**
     * SQL query to add a document to the documents table
     * these are the user inputs taken in the view
     *
     * @param document a new document was created, this document contains the ID, the title and the author
     */
    public void addDocument(Document document) throws ErrorExceptionThrow {
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO Documents(ID, title, author) VALUES (?, ?, ?)")) {
                insertIntostatement.setInt(1, document.getID());
                insertIntostatement.setString(2, document.getTitle());
                insertIntostatement.setString(3, document.getAuthor());
                insertIntostatement.execute();
            }

        } catch (SQLException e) {
            throw new ErrorExceptionThrow();
        }
    }

    /**
     * this method adds the document ID and the Keyword ID into the ActiveKeywords table
     * if the entry already exists it does not add the keyword again
     * @param documentId is the document ID that will be stored in this table
     * @param keywordID  is the keyword ID that will be stored in this table
     */
    public void addKeywordsToDocument(int documentId, int keywordID) {
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement insertIntoStatement = connection.prepareStatement(
                    "INSERT INTO ActiveKeywords(ID, Keyword) SELECT ?, ? " +
                            "WHERE NOT EXISTS (SELECT ID, Keyword " +
                            "FROM ActiveKeywords" +
                            " WHERE ID = ? AND Keyword = ?)")) {
                insertIntoStatement.setInt(1, documentId);
                insertIntoStatement.setInt(2, keywordID);
                insertIntoStatement.setInt(3, documentId);
                insertIntoStatement.setInt(4, keywordID);
                insertIntoStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * starts the query for the keywords table
     * selects the keyword and the ID from the table Keywords
     * creates a new keyword from the class Keyword
     * both values are given to the new keyword
     * the keywords are then added to an observable list
     */
    private void keywordsTableQuery() {
        synchronized (this.keywordObservableList) {
            this.keywordObservableList.clear();
            String sqlQuery = "SELECT Keyword, ID FROM Keywords";
            try {
                PreparedStatement preparedStatement;
                try (Connection connection = DriverManager.getConnection(url)) {
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet cursor = preparedStatement.executeQuery();
                    while (cursor.next()) {
                        String keywordName = cursor.getString(1);
                        int id = cursor.getInt(2);
                        Keyword keywords = new Keyword(keywordName, id);
                        keywordObservableList.addAll(keywords);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // The keywordIDArray contains the keywordIDs, these will later be given to an other query
    private List<Integer> keywordIDArray = new ArrayList<>();

    protected List<Integer> getKeywordIDArray() {
        return keywordIDArray;
    }

    public void startKeywordConnectionQuery(int id) {
        getKeywordsIdConnectionQuery(id);
    }


    /**
     * this method selects the id and the keyword from the ActiveKeywords table
     *
     * @param id is the selected Document id
     *        the keywordID is added to the keywordIDArray
     */
    private void getKeywordsIdConnectionQuery(int id) {
        String sqlQuery = "SELECT ID, keyword FROM ActiveKeywords WHERE ID == ?";
        try {
            PreparedStatement preparedStatement;
            try (Connection connection = DriverManager.getConnection(url)) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, id);
                ResultSet cursor = preparedStatement.executeQuery();
                while (cursor.next()) {
                    int ID = cursor.getInt(1);
                    int keywordID = cursor.getInt(2);
                    keywordIDArray.add(keywordID);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Starts the query to get the Keywords from the Keyword table
     * @param keywordID is the ID from the ActiveKeyword table
     */
    public void startKeywordDisplayQuery(int keywordID) {
        startKeywordsDisplayQuery(keywordID);
    }


    protected ArrayList<String> displayKeywordsArray = new ArrayList<>();

    /**
     * this query accesses the Keywords table and adds the keywords to an ArrayList, that way they can be accessed in the View
     *
     * @param keywordID the keywordID is the id from the keywordIDArray, that contains the IDs of the keywords connected to a document
     */
    private void startKeywordsDisplayQuery(int keywordID) {
        String sqlQuery = "SELECT ID, Keyword FROM Keywords WHERE ID == ?";
        try {
            PreparedStatement preparedStatement;
            try (Connection connection = DriverManager.getConnection(url)) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, keywordID);
                ResultSet cursor = preparedStatement.executeQuery();
                while (cursor.next()) {
                    String keywordToDisplay = cursor.getString(2);
                    displayKeywordsArray.add(keywordToDisplay);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * this method starts the query for the Document table
     * it selects the id, the title and the author from the Documents table
     * these values are then given to the Document class
     * a new document is created
     * the document(s) are then added to an observable list
     */
    private void documentTableQuery() {
        synchronized (this.documentObservableList) {
            this.documentObservableList.clear();
            String sqlQuery =
                    "SELECT ID, title, author " +
                            "FROM Documents";
            try {
                PreparedStatement preparedStatement;
                try (Connection connection = DriverManager.getConnection(url)) {
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet cursor = preparedStatement.executeQuery();
                    while (cursor.next()) {
                        int id = cursor.getInt(1);
                        String title = cursor.getString(2);
                        String author = cursor.getString(3);
                        Document documents = new Document(id, title, author);
                        documentObservableList.addAll(documents);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * creates the document table if it does not exist yet
     * creates columns for the document ID, the title and the author
     */
    private void createDocumentTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String query =
                    "CREATE TABLE IF NOT EXISTS Documents (ID INTEGER UNIQUE , title TEXT, author TEXT)";
            Statement statement = connection.createStatement();
            statement.execute(query);

        } catch (SQLException z) {
            z.printStackTrace();
        }
    }

    /**
     * creates the Keyword table, where every Keyword will be stored
     * creates  a column for the automatically generated ID and a column for the Keyword
     */
    private void createKeywordsTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String query =
                    "CREATE TABLE IF NOT EXISTS Keywords (ID INTEGER PRIMARY KEY AUTOINCREMENT, Keyword TEXT  UNIQUE NOT NULL )";
            Statement statement = connection.createStatement();
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the ActiveKeywords table
     * in this table the Document ID and the Keyword ID will be stored
     */
    private void createActiveKeywordsTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String query =
                    "CREATE TABLE IF NOT EXISTS ActiveKeywords (ID INTEGER ," +
                            " keyword INTEGER)";
            Statement statement = connection.createStatement();
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * creates the UrlTable
     * table will contain the id and the URL
     */
    private void createUrlTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String query =
                    "CREATE TABLE IF NOT EXISTS UrlTable(ID INTEGER PRIMARY KEY" +
                            ",URL TEXT UNIQUE , FOREIGN KEY(ID) REFERENCES ReferenceTable (ID))";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * creates the FilePathTable
     * table will contain the ID, the file path and the file name
     */
    private void createFilePathTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String query =
                    "CREATE TABLE IF NOT EXISTS FilePathTable (ID INTEGER PRIMARY KEY" +
                            " ,FilePath TEXT UNIQUE , FileName TEXT, FOREIGN KEY(ID) REFERENCES ReferenceTable (ID))";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the ArchiveTable
     * table will contain the ID, the shed, the rack and the folder
     */
    private void createArchiveTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String query =
                    "CREATE TABLE IF NOT EXISTS ArchiveTable (ID INTEGER PRIMARY KEY" +
                            ",Shed TEXT, Rack TEXT, Folder TEXT,FOREIGN KEY(ID) REFERENCES ReferenceTable (ID))";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the ReferencesTable
     * ID will be automatically generated and then distributed to the references tables, UrlTable, FilePathTable and ArchiveTable as IDs
     * this table is to ensure that each id will be unique in the three tables
     */
    private void createReferenceTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            String query =
                    "CREATE TABLE IF NOT EXISTS ReferenceTable (ID INTEGER PRIMARY KEY AUTOINCREMENT, DocumentID INTEGER UNIQUE)";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * adds an id to the ReferencesTable that will be unique
     * @param docID is the document that will be connected with the unique id
     */
    public void addIdToReferenceTable(int docID){
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO ReferenceTable VALUES (NULL, ?)")) {
                insertIntostatement.setInt(1,docID);
                insertIntostatement.execute();
                System.out.println();
            }

        } catch (SQLException e) {
           e.printStackTrace();
        }
    }



    /**
     * adds the a unique ID from the ReferencesTable to the ID column in ht FilePathTable
     * @param filePath is the file path of the selected file by the user
     * @param fileName is the name of the file selected by the user
     */
    public void addFileToFilePathTable(String filePath, String fileName) throws ErrorExceptionThrow{
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO FilePathTable SELECT MAX(ID), ?, ? FROM ReferenceTable")) {
                insertIntostatement.setString(1, filePath);
                insertIntostatement.setString(2, fileName);
                insertIntostatement.execute();
            }
        } catch (SQLException e) {
            deleteReferenceIfNoConnection();
            throw new ErrorExceptionThrow();
        }

    }


    /**
     *  removes the latest ID from the ReferencesTable
     */
    private void deleteReferenceIfNoConnection(){
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ReferenceTable WHERE ID = (SELECT MAX(ID) FROM ReferenceTable)"
            )) {
                deleteStatement.executeUpdate();
                System.out.println();
            } catch (SQLException e) {
                System.out.println(e + "error in delete Reference if no connection exists");
            }
        } catch (SQLException c) {
            System.out.println(c);
        }
    }

    /**
     * selects the filePath and the fileName from the FilePathTable and creates a RefFilePath object
     * @param docId is the selected document ID
     * @return the RefFilePath object is returned
     */
    public RefFilePath selectFilePathTable(int docId){
        String sqlQuery = "SELECT ID, FilePath, FileName FROM FilePathTable WHERE ID == ?";
        try {
            PreparedStatement preparedStatement;
            try (Connection connection = DriverManager.getConnection(url)) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, docId);
                ResultSet cursor = preparedStatement.executeQuery();
                while (cursor.next()) {
                    int refId = cursor.getInt(1);
                    String filePath = cursor.getString(2);
                    String fileName = cursor.getString(3);
                    return new RefFilePath(refId, filePath, fileName);
                }
            }
        } catch (SQLException e) {
            System.out.println("This is an error in the selectFilePathTable method " + e);
        }
        return null;
    }



    /**
     * adds an url to the UrlTable
     * @param urlInsert is the url that is added to the Table
     */
    public void addUrlToUrlTable(String urlInsert) throws ErrorExceptionThrow{
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO UrlTable SELECT MAX(ID), ? FROM ReferenceTable")) {
                insertIntostatement.setString(1, urlInsert);
                insertIntostatement.execute();
            }

        } catch (SQLException e) {
            deleteReferenceIfNoConnection();
            throw new ErrorExceptionThrow();
        }

    }


    /**
     * selects the ref id and the url from the UrlTable
     * creates the new RefUrl Table with the ref id and the url
     * @param docId is the document ID that is selected by the user
     * @return
     */
    public RefURL selectUrlTable(int docId){
        String sqlQuery = "SELECT ID, URL FROM UrlTable WHERE ID == ?";
        try {
            PreparedStatement preparedStatement;
            try (Connection connection = DriverManager.getConnection(url)) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, docId);
                ResultSet cursor = preparedStatement.executeQuery();
                while (cursor.next()) {
                    int refId = cursor.getInt(1);
                    String urlStr = cursor.getString(2);
                    return new RefURL(refId, urlStr);
                }
            }
        } catch (SQLException e) {
            System.out.println("This is an error in the selectFilePathTable method " + e);
        }
        return null;
    }


    /**
     * adds archive information into the ArchiveTable
     * @param shed is where the folder is being stored with other files
     * @param rack is where the folder is placed
     * @param folderName is the name of the folder
     */
    public void addToArchive(String shed, String rack, String folderName){
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO ArchiveTable SELECT MAX(ID), ?, ?, ? FROM ReferenceTable")) {
                insertIntostatement.setString(1, shed);
                insertIntostatement.setString(2, rack);
                insertIntostatement.setString(3, folderName);
                insertIntostatement.execute();
            }
        } catch (SQLException e) {
            System.out.println("Reference is deleted because the insert into failed");
            e.printStackTrace();
            deleteReferenceIfNoConnection();
        }

    }


    /**
     * selects the shed, the rack and the folder from a selected document
     * @param docId is the selected document
     * @return a RefArchive object
     */
    public RefArchive selectArchive(int docId){
        String sqlQuery = "SELECT ID, Shed, Rack, Folder  FROM ArchiveTable WHERE ID == ?";
        try {
            PreparedStatement preparedStatement;
            try (Connection connection = DriverManager.getConnection(url)) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, docId);
                ResultSet cursor = preparedStatement.executeQuery();
                while (cursor.next()) {
                    int refId = cursor.getInt(1);
                    String shed = cursor.getString(2);
                    String rack = cursor.getString(3);
                    String folder = cursor.getString(4);
                    return new RefArchive(refId, shed, rack, folder);
                }
            }
        } catch (SQLException e) {
            System.out.println("This is an error in the selectFilePathTable method " + e);
        }
        return null;
    }


    /**
     * this select query, gets the reference id and then selects the right reference object and returns it
     * @param refID is the id of the reference
     * @return the selected reference
     */
    public String selectReference(int refID){
        String result = "";
        if (selectArchive(refID) != null){
            RefArchive archive = selectArchive(refID);
            result = " Shed: " + archive.getArchShed() + " \n Rack: " + archive.getArchRack() + " \n Folder: " + archive.getArchFolder();
        }else if (selectUrlTable(refID) != null){
            RefURL url = selectUrlTable(refID);
            result = url.getUrlStr();
        }else if (selectFilePathTable(refID) != null){
            RefFilePath filePath = selectFilePathTable(refID);
            result = filePath.getFilePathStr();
        }
        return result;
    }


    /**
     * selects the the reference id from the ReferenceTable from the selected document
     * @param docID is the selected
     * @return returns the reference id
     */
    public int selectReferenceID(int docID){
        try (Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement selectRefID = connection.prepareStatement("SELECT ID FROM ReferenceTable WHERE DocumentID == ? ")){
                selectRefID.setInt(1, docID);
                ResultSet cursor = selectRefID.executeQuery();
                while (cursor.next()) {
                    return cursor.getInt(1);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException z){
            z.printStackTrace();
        } return -1;
    }


}






