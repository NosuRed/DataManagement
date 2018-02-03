package sample;

import javafx.collections.ObservableList;

import java.util.ArrayList;


public class Controller {
    DocumentData documentData = new DocumentData();

    public void setFilename(String filename){
        documentData.setFilename(filename);
    }

    public ObservableList<Document> getDocumentsDataObList() {
        return documentData.getDocumentObservableList();
    }

    public void getaddDocument(Document document){
        documentData.addDocument(document);

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

    public void addKeyword(Keyword keyword){
        documentData.addKeyword(keyword);
    }

    public void connectKeywordToDocument(int id, String keyword){
        documentData.addKeywordsToDocument(id, keyword);
    }

    public void deletedConnectedKeywordFromDocument(int id){
        documentData.deleteKeywordsFromDeletedDocument(id);

    }



    public ArrayList<String> connectionToDocumentQuery(int id){
        return documentData.connectionToDocumentQuery(id);
    }
}
