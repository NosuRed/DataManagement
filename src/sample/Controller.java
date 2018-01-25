package sample;

import javafx.collections.ObservableList;

public class Controller {
    BookData documentData = new BookData();

    public void setFilename(String filename){
        documentData.setFilename(filename);
    }

    public ObservableList<Document> getDocumentsDataObList() {
        return documentData.getDocumentObservableList();
    }

    public Document getaddDocument(Document document){
        return documentData.addDocument(document);
    }

    public void startQuery(){
        documentData.startQuery();
    }

    public void deleteDocument(int document){
        documentData.deleteDocument(document);
    }

    public  ObservableList<Keyword> getKeywordsObList(){
     return documentData.getKeywordObservableList();
    }

}
