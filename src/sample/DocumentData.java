package sample;
import java.security.Key;
import java.sql.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

public class DocumentData {
    private ObservableList<Document> documentObservableList = FXCollections.observableArrayList();
    private ObservableList<Keyword> keywordObservableList = FXCollections.observableArrayList();



    private String jdbc = "jdbc:sqlite";
    private String filename = null;
    private String url;


    public void setFilename(String filename) {
        this.filename = filename;
        documentTableQuery();


    }

    protected DocumentData(){
        String driver = "org.sqlite.JDBC";
        try{
            Class.forName(driver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
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
                    "DELETE FROM Keywords WHERE Name == ?;"
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
            try (PreparedStatement insertIntoStatement = connection.prepareStatement("INSERT INTO Keywords(Name) VALUES (?)")){
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
    public void startQuery(){
        documentTableQuery();
        keywordsTableQuery();
    }

    /**
     *
     */
    private void keywordsTableQuery(){
        synchronized (this.keywordObservableList) {
            this.keywordObservableList.clear();
            this.url = jdbc + ":" + filename;
            String sqlQuery = "SELECT NAME FROM Keywords";
            try {
                PreparedStatement preparedStatement;
                try (Connection connection = DriverManager.getConnection(url)) {
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet cursor = preparedStatement.executeQuery();
                    while (cursor.next()) {
                        String name = cursor.getString(1);
                        Keyword keywords = new Keyword(name);
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
                        //System.out.println(id + " " + title + " " + author );
                        Document documents = new Document(id, title, author);
                        documentObservableList.addAll(documents);


                    }
                }
            }catch (SQLException e){
                e.printStackTrace();

            }
        }
    }

    public void addKeywordsToDocument(int id, String keywordName){
        try ( Connection connection = DriverManager.getConnection(url)){
            try(PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO ActiveKeywords(ID, Keyword) VALUES (?, ?)")) {
                insertIntostatement.setInt(1, id);
                insertIntostatement.setString(2, keywordName);
                insertIntostatement.execute();

            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }


    //Deletes everything!
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

    //TODO: More delete queries!





    public ArrayList<String> connectionToDocumentQuery(int id){
        ArrayList<String> keywordList = new ArrayList<>();
            this.url = jdbc + ":" + filename;
            String sqlQuery =
                    "SELECT Keyword FROM ActiveKeywords WHERE ID ==" + id ;
            try{
                PreparedStatement preparedStatement;
                try(Connection connection = DriverManager.getConnection(url)){
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet cursor = preparedStatement.executeQuery();

                    while (cursor.next()){
                        String keyword = cursor.getString(1);
                        keywordList.add(keyword);
                        }

                        return keywordList;
                }
            }catch (SQLException e){
                e.printStackTrace();

            } return keywordList;
    }

}



