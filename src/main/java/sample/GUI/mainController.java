package sample.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;

import sample.logic.MyCompiler;

public class mainController {
    @FXML
    private MenuButton MenuFile;

    @FXML
    private MenuItem load;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem exit;

    @FXML
    private Button buttonAnalize;

    @FXML
    private Button buttonClear;

    @FXML
    private TextArea programArea;

    @FXML
    private TextArea exceptionsArea;

    @FXML
    private MenuButton MenuTables;

    @FXML
    private MenuItem itemLexems;

    @FXML
    private MenuItem itemIdentifiers;

    @FXML
    private MenuItem itemConsts;

    @FXML
    private MenuItem itemLabels;

    private StringBuilder strException;

    @FXML
    void initialize() {
        load.setOnAction(event -> {
            final FileChooser fileChooser = new FileChooser();
            String inStr = null;

            fileChooser.setTitle("Load file");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("Text", "*.txt"),
                    new FileChooser.ExtensionFilter("XML", "*.xml"));

            File file = fileChooser.showOpenDialog(buttonAnalize.getScene().getWindow());

            if (file != null) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader input = new InputStreamReader(fis, "UTF-8");
                    inStr = "";
                    int ch = 0;
                    while ((ch = input.read()) != -1)
                        inStr += (char) ch;

                    input.close();
                    fis.close();
                } catch (Exception error) {
                    modalWindow("File was not load.");
                }
            }

            if(inStr != null) {
                programArea.setText(inStr);
            }
        });

        save.setOnAction(event -> {
            if (programArea.getText().trim().isEmpty()) {
                modalWindow("Enter your program.");
            } else {
                final FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save file");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Files", "*.*"),
                        new FileChooser.ExtensionFilter("Text", "*.txt"),
                        new FileChooser.ExtensionFilter("XML", "*.xml"));

                File file = fileChooser.showSaveDialog(buttonAnalize.getScene().getWindow());

                if (file != null) {
                    try {
                        PrintWriter pw = new PrintWriter(file);
                        pw.print(programArea.getText());

                        pw.close();
                    } catch (Exception error) {}
                }else modalWindow("File was not saved.");
            }
        });

        buttonAnalize.setOnAction(event -> {
            MyCompiler ex = new MyCompiler();
            ex.isWorking = false;
            strException = new StringBuilder();
            exceptionsArea.clear();

            if(programArea.getText().trim().isEmpty()) {
                modalWindow("You not enter your program.");
            } else{
                ex.setInputProgramText(programArea.getText().trim());
                ex.startLA();
                ex.startSAA();
                ex.isWorking = true;
            }

            if(ex.isWorking && !ex.getSetWithException().isEmpty()){
                for (String exception : ex.getSetWithException()) {
                    strException
                            .append(exception)
                            .append("\n");
                }
                exceptionsArea.setText(strException.toString());
            } else if(ex.isWorking)exceptionsArea.setText("Analize uccessfully passed.");
        });

        itemLexems.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithLexems.fxml");
            else modalWindow("Your program was not analized.");
        });

        itemIdentifiers.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithId.fxml");
            else modalWindow("Your program was not analized.");
        });

        itemConsts.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithConsts.fxml");
            else modalWindow("Your program was not analized.");
        });

        itemLabels.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithLabels.fxml");
            else modalWindow("Your program was not analized.");
        });

        buttonClear.setOnAction(event -> {
            exceptionsArea.clear();
            programArea.clear();
        });

        exit.setOnAction(event -> {
            Stage stage = (Stage)buttonClear.getScene().getWindow();
            stage.close();
        });
    }

    public void openNewScene (String window){
        //buttonClear.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(true);
        stage.showAndWait();
    }

    public void modalWindow(String message) {
        Label label = new Label(message);
        StackPane LayoutWithLabel = new StackPane();
        LayoutWithLabel.getChildren().add(label);
        Scene modalScene = new Scene(LayoutWithLabel, 400, 100);

        Stage modalStage = new Stage();
        modalStage.setTitle("Attention");
        modalStage.setResizable(false);
        modalStage.setScene(modalScene);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(buttonAnalize.getScene().getWindow());
        modalStage.setX(buttonAnalize.getScene().getWindow().getX() + 100);
        modalStage.setY(buttonAnalize.getScene().getWindow().getY() + 100);

        modalStage.show();
    }
}

