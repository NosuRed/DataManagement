package sample;

import javafx.collections.ObservableList;

public class Controller {
    BookData bookData = new BookData();

    public void setFilename(String filename){
        bookData.setFilename(filename);
    }

    public ObservableList<Book> getBookDataObList() {
        return bookData.getBookObservableList();
    }

}
