package sample.GUI.tables.tablesControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.GUI.tables.Observable_ListWithId;
import sample.logic.Ident;
import sample.logic.LA;

public class tableWithIdController {

    @FXML
    private TableView<Ident> tableIdent;

    @FXML
    private TableColumn<Ident, Integer> columnNumber;

    @FXML
    private TableColumn<Ident, String> columnIdn;

    @FXML
    private TableColumn<Ident, String> columnType;

    @FXML
    void initialize() {
        //автоматически через геттеры объекта Lexema будет искать подходящее поле и заполнять колонку
        columnNumber.setCellValueFactory(new PropertyValueFactory<Ident, Integer>("num"));
        columnIdn.setCellValueFactory(new PropertyValueFactory<Ident, String>("idn"));
        columnType.setCellValueFactory(new PropertyValueFactory<Ident, String>("type"));

        Observable_ListWithId ex = new Observable_ListWithId();
        LA setIdentInTable = new LA();

        for (int i = 0; i < setIdentInTable.getIdentMassSize() ; i++) {
            ex.add(setIdentInTable.getIdentMass()[i]);
        }

        tableIdent.setItems(ex.getIdentObservableList());
    }
}

