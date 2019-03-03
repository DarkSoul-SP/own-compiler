package sample.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import java.io.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import sample.logic.MyCompiler;
import javax.swing.*;

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

    @FXML
    void initialize() {
        load.setOnAction(event -> {

            FileInputStream fis = null;
            InputStreamReader input = null;
            BufferedReader br = null;
            String inStr = "";

            JFileChooser choosedFile = new JFileChooser();
            int ret = choosedFile.showDialog(null, "Select File");
            if (ret == JFileChooser.APPROVE_OPTION) {
                try
                {
                    File file = choosedFile.getSelectedFile();
                    try {
                        fis = new FileInputStream(file);
                        input = new InputStreamReader(fis,"UTF-8");

                        int ch = 0;
                        while ((ch = input.read()) != -1)
                            inStr += (char)ch;

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception error)
                {
                    JOptionPane.showMessageDialog(null, "File not saved.");
                }
            }

            programArea.setText(inStr);
        });

        save.setOnAction(event -> {
                    if (programArea.getText() == " " || programArea.getText() == "\t" || programArea.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "You not enter your program");
                    } else {
                        JFileChooser choosedFile = new JFileChooser();
                        int ret = choosedFile.showDialog(null, "Select File");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            try {
                                File file = choosedFile.getSelectedFile();

                                PrintWriter pw = null;
                                try {
                                    pw = new PrintWriter(file);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                pw.print(programArea.getText());
                                System.out.print("\n You saved file with text: " + programArea.getText());
                                pw.close();
                            } catch (Exception error) {
                                JOptionPane.showMessageDialog(null, "File not saved.");
                            }
                        }
                    }
                });

        buttonAnalize.setOnAction(event -> {
            MyCompiler ex = new MyCompiler();
            String strException = new String();
            exceptionsArea.clear();

            if(programArea.getText() == " " || programArea.getText() == "\t" || programArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You not enter your program");
            } else{
                ex.setInputProgramText(programArea.getText());
                ex.startLA();
                ex.startSAA();
                ex.isWorking = true;
            }

            if(ex.isWorking){
                for (String exception : ex.getSetWithException()
                     ) {
                    System.out.println(exception);
                    strException += exception + "\n";
                }
                exceptionsArea.setText(strException);
            }
        });

        itemLexems.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithLexems.fxml");
            else JOptionPane.showMessageDialog(null, "Your program was not analized.");
        });

        itemIdentifiers.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithId.fxml");
            else JOptionPane.showMessageDialog(null, "Your program was not analized.");
        });

        itemConsts.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithConsts.fxml");
            else JOptionPane.showMessageDialog(null, "Your program was not analized.");
        });

        itemLabels.setOnAction(event -> {
            if(MyCompiler.isWorking)
            openNewScene("/fxml/forTables/tableWithLabels.fxml");
            else JOptionPane.showMessageDialog(null, "Your program was not analized.");
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
        buttonClear.getScene().getWindow();

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
}

