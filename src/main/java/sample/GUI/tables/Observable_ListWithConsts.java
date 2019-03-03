package sample.GUI.tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.logic.Const;


public class Observable_ListWithConsts {
    private ObservableList<Const> constsObservableList = FXCollections.observableArrayList();

    public ObservableList<Const> getConstObservableList() {
        return constsObservableList;
    }

    public void setConstObservableList(ObservableList<Const> constsObservableList) {
        this.constsObservableList = constsObservableList;
    }

    public void add (Const con){ constsObservableList.add(con); }

    public void setData(){
        constsObservableList.add(new Const(1, "123"));
        constsObservableList.add(new Const(2, "12"));
        constsObservableList.add(new Const(3, "22"));
        constsObservableList.add(new Const(4, "31"));
    }
}
