package sample.GUI.tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.logic.Lexema;

public class Observable_ListWithLexem {
    private ObservableList<Lexema> lexemaObservableList = FXCollections.observableArrayList();

    public ObservableList<Lexema> getLexemaObservableList() {
        return lexemaObservableList;
    }

    public void setLexemaObservableList(ObservableList<Lexema> lexemaObservableList) {
        this.lexemaObservableList = lexemaObservableList;
    }

    public void add (Lexema lexema){
        lexemaObservableList.add(lexema);
    }

    public void setData(){
        lexemaObservableList.add(new Lexema(1, "lex1",1));
        lexemaObservableList.add(new Lexema(1, "lex1.2",2));
        lexemaObservableList.add(new Lexema(2, "lex2",3));
        lexemaObservableList.add(new Lexema(2, "lex2.2",5, 1));
        lexemaObservableList.add(new Lexema(3, "lex3",4));
        lexemaObservableList.add(new Lexema(4, "lex4",5, 2));
        lexemaObservableList.add(new Lexema(6, "lex1",6,1));
    }
}
