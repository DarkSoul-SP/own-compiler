package sample.logic;

public class Lexema {
    private int line;
    private String lexem;
    private int code;
    private int secondCode;

    public Lexema(){}

    public Lexema(int line, String lexem, int code, int secondCode) {
        this.line = line;
        this.lexem = lexem;
        this.code = code;
        this.secondCode = secondCode;
    }

    public Lexema(int line, String lexem, int code) {
        this.line = line;
        this.lexem = lexem;
        this.code = code;
    }

    @Override
    public String toString() {
        return "Lexema{" +
                "line=" + line +
                ", lexem='" + lexem + '\'' +
                ", code=" + code +
                ", secondCode=" + secondCode +
                '}';
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getLexem() {
        return lexem;
    }

    public void setLexem(String lexem) {
        this.lexem = lexem;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setSecondCode(int secondCode){
        this.secondCode =  secondCode;
    }

    public int getSecondCode() {
        return secondCode;
    }
}

