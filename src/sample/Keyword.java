package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class Keyword {
    private SimpleStringProperty keywordName;
    private CheckBox checkBox;


    public Keyword(String keywordName){
        this.keywordName = new SimpleStringProperty(keywordName);
        this.checkBox = new CheckBox();
    }

    public String getKeyword() {
        return keywordName.get();
    }

   public CheckBox getCheckBox(){
        return checkBox;
   }

   public void setCheckBox(){
        this.checkBox = checkBox;
   }


}
