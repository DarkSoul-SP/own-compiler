package sample.GUI.tables.tablesControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.GUI.tables.Observable_ListWithConsts;
import sample.logic.Const;
import sample.logic.LA;


public class tableWithConstsController {

    @FXML
    private TableView<Const> tableConst;

    @FXML
    private TableColumn<Const, Integer> columnNumber;

    @FXML
    private TableColumn<Const, String> columnConst;

    @FXML
    void initialize() {
        columnNumber.setCellValueFactory(new PropertyValueFactory<Const, Integer>("num"));
        columnConst.setCellValueFactory(new PropertyValueFactory<Const, String>("cons"));

        Observable_ListWithConsts ex = new Observable_ListWithConsts();
        LA setConstInTable = new LA();

        for (int i = 0; i < setConstInTable.getConstMassSize() ; i++) {
            ex.add(setConstInTable.getConstMass()[i]);
        }

        tableConst.setItems(ex.getConstObservableList());
    }
}
