package sample.logic;

import java.util.ArrayList;

public class MyCompiler {
    private String inputProgramText;
    private LA exampleLA = new LA();
    private SA exampleSA = new SA(exampleLA);
    private SAAutomate exampleSAA = new SAAutomate(exampleLA);
    public static boolean isWorking = false;
    private ArrayList<String> SetWithException = new ArrayList<>();

    public void startLA(){
        exampleLA.CheckAndDivide(this.getInputProgramText());
        exampleLA.PrintTable();
        this.addSetWithException(exampleLA.getSetWithLAException());
    }

   public void startSA(){
        exampleSA.SAnalize();
        this.addSetWithException(exampleSA.getSetWithSAException());
    }

    public void startSAA(){
        exampleSAA.run();
        for (Object ch: exampleSAA.getDataTable()) {
            System.out.println(ch);
        }
        this.addSetWithException(exampleSAA.getErrors());
    }

    public void setInputProgramText(String programText) {
        this.inputProgramText = programText;
    }

    public String getInputProgramText() {
        return inputProgramText;
    }

    public ArrayList<String> getSetWithException() {
        return SetWithException;
    }

    public void addSetWithException(ArrayList<String> setWithException) {
        SetWithException.addAll(setWithException);
    }
}
