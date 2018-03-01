package DocumentManagementProject;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    private DocumentData documentData = new DocumentData();

    public ObservableList<Document> getDocumentsDataObList() {
        return documentData.getDocumentObservableList();
    }

    public void addDocument(Document document) throws ErrorExceptionThrow{
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

    public void addKeyword(String keyword) throws ErrorExceptionThrow{
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
        documentData.startKeywordConnectionQuery(id);

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

    public void getAddIdToReferenceTable(int docID){
        documentData.addIdToReferenceTable(docID);
    }

    public void getAddFileToPathTable(String filePath, String fileName) throws ErrorExceptionThrow {
        documentData.addFileToFilePathTable(filePath, fileName);
    }

    public RefFilePath selectFilePath(int docId){
        return documentData.selectFilePathTable(docId);
    }

    public void getAddUrlToUrlTable(String urlInsert) throws ErrorExceptionThrow{
        documentData.addUrlToUrlTable(urlInsert);
    }

    public void addToArchive(String shed, String rack, String folder){
        documentData.addToArchive(shed, rack, folder);
    }

    public String selectReference(int refID){
        return documentData.selectReference(refID);
    }

    public int selectReferenceID(int docID){
        return documentData.selectReferenceID(docID);
    }
}
