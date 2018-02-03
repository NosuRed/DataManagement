package sample;

import javafx.scene.control.CheckBox;

public class AddKeywordName extends CheckBox {

    Keyword keyword;

    AddKeywordName(Keyword keyword){
        this.keyword = keyword;

    }

    public Keyword getKeyword() {
        return keyword;
    }
}
