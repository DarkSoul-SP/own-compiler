package sample.GUI.tables.tablesControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.GUI.tables.Observable_ListWithLexem;
import sample.logic.LA;
import sample.logic.Lexema;

public class tableWithLexemsController {

    @FXML
    private TableView<Lexema> tableLexem;

    @FXML
    private TableColumn<Lexema, Integer> columnLine;

    @FXML
    private TableColumn<Lexema, String> columnLexem;

    @FXML
    private TableColumn<Lexema, Integer> columnCode;

    @FXML
    private TableColumn<Lexema, Integer> columnSecondCode;

    @FXML
    void initialize() {
        columnLine.setCellValueFactory(new PropertyValueFactory<Lexema, Integer>("line"));
        columnLexem.setCellValueFactory(new PropertyValueFactory<Lexema, String>("lexem"));
        columnCode.setCellValueFactory(new PropertyValueFactory<Lexema, Integer>("code"));
        columnSecondCode.setCellValueFactory(new PropertyValueFactory<Lexema, Integer>("secondCode"));



        Observable_ListWithLexem ex = new Observable_ListWithLexem();
        LA setLexInTable = new LA();

        for (int i = 0; i <setLexInTable.getLexemMassSize() ; i++) {
            ex.add(setLexInTable.getLexemMass()[i]);
        }

        tableLexem.setItems(ex.getLexemaObservableList());
    }
}
