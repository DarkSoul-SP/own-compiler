package sample.GUI.tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.logic.Ident;

//?
public class Observable_ListWithId {
    private ObservableList<Ident> identObservableList = FXCollections.observableArrayList();

    public ObservableList<Ident> getIdentObservableList() {
        return identObservableList;
    }

    public void setIdentObservableList(ObservableList<Ident> identObservableList) {
        this.identObservableList = identObservableList;
    }

    public void add (Ident ident){
        identObservableList.add(ident);
    }

    public void setData(){
        identObservableList.add(new Ident(1, "lex1","integer"));
        identObservableList.add(new Ident(1, "lex1.2","integer"));
        identObservableList.add(new Ident(2, "lex2","integer"));
        identObservableList.add(new Ident(2, "lex2.2","integer"));
        identObservableList.add(new Ident(3, "lex3","integer"));
        identObservableList.add(new Ident(4, "lex4","integer"));
        identObservableList.add(new Ident(6, "lex1","integer"));
    }
}
