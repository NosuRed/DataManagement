package sample;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    private DocumentData documentData = new DocumentData();


    public ObservableList<Document> getDocumentsDataObList() {
        return documentData.getDocumentObservableList();
    }

    public void addDocument(Document document){
            documentData.addDocument(document);
            }

    public void startKeywordTableQuery(){
        documentData.startKeywordTableQuery();
    }

    public void startDocumentTableQuery(){
        documentData.startDocumentTableQuery();
    }

    public void deleteDocument(int document){
        documentData.deleteDocument(document);
    }

    public  ObservableList<Keyword> getKeywordsObList(){
     return documentData.getKeywordObservableList();
    }

    public void deleteKeyword(int keyword){
        documentData.deleteKeyWord(keyword);
    }

    public void addKeyword(Keyword keyword){
        documentData.addKeyword(keyword);
    }

    public void connectKeywordToDocument(int id, int keyword){
        documentData.addKeywordsToDocument(id, keyword);
    }

    public void deletedConnectedKeywordFromDocument(int id){
        documentData.deleteKeywordsFromDeletedDocument(id);

    }

    public void deleteSingleKeyword(int keyword, int id){
        documentData.deleteSingleKeyword(keyword, id);
    }

    public void deleteKeywordFromActiveKeywordQuery(int keyword){
        documentData.deleteKeywordsFromActiveKeywordTable(keyword);
    }

    public void startKeywordIDQuery(int id){
        documentData.startKeywordReferenceQuery(id);

    }


    public void startKeywordDisplayQuery(int keywordID){
        documentData.startKeywordDisplayQuery(keywordID);
    }

    public  ArrayList<String> getDisplayKeywordsArray(){
        return documentData.displayKeywordsArray;
    }

    public List<Integer> getKeywordIdArray(){
        return documentData.getKeywordIDArray();
    }

}
