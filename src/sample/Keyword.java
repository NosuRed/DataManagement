package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class Keyword {
    private SimpleStringProperty keywordName;
    private int keywordID;


    public Keyword(String keywordName){
        this.keywordName = new SimpleStringProperty(keywordName);
    }

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
