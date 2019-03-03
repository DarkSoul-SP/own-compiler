package sample.logic;

public class Const{
    private int num;
    private String cons;

    public Const(int num, String cons) {
        this.num = num;
        this.cons = cons;
    }

    public Const() {
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = ++num;
    }
}
