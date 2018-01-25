package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Document {

    private SimpleIntegerProperty documentID;
    private SimpleStringProperty documentTitle;
    private SimpleStringProperty documentAuthor;



    Document(int id, String documentTitle, String documentAuthor){
        this.documentID = new SimpleIntegerProperty(id);
        this.documentTitle = new SimpleStringProperty(documentTitle);
        this.documentAuthor = new SimpleStringProperty(documentAuthor);

    }

    /**
     * Getter method to get the book id
     * @return document ID
     */
    public int getID() {
        return documentID.get();
    }

    /**
     * Getter method to get the books authors name
     * @return the authors name
     */
    public String getAuthor() {
        return documentAuthor.get();
    }

    /**
     * Method to get the book title
     * @return the document title
     */
    public String getTitle() {
        return documentTitle.get();
    }
}
