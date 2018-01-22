package sample;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookData {
    private ObservableList<Book> bookObservableList = FXCollections.observableArrayList();
    private String url;
    private String filename = null;


    public void setFilename(String filename) {
        this.filename = filename;
        query();

    }



    protected BookData(){
        String driver = "org.sqlite.JDBC";
        try{
            Class.forName(driver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Book> getBookObservableList() {
        return this.bookObservableList;
    }

    /*
    private void createDataBase(){

        try{Connection connection = DriverManager.getConnection(url); {
                if (connection != null){
                    DatabaseMetaData metaData = connection.getMetaData();
                    System.out.println("The driver name is " + metaData.getDriverName());
                    System.out.println("A new database has been created");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void createBooks(){
        String sqlQuery = "CREATE TABLE IF NOT EXISTS Books (id INTEGER, title VARCHAR(255), author VARCHAR(255)) ";
        try{
            PreparedStatement preparedStatement;
            try(Connection connection = DriverManager.getConnection(url)){
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.executeQuery();
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
    }*/


    private void query(){
        synchronized (this.bookObservableList){
            this.bookObservableList.clear();
            String jdbc = "jdbc:sqlite";
            this.url =jdbc + ":" + filename;

            String sqlQuery =
                    "SELECT ID, title, author " +
                    "FROM Books";
            try{
                PreparedStatement preparedStatement;
                try(Connection connection = DriverManager.getConnection(url)){
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    ResultSet cursor = preparedStatement.executeQuery();
                    while (cursor.next()){
                        int id = cursor.getInt(1);
                        String title = cursor.getString(2);
                        String author = cursor.getString(3);
                        System.out.println(id + " " + title + " " + author );
                        Book books = new Book(id, title, author);
                        bookObservableList.addAll(books);


                    }
                }
            }catch (SQLException e){
                e.printStackTrace();

            }
        }
    }
}
