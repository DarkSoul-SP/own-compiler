package sample.logic;

import java.io.*;
import java.util.ArrayList;

public class LA {
    private int k = 0;
    private int i = 0;
    private String inputText;
    private String temp = "";
    private String lastSymbol = "}";
    private boolean flag = true;
    private boolean flagOfTheEnd = false;
    private String Enter = new String();
    public ArrayList <String> list = new ArrayList<String>(); //весь текст разбитый на строки
    private ArrayList <String> lex = new ArrayList<String>();//лист лексем которые считались
    private int len; //длина того что в Enter
    private ArrayList <Integer> lineNum = new ArrayList<Integer>(); //номера строк
    private ArrayList <String> TRNt = new ArrayList<String>();//переменные и зарезервированые слова
    private ArrayList <String> CONt = new ArrayList<String>();//цифры(возможны повторы)
    private ArrayList <String> LABt = new ArrayList<String>();//метки(возможны повторы)
    private ArrayList <String> IDN = new ArrayList<String>();//переменные
    private ArrayList <String> typeIDN = new ArrayList<String>();
    private ArrayList <String> LAB = new ArrayList<String>();//метки
    private ArrayList <String> CON = new ArrayList<String>();//константы
    private ArrayList<String> SetWithLAException = new ArrayList<>(); //Исключения

    private String[] TRN = {"start","integer", "сonst", "cin","cout","for","by","while","stc", "enc","if","then", "do", "goto"};
    public  String[] TABLE = {"start","{","}","integer", "сonst", ">>", "<<", "=","!=","<",">","<=",">=","==",
            "+","-","*","/","cin","cout", "for", "by","while","stc", "enc", "," , ";" , "(",")","if","then", "do", "goto",
            "IDN","CON", "LAB"};//входная таблица лексем
    public char[] Separators = {'*', '+', '-', '/', ',', '(', ')', ';', '{', '}', '\t', ' '};
    private static Lexema[] A;
    private static Ident[] Idn;
    private static Const[] Con;
    private static Label[] Lab;

    public LA(String inputText) {
        this.inputText = inputText;
    }
    public LA(){}

    public void CheckAndDivide(String inputText){
        list.clear();
        System.out.println(inputText);

        for (String temp : inputText.split("\n")) {
            list.add(temp);
        }

       LAnalize(list);
    }

    public Lexema[] LAnalize(ArrayList<String> list){

        for(; i < list.size(); i++)
        {
            Enter = list.get(i);
            Enter = Enter.trim();
            len = Enter.length();

            //Разбиваем на лексемы
            while(k < len){
                if(Enter.charAt(k)==' ')
                    k++;
                State1(Enter.charAt(k));
                lex.add(temp);
                //System.out.println("FixLex " + temp);
                if(temp.equals(lastSymbol)) {
                    flagOfTheEnd = true;
                    break;
                }

                temp = "";
                k++;

                /*if(flag == false)
                {
                   return new Lexema[0];
                }*/
            }

            k=0;
            lineNum.add(lex.size());
            if(flagOfTheEnd) break;
        }

        // массив лексем, куда записываются данные
        A = new Lexema[lex.size()];


        int p = 0;
        //записываем значения номера рядка, название лексемы и кода символа
        for(int i = 0; i < lex.size(); i++)
        {
            Lexema B = new Lexema();
            if(i == lineNum.get(p))
            {
                p++;
            }

            B.setLine(p+1);
            B.setLexem(lex.get(i));
            B.setCode(InitCode(lex.get(i)));
            A[i] = B;
        }

        //Обробка идентификаторов
        IDN = isIDN(TRNt);
        Idn = new Ident[IDN.size()];
        for(int i=0; i < IDN.size(); i++)
        {
            Ident IdnB = new Ident();
            IdnB.setType(typeIDN.get(i));
            IdnB.setIdn(IDN.get(i));
            IdnB.setNum(i);
            Idn[i] = IdnB;
        }

        //Обробка констант
        CON = isCon(CONt);
        Con = new Const[CON.size()];
        for(int i=0; i<CON.size(); i++)
        {
            Const ConB = new Const();
            ConB.setCons(CON.get(i));
            ConB.setNum(i);
            Con[i] = ConB;
        }

        //Обробка меток
        LAB = isLab(LABt);
        Lab = new Label[LAB.size()];
        for(int i=0; i < LAB.size(); i++)
        {
            Label LabB = new Label();
            LabB.setLab(LAB.get(i));
            LabB.setNum(i);
            Lab[i] = LabB;
        }

        //закидываем в класс лексем идентификаторы
        for(int i=0; i < lex.size(); i++)
        {
            for(int j=0; j < Idn.length; j++)
            {
                if(A[i].getLexem().equals(Idn[j].getIdn()))
                {
                    A[i].setSecondCode(Idn[j].getNum());
                    A[i].setCode(TABLE.length - 2);
                }
            }
        }

        //закидываем в класс лексем const
        for(int i=0; i < lex.size(); i++)
        {
            for(int j=0; j<Con.length; j++)
            {
                if(A[i].getLexem().equals(Con[j].getCons()))
                {
                    A[i].setSecondCode(Con[j].getNum());
                    A[i].setCode(TABLE.length - 1);
                }
            }
        }

        //закидываем в класс лексем label
        for(int i=0; i < lex.size(); i++)
        {
            for(int j=0; j< Lab.length; j++)
            {
                if(A[i].getLexem().equals(Lab[j].getLab()))
                {
                    A[i].setSecondCode(Lab[j].getNum());
                    A[i].setCode(TABLE.length);
                }
            }
        }

        return A;
    }

    //Первичное состояние
    public void State1(char s1)
    {
        if(s1=='@')
        {
            temp+=s1;
            if(k < len-1 && Character.isLetter(Enter.charAt(k+1))){
                k++;
                char s2 = Enter.charAt(k);
                State6(s2);
            }
            else
            {
                this.addInSetWithLAException("Line "+ (i+1) + ": Mistake in label.");
                flag=false;
            }
        }
        else
        if(Character.isDigit(s1))
        {
            temp+=s1;
            if(k < len-1){
                k++;
                char s2=Enter.charAt(k);
                State2(s2);
            }
            else{
                CONt.add(temp);
            }
        }
        else
        if (Character.isLetter(s1))
        {
            temp+=s1;
            if(k<len-1){
                k++;
                char s2=Enter.charAt(k);
                State3(s2);
            }
            else{
                TRNt.add(temp);
            }
        }
        else
        if(s1=='-')
        {
            temp+=s1;
            if(k < len-1 && Character.isDigit(Enter.charAt(k+1))){
                k++;
                char s2=Enter.charAt(k);
                State2(s2);
            }
        }
        else
       if(s1=='=')
        {
            temp+=s1;
            if(k<len-1){
                k++;
                char s2=Enter.charAt(k);
                State4(s2);
            }
        }
        else
        if(s1=='<'||s1=='>')
        {
            if(k<len-1){
                State9(s1);
            }
            else temp += s1;
        }
        else
        if(s1=='!')
        {
            temp+=s1;
            if(k<len-1){
                k++;
                char s2=Enter.charAt(k);
                State5(s2);
            }
            else
            {
                this.addInSetWithLAException("Line " + (i+1) + ": End of line not can be after '!'.");
                flag=false;
            }
        }
        else
        if(isSeparator(s1))
        {
            temp+=s1;
        }
        else
        {
            this.addInSetWithLAException("Line " + (i+1) + ": Unknown symbol " +  s1 + ".");
            k++;
        }
    }
    //Numbers
    public void State2(char s1)
    {
        if(Character.isDigit(s1))
        {
            temp+=s1;
            if(k < len-1){
                k++;
                char s2 = Enter.charAt(k);
                State2(s2);
            }
            else{
                CONt.add(temp);
            }
        }
        else if (s1 == '.')
        {
            temp+=s1;
            if(k < len-4 && Character.isDigit(Enter.charAt(k+1))){
                k++;
                char s2 = Enter.charAt(k);
                State7(s2);
            }
            else{
                this.addInSetWithLAException("Line "+ (i+1) + ": Mistake in fixed const.");
                flag=false;
            }
        }
        else
            {
            CONt.add(temp);
            k--;
            }
    }
    //Id and fix words
    public void State3(char s1)
    {
        if(Character.isDigit(s1))
        {
            temp+=s1;
            if(k<len-1){
                k++;
                char s2=Enter.charAt(k);
                State3(s2);
            }
            else{
                TRNt.add(temp);
            }
        }
        else
        if (Character.isLetter(s1))
        {
            temp+=s1;
            if(k<len-1){
                k++;
                char s2=Enter.charAt(k);
                State3(s2);
            }
            else{
                TRNt.add(temp);
            }
        }
        else
        {
            TRNt.add(temp);
            k--;
        }
    }
    //For ==
    public void State4(char s1)
    {
        if(s1=='=')
        {
            temp+='=';
        }
        else
            k--;
    }
    //For !=
    public void State5(char s1)
    {
        if(s1=='=')
        {
            temp+=s1;
        }
        else
        {
            this.addInSetWithLAException("Line " + (i + 1) + ": Expected '=' after '!'.");
            flag = false;
        }
    }
    //Label
    public void State6(char s1)
    {
        if(Character.isDigit(s1))
        {
            temp+=s1;
            if(k < len-1){
                k++;
                char s2=Enter.charAt(k);
                State6(s2);
            }
            else{
                LABt.add(temp);
            }
        }
        else
        if (Character.isLetter(s1))
        {
            temp+=s1;
            if(k < len-1){
                k++;
                char s2=Enter.charAt(k);
                State6(s2);
            }
            else{
                LABt.add(temp);
            }
        }
        else
        {
            LABt.add(temp);
            k--;
        }
    }
    //Fix const
    public void State7(char s1) {
        temp += s1;
        if (k < len - 3 && Enter.charAt(k + 1) == 'E') {
            k++;
            char s2 = Enter.charAt(k); // = E
            temp += s2;
            if (k < len - 2 && (Enter.charAt(k + 1) == '+' || Enter.charAt(k + 1) == '-')) {
                k++;
                char s3 = Enter.charAt(k); // = + або -
                temp += s3;
                if (k < len - 1 && Character.isDigit(Enter.charAt(k + 1))) {
                    k++;
                    char s4 = Enter.charAt(k); // должно быть цифрой
                    State8(s4);
                } else {
                    this.addInSetWithLAException("Line " + (i + 1) + ": Mistake in fixed const.");
                    flag = false;
                }
            } else {
                this.addInSetWithLAException("Line " + (i + 1) + ": Mistake in fixed const.");
                flag = false;
            }
        } else {
            this.addInSetWithLAException("Line " + (i + 1) + ": Mistake in fixed const.");
            flag = false;
        }
    }
    //Last digits for F.C.
    public void State8(char s1)
    {
        if(Character.isDigit(s1))
        {
            temp+=s1;
            if(k < len){
                k++;
                char s2 = Enter.charAt(k);
                State8(s2);
            }
            else{
                CONt.add(temp);
            }
        }
        else
        {
            CONt.add(temp);
            k--;
        }
    }
    //For >> or >= or << or <=
    public void State9(char s1)
    {
        if(s1=='>')
        {
            temp+=s1;
            k++;
            char s2=Enter.charAt(k);
            if(s2 == '>' || s2 == '=')
                temp+=s2;
            else
                k--;
        }
        if(s1=='<')
        {
            temp+=s1;
            k++;
            char s2=Enter.charAt(k);
            if(s2 == '<' || s2 == '=')
                temp+=s2;
            else
                k--;
        }
    }
    //проверка на разделители
    public boolean isSeparator(char c)
    {
        boolean tester = false;

        for(int i=0; i < Separators.length; i++)
        {
            if(c == Separators[i])
                tester = true;
        }

        return tester;
    }

    public int InitCode(String A)
    {
        int code = -1;

        for(int i = 0; i < TABLE.length; i++)
        {
            if(A.equals(TABLE[i]))
                code = i;
        }
        return code+1;
    }

    public ArrayList <String> isIDN(ArrayList <String> allTRN)
    {
        ArrayList <String> outputList = new ArrayList<String>();

        for(int i=0; i< allTRN.size(); i++)
        {
            boolean tester=false;

            for(int j=0; j < TRN.length; j++)
            {
                if(TRN[j].equals(allTRN.get(i)))
                {
                    tester = true;
                    break;
                }
            }
            if(tester==false)
            {
                outputList.add(allTRN.get(i)); //это не зарезер слово, выбираем
            }
        }

        ArrayList <String> idnList = new ArrayList<String>();

        // отсеиваем повторы
        for(int i=0; i < outputList.size();i++)
        {
            boolean tester = false;

            for(int j=0;j < i; j++)
            {
                if(outputList.get(i).equals(outputList.get(j)))
                {
                    tester=true;
                    break;
                }
            }
            if(tester==false)
            {
                idnList.add(outputList.get(i)); // это идентиф без повтора, выбираем
            }
        }

        //Проверка токена (нахождение в общем массиве лексем и проверка на существование)
        for (int j = 0; j < idnList.size(); j++) {
            boolean tester = false;
            boolean idnFail = true;
            for (int a = 0; a < lex.size(); a++) {
                if (lex.get(a).equals(idnList.get(j))) {
                    if (a == 0) {
                        typeIDN.add("Unknown");
                        break;
                    }
                    if (lex.get(a - 1).equals("start") || lex.get(a - 1).equals("integer") || lex.get(a - 1).equals("сonst")) {
                        idnFail = false;
                        typeIDN.add(lex.get(a - 1));
                    } else {
                        typeIDN.add("Unknown");
                    }
                    tester = true;
                }
                if (tester) break;
            }

            if (idnFail) {
                int numline = 0;
                for (int l = 0; l < A.length ; l++) {
                    if (idnList.get(j).equals(A[l].getLexem())){
                        numline = A[l].getLine();
                    }
                }
                this.addInSetWithLAException("Line " + numline + ": Unknown type for idn '" + idnList.get(j) + "'.");
            }
        }

        return idnList;
    }

    public ArrayList <String> isCon(ArrayList <String> allCon)
    {
        ArrayList <String> outputList = new ArrayList<String>();

        //проверка на повторы
        for(int i=0; i < allCon.size(); i++)
        {
            boolean tester = false;

            for(int j=0; j<i; j++)
            {
                if(allCon.get(i).equals(allCon.get(j)))
                {
                    tester = true;
                    break;
                }
            }
            if(tester==false)
            {
                outputList.add(allCon.get(i));
            }
        }

        return outputList;
    }

    public ArrayList <String> isLab(ArrayList <String> allLab)
    {
        ArrayList <String> outputList = new ArrayList<String>();

        //проверка на повторы
        for(int i=0; i < allLab.size(); i++)
        {
            boolean tester = false;

            for(int j=0; j<i; j++)
            {
                if(allLab.get(i).equals(allLab.get(j)))
                {
                    tester = true;
                    break;
                }
            }
            if(tester==false)
            {
                outputList.add(allLab.get(i));
            }
        }

        return outputList;
    }

    public void ShowTable(){
        System.out.println("\n   Вихідна таблиця лексем");
        System.out.println("№рядка |   Підрядок   |   Код   | Індекс idn/con/lab");
        for (int j = 0; j <  A.length; j++) {
            if(A[j].getSecondCode() == 0) System.out.printf( "%-7d | %-12s | %-7d \n", A[j].getLine(),A[j].getLexem(), A[j].getCode());
            else System.out.printf( "%-7d | %-12s | %-7d | %-12s\n", A[j].getLine(),A[j].getLexem(), A[j].getCode(), A[j].getSecondCode());
        }

        System.out.println("\n   Таблиця ідентифікаторів");
        System.out.println("Ідентифікатор | Індекс | Тип");
        for (int j = 0; j < Idn.length; j++) {
            System.out.printf( "%-13s | %-6d | %-12s\n", Idn[j].getIdn(),Idn[j].getNum(), Idn[j].getType());
        }

        System.out.println("\n   Таблиця констант");
        System.out.println("Константа  | Індекс");
        for (int j = 0; j < Con.length; j++) {
            System.out.printf( "%-10s | %-6d \n", Con[j].getCons(),Con[j].getNum() );
        }

        System.out.println("\n   Таблиця міток");
        System.out.println("Мітка      | Індекс");
        for (int j = 0; j < Lab.length; j++) {
            System.out.printf( "%-10s | %-6d \n", Lab[j].getLab(),Lab[j].getNum() );
        }
    }
    public void PrintTable(){
        File file = new File("ExitFile.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pw.println("\n   Вихідна таблиця лексем");
        pw.println("№рядка    Підрядок      Код    Індекс idn/con/lab");
        for (int j = 0; j <  A.length; j++) {
            if(A[j].getSecondCode() == 0) pw.printf( "%-7d | %-12s | %-7d \n", A[j].getLine(),A[j].getLexem(), A[j].getCode());
            else pw.printf( "%-7d | %-12s | %-7d | %-12s\n", A[j].getLine(),A[j].getLexem(), A[j].getCode(), A[j].getSecondCode());
        }

        pw.println("\n   Таблиця ідентифікаторів");
        pw.println("Ідентифікатор | Індекс | Тип");
        for (int j = 0; j < Idn.length; j++) {
            pw.printf( "%-13s | %-6d | %-12s\n", Idn[j].getIdn(),Idn[j].getNum(), Idn[j].getType());
        }

        pw.println("\n   Таблиця констант");
        pw.println("Константа  | Індекс");
        for (int j = 0; j < Con.length; j++) {
            pw.printf( "%-10s | %-6d \n", Con[j].getCons(),Con[j].getNum() );
        }

        pw.println("\n   Таблиця міток");
        pw.println("Мітка      | Індекс");
        for (int j = 0; j < Lab.length; j++) {
            pw.printf( "%-10s | %-6d \n", Lab[j].getLab(),Lab[j].getNum() );
        }

        pw.close();
    }
    public void PrintCSVTable(){
        File file = new File("ExitTable.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pw.println("\nExit table with lex");
        for (int j = 0; j < A.length; j++) {
            if(A[j].getSecondCode() == 0) pw.println(A[j].getLine() + ";" + A[j].getLexem() + ";" + A[j].getCode() + ";");
            else pw.println(A[j].getLine() + ";" + A[j].getLexem() + ";" + A[j].getCode() + ";" + A[j].getSecondCode());
        }


        pw.println("\nTable with idn");
        for (int j = 0; j < Idn.length; j++) {
            pw.println(Idn[j].getIdn() + ";" + Idn[j].getNum() + ";" + Idn[j].getType());
        }

        pw.println("\nTable with con");
        for (int j = 0; j < Con.length; j++) {
            pw.println(Con[j].getCons() + ";" + Con[j].getNum() );
        }

        pw.println("\nTable with lab");
        for (int j = 0; j < Lab.length; j++) {
            pw.println(Lab[j].getLab() + ";" + Lab[j].getNum() );
        }

        pw.close();
    }

    public Lexema[] getLexemMass(){
        return A;
    }
    public int getLexemMassSize(){
        return A.length;
    }
    public 	Ident[] getIdentMass()
    {
        return Idn;
    }
    public int getIdentMassSize(){
        return Idn.length;
    }
    public 	Const[] getConstMass()
    {
        return Con;
    }
    public int getConstMassSize(){
        return Con.length;
    }
    public Label[] getLabMass() {
        return Lab;
    }
    public int getLabMassSize(){
        return Lab.length;
    }
    public ArrayList<String> getSetWithLAException() {
        return SetWithLAException;
    }
    public void addInSetWithLAException(String newException) {
        SetWithLAException.add(newException);
    }
}