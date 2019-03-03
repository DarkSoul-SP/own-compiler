package sample.logic;

import sample.transition_table.DataTableField;
import sample.transition_table.State;
import sample.transition_table.TTReader;
import sample.transition_table.TransitionElems;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class SAAutomate {
    //problem with path
    public static final String PATH_TRANSITION_TABLE = "main-info/transition_table.xml";
    private LA la;
    private Map<Integer, State> stateTransitions;
    private ArrayList<DataTableField> dataTable;
    private ArrayList<String> errors;
    private Stack<Integer> stack;
    private String curLex;
    private int i;
    private int state;


    public static void main(String[] args) {
        SAAutomate saa = new SAAutomate();

        boolean res = saa.run();
        System.out.println("SA: " + res);
//        if (res)
        System.out.println(saa.dataTable);
    }

    public SAAutomate() {
        dataTable = new ArrayList<>() {
            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < this.size(); i++) {
                    builder.append(i + 1).append(": ").append(this.get(i)).append('\n');
                }
                return builder.toString();
            }
        };
        stack = new Stack<>();
        TTReader ttr = new TTReader(PATH_TRANSITION_TABLE);
        this.la = new LA();
        la.CheckAndDivide("start main { integer id; 123 }");
        this.stateTransitions = ttr.getStates();
        this.i = 0;
        this.state = 1;
        this.curLex = getCurrentLexeme();
        this.errors = new ArrayList<>();
    }

    public SAAutomate(LA lexer) {
        dataTable = new ArrayList<>() {
            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < this.size(); i++) {
                    builder.append(i + 1).append(": ").append(this.get(i)).append('\n');
                }
                return builder.toString();
            }
        };

        stack = new Stack<>();
        TTReader ttr = new TTReader(PATH_TRANSITION_TABLE);
        this.la = lexer;
        this.stateTransitions = ttr.getStates();
    }

    public boolean run() {
        i = 0;
        state = 1;
        curLex = getCurrentLexeme();
        errors = new ArrayList<>();

        while (true) {    // || не последняя лксема
            String error;
            TransitionElems elems;
            if (hasTransition(getCurrentLexeme())) {
                elems = stateTransitions.get(state).getTransition(getCurrentLexeme());
                nextState(elems);
                inc();
            } else {
                if (hasIncompatibilityTransition()) {
                    elems = stateTransitions.get(state).getIncomparability();
                    nextState(elems);
                } else {
                    error = stateTransitions.get(state).getIncomparabilityMsg();
                    if (error != null) {
                        if (error.equals("exit")) {
                            if (stack.empty()) {
                                if (i < la.getLexemMassSize()) {
                                    error("There is extra code after closing brace. Nothing expected");
                                    return false;
                                }
                                return true;
                            }
                            addTableRecord();
                            state = stack.pop();
                            continue;
                        } else {
                            error(error);
                        }
                        return false;
                    }
                }
            }
        }
    }

    private void error(String error) {
        errors.add("line: " + getLexemeLine(i) + " - " + error + "! But found" + ((curLex.equals("")) ? " nothing" : ": " + curLex));
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void clear() {
//        this.stateTransitions.clear();
        this.dataTable.clear();
        this.stack.clear();
        this.errors.clear();
        this.i = 0;
        this.state = 1;
        this.curLex = getCurrentLexeme();
    }

    public ArrayList<DataTableField> getDataTable() {
        return dataTable;
    }

    private boolean hasTransition(String lex) {
        System.out.println("In hasTr lex: " + lex);
        return stateTransitions.get(state).getTransition(lex) != null;
    }

    private boolean hasIncompatibilityTransition() {
        return stateTransitions.get(state).getIncomparability() != null;
    }

    private void addTableRecord() {
        dataTable.add(new DataTableField(state, curLex, stackCopy()));
    }

    private void nextState(TransitionElems elems) {
        addTableRecord();
        state = elems.getNextState();
        if (elems.getStackPush() != null)
            stack.push(elems.getStackPush());
    }

    private String getCurrentLexeme() {
        if (i >= la.getLexemMassSize()) return curLex = "";
        Lexema lexeme = la.getLexemMass()[i];

        if (lexeme.getCode() > 33) {
            if (lexeme.getCode() == 34) {
                curLex = lexeme.getLexem();
                return "IDN";
            } else if (lexeme.getCode() == 35) {
                curLex = lexeme.getLexem();
                return "CON";
            } else if (lexeme.getCode() == 36) {
                curLex = lexeme.getLexem();
                return "LAB";
            }
        }
        return curLex = lexeme.getLexem();
    }

    private int getLexemeLine(int i) {
        if (la.getLexemMass().length == 0) return 0;
        int index = (i < la.getLexemMass().length) ? i : la.getLexemMass().length - 1;
        return la.getLexemMass()[index].getLine();
    }

    private boolean inc() {
        if (++i < la.getLexemMass().length) {
            return true;
        } else {
            return false;
        }
    }

    private ArrayList<Integer> stackCopy() {
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(stack);
        return list;
    }
}
