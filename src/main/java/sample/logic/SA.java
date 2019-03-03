package sample.logic;

import java.util.ArrayList;

public class SA {

    //private static int currentNumOfLex = 0;
    private  int currentNumOfLex = 0;
    private static boolean flagOfCor = false;
    private LA inputLAResault;
    private ArrayList<String> SetWithSAException = new ArrayList<>();

    public SA() {}
    public SA(LA inputLAResault) { this.inputLAResault = inputLAResault; }

    public ArrayList<String> getSetWithSAException() {
        return SetWithSAException;
    }
    private void addInSetWithSAException(String newException) {
        SetWithSAException.add(newException);
    }

    public void SAnalize(){

        if(CurLexCode() == 1){
            currentNumOfLex++;
            if(CurLexCode() == 34) {
                currentNumOfLex++;
                if (CurLexCode() == 2) {
                    currentNumOfLex++;
                    if (ListWithDaclaration()) {
                        currentNumOfLex++;
                        if (ListWithOper()) {
                            if (CurLexCode() == 3) {
                                flagOfCor = true;
                            }
                            else{ System.out.println("Error: End of program not specified.");
                            this.addInSetWithSAException("Error: End of program is not specified.");}
                        }
                        else{ System.out.println("Error: Incorrect list of operators.");
                        this.addInSetWithSAException("Error: Incorrect list of operators.");}
                    }
                    else{ System.out.println("Error: Incorrect list of declarations.");
                    this.addInSetWithSAException("Error: Incorrect list of declarations.");}
                }
                else{ System.out.println("Error: Missed '{' in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Missed '{' after name of program.");}
            }
            else{ System.out.println("Error: Program name is expected in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Program name is expected after keyword 'start'.");}
        }
        else{ System.out.println("Error: Missed keyword 'start' in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine()+ ": Missed keyword 'start'.");}
    }

    private boolean ListWithDaclaration(){
        flagOfCor = true;

        //check first init allright
        if(InitVariable()) {
            //check ;
            if (CurLexCode() == 27) {
                currentNumOfLex++;

                while (IsType()) {
                    if (CurLexCode() == 34) {
                        currentNumOfLex++;
                        if (CurLexCode() == 27) {
                            currentNumOfLex++;
                        }
                        else {
                            System.out.println("Error:InWhileListWD Missed ';' in line: " + CurLine()); flagOfCor = false;
                            this.addInSetWithSAException("Line " + CurLine()+ ": Missed ';' after declaration.");
                        }
                    }
                    else {
                        System.out.println("Error:InWhileListWD Name of variable is expected in line: " + CurLine()); flagOfCor = false;
                        this.addInSetWithSAException("Line " + CurLine()+ ": Name of variable is expected after type.");
                    }
                }
            }
            else {System.out.println("Error:InListWD Missed ';' in line: " + CurLine()); flagOfCor = false;
                this.addInSetWithSAException("Line " + CurLine()+ ": Missed ';' after declaration.");
            }
        }
        else {System.out.println("Error:InListWD Incorrect initialization of variable in line: " + CurLine()); flagOfCor = false;
            this.addInSetWithSAException("Line " + CurLine()+ " : Incorrect initialization of variable.");}

        return flagOfCor;
    }

    private boolean IsType(){
        boolean isTypeFlag = false;

        if (CurLexCode() == 4 || CurLexCode() == 5){
            currentNumOfLex++;
            isTypeFlag = true;
        }
        else currentNumOfLex--;

        return isTypeFlag;
    }

    private boolean InitVariable(){
        flagOfCor = true;

        //check type int||const
        if (CurLexCode() == 4 || CurLexCode() == 5){
            currentNumOfLex++;
            //check id or not
            if(CurLexCode() == 34)
                currentNumOfLex++;
            else {System.out.println("Error:InInitVar Name of variable is expected in line: " + CurLine()); flagOfCor = false;
                this.addInSetWithSAException("Line " + CurLine()+ ": Name of variable is expected after type.");}
        }
        else {System.out.println("Error:InInitVar Unknown type of variable in line: " + CurLine()); flagOfCor = false;
            this.addInSetWithSAException("Line " + CurLine()+ ": SE.Unknown type of variable.");}

        return flagOfCor;
    }

    private boolean ListWithOper(){
        flagOfCor = true;

        if(IsLabel() || IsOper()){
            currentNumOfLex++;
            if(currentNumOfLex >= inputLAResault.getLexemMassSize()){
                System.out.println("Error: EndOfProgramNotFound.");
                this.addInSetWithSAException("Error: EndOfProgramNotFound.");
                currentNumOfLex --;
                return false;
            }

            while (CurLexCode() != 3 && flagOfCor == true){
                flagOfCor = false;
                //System.out.println("\nOper start from line " + CurLine() + " lex: " + CurLex());

                if(IsLabel()) currentNumOfLex++;
                else
                if(IsOper()) currentNumOfLex++;
                else {
                    System.out.println("Error: Unknown type of statement in line: " + CurLine() + " ,start from lex '" + CurLex() + "'.");
                    this.addInSetWithSAException("Line " + CurLine()+ ": Unknown type of statement start from lex '" + CurLex() + "'.");
                    flagOfCor = false;
                }

                //Если конец программы не найден, а лексемы закончились.
                if(currentNumOfLex >= inputLAResault.getLexemMassSize()){
                    System.out.println("Error: EndOfProgramNotFound.");
                    this.addInSetWithSAException("Error: EndOfProgramNotFound.");
                    currentNumOfLex --;
                    return false;
                }
                //System.out.println("Oper end in line " + CurLine() + " lex: " + CurLex());
            }
        }
        else {System.out.println("Error: Incorrect statement in line: " + CurLine()); flagOfCor = false;
            this.addInSetWithSAException("Line " + CurLine()+ ": Incorrect statement.");}

        return flagOfCor;
    }

    private boolean IsOper(){
        boolean temp = false;
        boolean fail = false;

        if(CurLexCode() == 30) {
            //System.out.println("Start CondTr in line: " + CurLine() + " lex: " + CurLex());
            if(IsCondTransition()) { temp = true; currentNumOfLex++; }
            else{
                System.out.println("Error: In conditional transition in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Mistake in conditional transition.");
                fail = true;}
        }

        if(CurLexCode() == 21 && temp == false && fail == false) {
            //System.out.println("Cycle in line: " + CurLine() + " lex: " + CurLex());
            if(IsCycle()) { temp = true; currentNumOfLex++; }
            else{
                System.out.println("Error: Loop error in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Loop error.");
                fail = true;
            }
        }

        if(CurLexCode() == 34 && temp == false && fail == false) {
            //System.out.println("Start VarINit in line: " + CurLine() + " lex: " + CurLex());
            if(IsVarInit()) { temp = true; currentNumOfLex++; }
            else{
                System.out.println("Error: Incorrect initialization of const or variable in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Incorrect initialization of const or variable.");
                fail = true;
            }
        }

        if(CurLexCode() == 19 && temp == false && fail == false) {
            //System.out.println("Start input in line: " + CurLine() + " lex: " + CurLex());
            if(IsInput()) { temp = true; currentNumOfLex++; }
            else {
                System.out.println("Error: Incorrect input operation in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Incorrect input operation.");
                fail = true;
            }
        }

        if(CurLexCode() == 20 && temp == false && fail == false) {
            //System.out.println("Start output in line: " + CurLine() + " lex: " + CurLex());
            if(IsOutput()) { temp = true; currentNumOfLex++; }
            else {
                System.out.println("Error: Incorrect output operation in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Incorrect output operation.");
                fail = true;
            }
        }

        //check ; after statement
        if(CurLexCode() == 27 && temp == true && fail == false) {
            flagOfCor = true;
        }
        else if(temp == true){
            currentNumOfLex--;
            System.out.println("Error: Missed ';' after statement in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine()+ ": Missed ';' after statement.");
            temp = false;
        }

        return temp;
    }

    private boolean IsLabel(){
        boolean temp = false;

        if(CurLexCode() == 36) {
            temp = true;
            flagOfCor = true;
        }
        return temp;
    }

    private boolean IsCondTransition(){
        boolean temp = false;
        currentNumOfLex++;

        if(CurLexCode() == 28) {
            currentNumOfLex++;
            if(IsRelation()){
                currentNumOfLex++;
                if(CurLexCode() == 29){
                    currentNumOfLex++;
                    if(CurLexCode() == 31){
                        currentNumOfLex++;
                        if(IsGoTo()){
                            temp = true;
                        }
                    }
                    else { System.out.println("Error: Keyword 'then' is excected in line: " + CurLine());
                        this.addInSetWithSAException("Line " + CurLine()+ ": Keyword 'then' is excected.");
                    }
                }
                else{ System.out.println("Error: Missed ')' in line: " + CurLine());
                    this.addInSetWithSAException("Line " + CurLine()+ ": Missed ')'.");
                }
            }
        }
        else { System.out.println("Error: Missed '(' in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine()+ ": Missed '('.");
        }

        flagOfCor = temp;

        return temp;
    }

    private boolean IsCycle(){
        boolean temp = false;
        currentNumOfLex++;

        if(CurLexCode() == 28) {
            currentNumOfLex++;
            if(CurLex().equals("integer")){
                currentNumOfLex++;
                if(CurLexCode() == 34) {
                    currentNumOfLex++;
                    if(CurLexCode() == 8) {
                        currentNumOfLex++;
                        if (IsExpression()) {
                            currentNumOfLex++;
                            if(CurLexCode() == 22) {
                                currentNumOfLex++;
                                if(IsExpressionInParenthees()){
                                    currentNumOfLex++;
                                    if(CurLexCode() == 23) {
                                        currentNumOfLex++;
                                        if(IsRelation()){
                                            currentNumOfLex++;
                                            if(CurLexCode() == 29) {
                                                currentNumOfLex++;
                                                if(CurLexCode() == 24) {
                                                    currentNumOfLex++;
                                                    if(CurLexCode() == 32) {
                                                        currentNumOfLex++;
                                                        if(IsLabel() || IsOper()) {
                                                            currentNumOfLex++;
                                                            if(CurLexCode() == 25) {
                                                                temp = true;
                                                            }
                                                            else{ System.out.println("Error: Keyword 'enc' is expected  in line: " + CurLine());
                                                                this.addInSetWithSAException("Line " + CurLine()+ ": Keyword 'enc' is expected.");
                                                            }
                                                        }
                                                        else{ System.out.println("Error: Label or statement is expected after 'do' in line: " + CurLine());
                                                            this.addInSetWithSAException("Line " + CurLine()+ ": Label or statement is expected after 'do'.");
                                                        }
                                                    }
                                                    else{ System.out.println("Error: Keyword 'do' is expected in line: " + CurLine());
                                                        this.addInSetWithSAException("Line " + CurLine()+ ": Keyword 'do' is expected.");
                                                    }
                                                }
                                                else{ System.out.println("Error: Keyword 'stc' is expected in line: " + CurLine());
                                                    this.addInSetWithSAException("Line " + CurLine()+ ": Keyword 'stc' is expected.");
                                                }
                                            }
                                            else{ System.out.println("Error: Missed ')' after relation in line: " + CurLine());
                                                this.addInSetWithSAException("Line " + CurLine()+ ": Missed ')' after relation.");
                                            }
                                        }
                                        else{ System.out.println("Error: Сondition is expected after 'while' in line: " + CurLine());
                                            this.addInSetWithSAException("Line " + CurLine()+ ": Сondition is expected after 'while'.");
                                        }
                                    }
                                    else{ System.out.println("Error: Keyword 'while' is expected after step in line: " + CurLine());
                                        this.addInSetWithSAException("Line " + CurLine()+ ": Keyword 'while' is expected after step.");
                                    }
                                }
                                else{ System.out.println("Error: Step(expression) is expected after 'by' in line: " + CurLine());
                                    this.addInSetWithSAException("Line " + CurLine()+ ": Step(expression) is expected after 'by'.");
                                }
                            }
                            else{ System.out.println("Error: Keyword 'by' is expected after initiolizaton in line: " + CurLine());
                                this.addInSetWithSAException("Line " + CurLine()+ ": Keyword 'by' is expected after initiolizaton.");
                            }
                        }
                    }
                    else{ System.out.println("Error: Missed '=' after variable in line: " + CurLine());
                        this.addInSetWithSAException("Line " + CurLine()+ ": Missed '=' after variable.");
                    }
                }
                else{ System.out.println("Error: Name of variable is expected in initiolizaton of variable in line: " + CurLine());
                    this.addInSetWithSAException("Line " + CurLine()+ ": Name of variable is expected in initiolizaton of variable.");
                }
            }
            else{ System.out.println("Error: Type of variable in initiolizaton of cycle can be only 'integer'. Line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Type of variable in initiolizaton of cycle can be only 'integer'.");
            }
        }
        else{ System.out.println("Error: Missed '(' after keyword 'for' in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine()+ ": Missed '(' after keyword 'for'.");
        }
        return temp;
    }

    private boolean IsVarInit(){
        boolean temp = false;
        boolean initConst = false;

        //check const or not
        for (int i = 0; i < inputLAResault.getIdentMass().length; i++) {
            if(CurLex().equals(inputLAResault.getIdentMass()[i].getIdn())
            && inputLAResault.getIdentMass()[i].getType().equals("сonst")){
                initConst = true;
                break;
            }
        }
        if(initConst) {
            currentNumOfLex++;
            if(CurLexCode() == 8) {
                currentNumOfLex++;
                if (IsInitConst()) temp = true;
            }
            else{ System.out.println("Error: Missed '=' after const in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Missed '=' after const.");
            }
        }
        else {
            //System.out.println("Current ExpInVarinit: " + CurLex());
            currentNumOfLex++;
            if(CurLexCode() == 8) {
                currentNumOfLex++;
                if (IsExpression()) temp = true;
            }
            else{ System.out.println("Error: Missed '=' after variable in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine()+ ": Missed '=' after variable.");
            }
        }

        return temp;
    }

    private boolean IsInitConst(){
        boolean temp = false;

        if(CurLexCode() == 35) temp = true;
        else this.addInSetWithSAException("Line " + CurLine()+ ": Const name is expected.");

        return temp;
    }

    private boolean IsInput(){
        boolean temp = false;

        currentNumOfLex++;
        if(CurLexCode() == 6) {
            currentNumOfLex++;
            if(CurLexCode() == 34)
                temp = true;
        }
        return temp;
    }

    private boolean IsOutput(){
        boolean temp = false;
        currentNumOfLex++;

        if(CurLexCode() == 7) {
            currentNumOfLex++;
            if(CurLexCode() == 34 || IsExpression())
                temp = true;
        }
        return temp;
    }

    private boolean IsRelation(){
        boolean temp = false;

        if(CurLexCode() == 34 || IsExpression()) {
            currentNumOfLex++;
            if(IsComparisonSign()){
                currentNumOfLex++;
                if(CurLexCode() == 34 || IsExpression()){
                    temp = true;
                }
                else {
                    System.out.println("Error: Expected variable or expression as the second argument of comparison in line: " + CurLine());
                    this.addInSetWithSAException("Line " + CurLine() + ": Expected variable or expression as the second argument of comparison.");
                }
            }
            else{ System.out.println("Error: Missed comparison sign in relation in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine() + ": Missed comparison sign in relation.");
            }
        }
        else{ System.out.println("Error: Expected variable or expression as the first argument of comparison in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine() + ": Expected variable or expression as the first argument of comparison.");
        }

        if(temp == false) {
            System.out.println("Error: Incorrect relation in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine() + ": Incorrect relation.");
            flagOfCor = false;
        }

        return temp;
    }

    private boolean IsExpression(){
        boolean temp = false;
        boolean nextIsTerm = false;
        boolean nextIsSign = false;

        //System.out.println("StartInExp: " + CurLex());
        if(IsTerm()){
            temp = true;
            //check sign +- or not
            if(inputLAResault.getLexemMass()[currentNumOfLex+1].getCode() == 15 ||
                    inputLAResault.getLexemMass()[currentNumOfLex+1].getCode() == 16){
                nextIsSign = true;
                currentNumOfLex = currentNumOfLex + 2; // skip sign and check term or not
                if (IsTerm()) {
                    nextIsTerm = true;
                    while (nextIsSign && nextIsTerm) {
                        nextIsTerm = false;
                        nextIsSign = false;
                        if (inputLAResault.getLexemMass()[currentNumOfLex + 1].getCode() == 15 ||
                                inputLAResault.getLexemMass()[currentNumOfLex + 1].getCode() == 16) {
                            nextIsSign = true;
                            currentNumOfLex = currentNumOfLex + 2;
                            if (IsTerm()) nextIsTerm = true;
                        }
                    }
                }
            }
        }

        return temp;
    }

    private boolean IsTerm(){
        boolean temp = false;
        boolean nextIsMul = false;
        boolean nextIsSign = false;

        if(IsMultiplier()){
            temp = true;
            //check sign */ or not
            if(inputLAResault.getLexemMass()[currentNumOfLex+1].getCode() == 17 ||
                    inputLAResault.getLexemMass()[currentNumOfLex+1].getCode() == 18){
                nextIsSign = true;
                currentNumOfLex = currentNumOfLex+2; // skip sign and check mult or not
                if (IsMultiplier()) {
                    nextIsMul = true;
                    while (nextIsSign && nextIsMul) {
                        nextIsMul = false;
                        nextIsSign = false;
                        if (inputLAResault.getLexemMass()[currentNumOfLex + 1].getCode() == 17 ||
                                inputLAResault.getLexemMass()[currentNumOfLex + 1].getCode() == 18) {
                            nextIsSign = true;
                            currentNumOfLex = currentNumOfLex + 2; // skip sign and check mult or not
                            if (IsMultiplier()) nextIsMul = true;
                        }
                    }
                }
            }
        }

        return temp;
    }

    private boolean IsMultiplier(){
        boolean temp = false;

        if(CurLexCode() == 34 || CurLexCode() == 35 || IsExpressionInParenthees()){
            temp = true;
        }
        else{ System.out.println("Error: Variable, number or expression expected instead of '" + CurLex() + "' in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine() + ": Variable, number or expression expected instead of  '" + CurLex() + "'.");
        }
        return temp;
    }

    private boolean IsExpressionInParenthees(){
        boolean temp = false;

        if(CurLexCode() == 28) {
            currentNumOfLex++;
            if (IsExpression()) {
                currentNumOfLex++;
                if (CurLexCode() == 29) {
                    temp = true;
                }
                else{ System.out.println("Error: ')' expected instead of '" + CurLex() + "' in line: " + CurLine());
                    this.addInSetWithSAException("Line " + CurLine() + ":Variable, number or expression expected instead of  '" + CurLex() + "' after '('.");
                }
            }
            else{ System.out.println("Error: Variable, number or expression expected instead of '" + CurLex() + "' in line: " + CurLine());
                this.addInSetWithSAException("Line " + CurLine() + ": Variable, number or expression expected instead of '" + CurLex() + "' after '('.");
            }
        }
        return temp;
    }

    private boolean IsComparisonSign(){
        boolean temp = false;

        for (int i = 9; i <= 14; i++) {
            if(CurLexCode() == i) {
                temp = true;
                break;
            }
        }

        return temp;
    }

    private boolean IsGoTo(){
        boolean temp = false;

        if(CurLexCode() == 33) {
            currentNumOfLex++;
            if(IsLabel()) temp = true;
            else{ System.out.println("Error: Not label after keyword 'goto' in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine() + ": Not label after keyword 'goto'.");
        }
        }
        else{ System.out.println("Error: Keyword 'goto' is expected in line: " + CurLine());
            this.addInSetWithSAException("Line " + CurLine() + ": Keyword 'goto' is expected.");
        }

        return temp;
    }

    private int CurLine(){
        return inputLAResault.getLexemMass()[currentNumOfLex].getLine();
    }

    private String CurLex(){
        return inputLAResault.getLexemMass()[currentNumOfLex].getLexem();
    }

    private int CurLexCode(){
        return inputLAResault.getLexemMass()[currentNumOfLex].getCode();
    }
}