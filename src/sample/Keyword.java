package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Keyword {
    private BooleanProperty selected;
    private SimpleStringProperty keywordName;


    public Keyword(String keywordName){
        this.keywordName = new SimpleStringProperty(keywordName);
    }

    public String getKeyword() {
        return keywordName.get();
    }


}
