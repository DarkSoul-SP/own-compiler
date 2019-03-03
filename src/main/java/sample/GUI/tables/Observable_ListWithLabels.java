package sample.GUI.tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.logic.Label;


public class Observable_ListWithLabels {
    private ObservableList<Label> labelsObservableList = FXCollections.observableArrayList();

    public ObservableList<Label> getLabelsObservableList() {
        return labelsObservableList;
    }

    public void setLabelsObservableList(ObservableList<Label> labelsObservableList) {
        this.labelsObservableList = labelsObservableList;
    }

    public void add (Label label){
        labelsObservableList.add(label);
    }

    public void setData(){
        labelsObservableList.add(new Label(1, "@label1"));
        labelsObservableList.add(new Label(1, "@gd"));
        labelsObservableList.add(new Label(2, "@df"));
        labelsObservableList.add(new Label(2, "@gda"));
    }
}
