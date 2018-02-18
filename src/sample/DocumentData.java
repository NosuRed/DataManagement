package sample;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DocumentData {
    private ObservableList<Document> documentObservableList = FXCollections.observableArrayList();
    private ObservableList<Keyword> keywordObservableList = FXCollections.observableArrayList();
    //Returns an Observable list for the keywords
    public ObservableList<Keyword> getKeywordObservableList() {
        return keywordObservableList;
    }
    //Returns an Observable list for the documents
    public ObservableList<Document> getDocumentObservableList() {
        return this.documentObservableList;
    }

    private String jdbc = "jdbc:sqlite";
    private String filename = "documentDatabase.db";
    private String url = jdbc + ":" +filename;




    /**
     * Method that creates the data base if none exists
     * also provides the driver
     */
    protected DocumentData(){
        String driver = "org.sqlite.JDBC";
        try{
            Class.forName(driver);
            createDocumentTable();
            createKeywordsTable();
            createKeywordToDocumentsTable();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    // method to start the document and keyword queries, used when the tables need to be updated etc.
    public void startQuery(){
        documentTableQuery();
        keywordsTableQuery();
    }



    //SQL query to delete a document from the documents table
    public void deleteDocument(int document){
        try (Connection connection = DriverManager.getConnection(url)){
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM Documents WHERE id == ?;"
            )){
                deleteStatement.setInt(1, document);
                deleteStatement.executeUpdate();
                }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * deletes a single keyword that has been connected to a document
     * @param id is the keyword that will be deleted
     * @param keyword is the id connected to the keyword
     *  the keyword and the id together are used to make sure that only the right keyword is deleted
     */
    public void deleteSingleKeyword(int id, int keyword){
        try(Connection connection = DriverManager.getConnection(url)) {
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ActiveKeywords WHERE ID == ? and Keyword  ==  ?;"
            )){
                deleteStatement.setInt(1, id);
                //deleteStatement.setString(2, keyword);
                deleteStatement.setInt(2, keyword);
                deleteStatement.execute();
            }catch (SQLException e){
                System.out.println("Delete Single Keyword Error" +  e);
            }

        }catch (SQLException p ){
            System.out.println("Delete Single Keyword Error " + p);
        }
    }

    /**
     * Accesses the ActiveKeywords table
     * When a document is deleted every Keyword that is connected to it needs to be deleted as well
     * This delete statement deletes every entry with the documents id and the keywords connected with it
     * @param id the document id, is used to delete every entry in the ActiveKeywords table
     */
    public void deleteKeywordsFromDeletedDocument(int id){
        try (Connection connection = DriverManager.getConnection(url)){
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ActiveKeywords WHERE ID == ?;"
            )){
                deleteStatement.setInt(1, id);
                deleteStatement.execute();
            }catch (SQLException e){
                System.out.println(e + "error in deleteKeyWord");
            }

        }catch (SQLException c){
            System.out.println(c);
        }
    }

    /**
     * SQL query to delete a keyword from the keywords table
     * @param keyword is the keyword that will be deleted from the Keywords table
     */
    public void deleteKeyWord(int keyword){
        try (Connection connection = DriverManager.getConnection(url)){
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM Keywords WHERE ID == ?;"
            )){
                deleteStatement.setInt(1,keyword);
                 deleteStatement.executeUpdate();
            }catch (SQLException e){
                System.out.println(e + "error in deleteKeyWord");
            }
        }catch (SQLException c){
            System.out.println(c);
        }
    }


    public void deleteKeywordsFromActiveKeywordTable(int keyword){
        try (Connection connection = DriverManager.getConnection(url)){
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM ActiveKeywords WHERE keyword == ?;"
            )){
                deleteStatement.setInt(1,keyword);
                deleteStatement.executeUpdate();
            }catch (SQLException e){
                System.out.println(e + "error in deleteKeyWord");
                System.out.println();
            }
        }catch (SQLException c){
            System.out.println(c);
        }
    }



    /**
     * SQL query to add a keyword to the keywords table
     * a keyword from the Class Keyword was created
     * @param keyword is a Keyword object that contains the keyword
     *                the keyword will be added into the Keywords table
     */
    public void addKeyword(Keyword keyword){
        System.out.println(keyword.getKeyword());
        try(Connection connection = DriverManager.getConnection(url)){
            try (PreparedStatement insertIntoStatement = connection.prepareStatement(
                    "INSERT INTO Keywords(Keyword) VALUES (?)")){
                insertIntoStatement.setString(1, keyword.getKeyword());
                insertIntoStatement.execute();
            }
            }catch (SQLException e){
            System.out.println(e);

            }
        }

    /**
     * SQL query to add a document to the documents table
     * these are the user inputs taken in the view
     * @param document a new document was created, this document contains the ID, the title and the author
     */
    public void addDocument(Document document){
        try ( Connection connection = DriverManager.getConnection(url)){
            try(PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO Documents(ID, title, author) VALUES (?, ?, ?)")) {
                insertIntostatement.setInt(1,document.getID());
                insertIntostatement.setString(2, document.getTitle());
                insertIntostatement.setString(3, document.getAuthor());
                insertIntostatement.execute();
                }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * this method adds the document ID and the Keyword ID into the ActiveKeywords table
     * @param documentId is the document ID that will be stored in this table
     * @param keywordID is the keyword ID that will be stored in this table
     */
    public void addKeywordsToDocument(int documentId, int keywordID){
        try ( Connection connection = DriverManager.getConnection(url)){
            try(PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO ActiveKeywords(ID, Keyword) VALUES (?, ?)")) {
                insertIntostatement.setInt(1, documentId);
                insertIntostatement.setInt(2, keywordID);
                insertIntostatement.execute();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * starts the query for the keywords table
     * selects the keyword and the ID from the table Keywords
     * creates a new keyword from the class Keyword
     * both values are given to the new keyword
     * the keywords are then added to an observable list
     *
     */
    private void keywordsTableQuery(){
        synchronized (this.keywordObservableList) {
            this.keywordObservableList.clear();
            this.url = jdbc + ":" + filename;
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
                System.out.println("NANIII " + e);
            }
        }
    }



    // The keywordIDArray contains the keywordIDs, these will later be given to an other query
    private List<Integer> keywordIDArray = new ArrayList<>();
    protected List<Integer> getkeywordIDArray(){
        return keywordIDArray;
    }

    public void startKeywordReferenceQuery(int id){
        getKeywordsIdReferenceQuery(id);
    }


    /**
     * this method selects the id and the keyword from the ActiveKeywords table
     * @param id is the selected Document id
     *  the keywordID is added to the keywordIDArray
     */
    private void getKeywordsIdReferenceQuery(int id){
        this.url = jdbc + ":" + filename;
        String sqlQuery = "SELECT ID, keyword FROM ActiveKeywords WHERE ID ==" + id;
        try {
            PreparedStatement preparedStatement;
            try (Connection connection = DriverManager.getConnection(url)) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet cursor = preparedStatement.executeQuery();
                while (cursor.next()) {
                    int ID = cursor.getInt(1);
                    int keywordID = cursor.getInt(2);
                    System.out.println(ID +" : "+ keywordID);
                    keywordIDArray.add(keywordID);

                }
            }
        } catch (SQLException e) {
            System.out.println("NANIII " + e);
        }
    }


    /**
     * Starts the query to get the Keywords from the Keyword table
     * @param keywordID is the ID from the ActiveKeyword table
     */
    public void startKeywordDisplayQuery(int keywordID){
        startKeywordsDisplayQuery(keywordID);
    }



    protected ArrayList<String> displayKeywordsArray = new ArrayList<>();
    /**
     * this query accesses the Keywords table and adds the keywords to an ArrayList, that way they can be accessed in the View
     * @param keywordID the keywordID is the id from the keywordIDArray, that contains the IDs of the keywords connected to a document
     */
    private void startKeywordsDisplayQuery(int keywordID){
        this.url = jdbc + ":" + filename;
        String sqlQuery = "SELECT ID, Keyword FROM Keywords WHERE ID ==" + keywordID;
        try {
            PreparedStatement preparedStatement;
            try (Connection connection = DriverManager.getConnection(url)) {
                preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet cursor = preparedStatement.executeQuery();
                while (cursor.next()) {
                    String keywordToDisplay = cursor.getString(2);
                    displayKeywordsArray.add(keywordToDisplay);

                }
            }
        } catch (SQLException e) {
            System.out.println("NANIII " + e);
        }
    }


    /**
     * this method starts the query for the Document table
     * it selects the id, the title and the author from the Documents table
     * these values are then given to the Document class
     * a new document is created
     * the document(s) are then added to an observable list
     */
    private void documentTableQuery(){
        synchronized (this.documentObservableList){
            this.documentObservableList.clear();
            this.url = jdbc + ":" + filename;
            String sqlQuery =
                    "SELECT ID, title, author " +
                            "FROM Documents";
            try{
                PreparedStatement preparedStatement;
                try(Connection connection = DriverManager.getConnection(url)){
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet cursor = preparedStatement.executeQuery();
                    while (cursor.next()){
                        int id = cursor.getInt(1);
                        String title = cursor.getString(2);
                        String author = cursor.getString(3);
                        Document documents = new Document(id, title, author);
                        documentObservableList.addAll(documents);
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();

            }
        }
    }

    /**
     * creates the document table if it does not exist yet
     * creates columns for the document ID, the title and the author
     */
    private void createDocumentTable(){
        try (Connection connection = DriverManager.getConnection(url)){
            String query =
                    "CREATE TABLE IF NOT EXISTS Documents (ID INTEGER UNIQUE , title TEXT, author TEXT)";
            Statement statement = connection.createStatement();
            statement.execute(query);

        }catch (SQLException z){
            z.printStackTrace();
        }
    }

    /**
     * creates the Keyword table, where every Keyword will be stored
     * creates  a column for the automatically generated ID and a column for the Keyword
     */
    private void createKeywordsTable(){
        try (Connection connection = DriverManager.getConnection(url)){
            String query =
                    "CREATE TABLE IF NOT EXISTS Keywords (ID INTEGER PRIMARY KEY AUTOINCREMENT, Keyword TEXT  UNIQUE NOT NULL )";
            Statement statement = connection.createStatement();
            statement.execute(query);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * creates the ActiveKeywords table
     * in this table the Document ID and the Keyword ID will be stored
     */
    private void createKeywordToDocumentsTable(){
        try (Connection connection = DriverManager.getConnection(url)){
            String query =
                    "CREATE TABLE IF NOT EXISTS ActiveKeywords (ID INTEGER ," +
                            " keyword INTEGER)";
            Statement statement = connection.createStatement();
            statement.execute(query);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}






