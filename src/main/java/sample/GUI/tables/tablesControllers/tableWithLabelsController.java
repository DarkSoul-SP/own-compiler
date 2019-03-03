package sample.GUI.tables.tablesControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.GUI.tables.Observable_ListWithLabels;
import sample.logic.LA;
import sample.logic.Label;

public class tableWithLabelsController {

    @FXML
    private TableView<Label> tableLabel;

    @FXML
    private TableColumn<Label, Integer> columnNumber;

    @FXML
    private TableColumn<Label, String> columnLabel;


    @FXML
    void initialize() {
        columnNumber.setCellValueFactory(new PropertyValueFactory<Label, Integer>("num"));
        columnLabel.setCellValueFactory(new PropertyValueFactory<Label, String>("lab"));

        Observable_ListWithLabels ex = new Observable_ListWithLabels();
        LA setLabInTable = new LA();

        for (int i = 0; i < setLabInTable.getLabMassSize() ; i++) {
            ex.add(setLabInTable.getLabMass()[i]);
        }


        tableLabel.setItems(ex.getLabelsObservableList());
    }
}

