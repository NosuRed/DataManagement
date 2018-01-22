package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

    private SimpleIntegerProperty bookID;
    private SimpleStringProperty bookTitle;
    private SimpleStringProperty bookAuthor;



    Book(int id, String bookTitle, String bookAuthor){
        this.bookID = new SimpleIntegerProperty(id);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.bookAuthor = new SimpleStringProperty(bookAuthor);

    }

    /**
     * Getter method to get the book id
     * @return book ID
     */
    public int getID() {
        return bookID.get();
    }

    /**
     * Getter method to get the books authors name
     * @return the authors name
     */
    public String getAuthor() {
        return bookAuthor.get();
    }

    /**
     * Method to get the book title
     * @return the book title
     */
    public String getTitle() {
        return bookTitle.get();
    }
}
