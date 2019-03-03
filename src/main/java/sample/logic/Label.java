package sample.logic;

public class Label{
    private int num;
    private String lab;

    public Label(int num, String lab) {
        this.num = num;
        this.lab = lab;
    }

    public Label() {
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = ++num;
    }
}
