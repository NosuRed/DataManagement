package sample;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DocumentData {
    private ObservableList<Document> documentObservableList = FXCollections.observableArrayList();
    private ObservableList<Keyword> keywordObservableList = FXCollections.observableArrayList();




    private String jdbc = "jdbc:sqlite";
    private String filename = "bookDatabase.db";
    private String url = jdbc + ":" +filename;


    public void setFilename(String filename) {
        this.filename = filename;
        documentTableQuery();



    }

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
    public void startQuery(){
        documentTableQuery();
        keywordsTableQuery();
    }

    //Returns an Observable list for the keywords
    public ObservableList<Keyword> getKeywordObservableList() {
        return keywordObservableList;
    }
    //Returns an Observable list for the documents
    public ObservableList<Document> getDocumentObservableList() {
        return this.documentObservableList;
    }

    //SQL query to delete a document from the documents table
    public int deleteDocument(int document){
        try (Connection connection = DriverManager.getConnection(url)){
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM Documents WHERE id == ?;"
            )){
                deleteStatement.setInt(1, document);
                return deleteStatement.executeUpdate();
                }
        }catch (SQLException e){
            e.printStackTrace();
        }
     return -1;
    }


    /**
     * SQL query to delete a keyword from the keywords table
     * @param keyword
     * @return
     */
    public void deleteKeyWord(String keyword){
        try (Connection connection = DriverManager.getConnection(url)){
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM Keywords WHERE Keyword == ?;"
            )){
                deleteStatement.setString(1, keyword);
                 deleteStatement.executeUpdate();
            }catch (SQLException e){
                System.out.println(e + "error in deleteKeyWord");
            }
        }catch (SQLException c){
            System.out.println(c);
        }
    }

    /**
     * SQL query to add a keyword to the keywords table
     * @param keyword
     * @return
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
      * @param document
     * @return
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
     * initiates the select queries
     */

    //DELETE
    private int keywordID;
    protected int getKeywordID(){
        //System.out.println(keywordID);
        return keywordID;
    }




    /**
     *Starts the query for the keywords table
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
                        this.keywordID = keywords.getKeywordID();
                        keywordObservableList.addAll(keywords);

                    }
                }
            } catch (SQLException e) {
                System.out.println("NANIII " + e);
            }
        }
    }

    /**
     *
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


    public void addKeywordsToDocument(int id, int keywordID){
        try ( Connection connection = DriverManager.getConnection(url)){
            try(PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO ActiveKeywords(ID, Keyword) VALUES (?, ?)")) {
                insertIntostatement.setInt(1, id);
                insertIntostatement.setInt(2, keywordID);
                insertIntostatement.execute();
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
     * creates  a column for the automatically generated ID and a column for the
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




    public void startKeywordReferenceQuery(int id){
        getKeywordsIdReferenceQuery(id);
    }


    private List<Integer> keywordIDArray = new ArrayList<>();
    protected List<Integer> getkeywordIDArray(){
        return keywordIDArray;
    }

    //FIXME change it so the Keyword Table is asked not the ActiveKeywords table, please!
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



    public void startKeywordDisplayQuery(int keywordID){
        startKeywordsDisplayQuery(keywordID);
    }



    protected ArrayList<String> displayKeywordsArray = new ArrayList<>();

    public ArrayList<String> getDisplayKeywordsArray() {
        return displayKeywordsArray;
    }

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
                    System.out.println(keywordToDisplay);

                }
            }
        } catch (SQLException e) {
            System.out.println("NANIII " + e);
        }
    }


}






