package sample;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookData {
    private ObservableList<Document> documentObservableList = FXCollections.observableArrayList();
    private ObservableList<Keyword> keywordObservableList = FXCollections.observableArrayList();
    private String jdbc = "jdbc:sqlite";
    private String filename = null;
    private String url;

    public void setFilename(String filename) {
        this.filename = filename;
        documentTableQuery();
        keywordsTableQuery();
    }

    protected BookData(){
        String driver = "org.sqlite.JDBC";
        try{
            Class.forName(driver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Keyword> getKeywordObservableList() {
        return keywordObservableList;
    }

    public ObservableList<Document> getDocumentObservableList() {
        return this.documentObservableList;
    }

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


    public Document addDocument(Document document){
        try ( Connection connection = DriverManager.getConnection(url)){
            try(PreparedStatement insertIntostatement = connection.prepareStatement(
                    "INSERT INTO Documents(ID, title, author) VALUES (?, ?, ?)")) {
                insertIntostatement.setInt(1,document.getID());
                insertIntostatement.setString(2, document.getTitle());
                insertIntostatement.setString(3, document.getAuthor());
                insertIntostatement.execute();

                return document;
                }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }


    public void startQuery(){
        documentTableQuery();
        keywordsTableQuery();
    }


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

    private void documentTableQuery(){
        synchronized (this.documentObservableList){
            this.documentObservableList.clear();
            //String jdbc = "jdbc:sqlite";
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
}
