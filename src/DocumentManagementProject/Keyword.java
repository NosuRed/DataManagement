package DocumentManagementProject;

import javafx.beans.property.SimpleStringProperty;



public class Keyword {

    private SimpleStringProperty keywordName;
    private int keywordID;

    /**
     * a Keyword for an observableList is created
     * @param keywordName is declared as simpleProperties so that it can be used in a TableView
     * @param keywordID the keyword ID is automatically generated
     */
    public Keyword(String keywordName, int keywordID){
        this.keywordName = new SimpleStringProperty(keywordName);
        this.keywordID = keywordID;
    }


    public String getKeyword() {
        return keywordName.get();
    }

    public int getKeywordID() {
        return keywordID;
    }
}
