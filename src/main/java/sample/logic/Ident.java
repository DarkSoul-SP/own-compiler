package sample.logic;

public class Ident{
    private int num;
    private String idn;
    private String type;


    public Ident() {}

    public Ident(int num, String idn, String type) {
        this.num = num;
        this.idn = idn;
        this.type = type;
    }

    public String getIdn() {
        return idn;
    }

    public void setIdn(String idn) {
        this.idn = idn;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = ++num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
