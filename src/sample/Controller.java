package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;


public class Controller {
    DocumentData documentData = new DocumentData();

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

    public void deleteKeyword(String keyword){
        documentData.deleteKeyWord(keyword);
    }

    public Keyword addKeyword(Keyword keyword){
        return documentData.addKeyword(keyword);
    }



}
