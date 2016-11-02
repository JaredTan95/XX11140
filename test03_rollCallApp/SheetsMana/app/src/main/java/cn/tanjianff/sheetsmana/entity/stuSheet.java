package cn.tanjianff.sheetsmana.entity;

/**
 * Created by tanjian on 16/10/27.
 *
 * 花名册的实体
 */

public class stuSheet {
    private String icon;
    private String std_id;
    private String std_name;
    private String std_className;
    private String case1;
    private String case2;
    private String case3;
    private String case4;
    private String case5;
    public stuSheet(){

    }


    public stuSheet(String icon, String std_id, String std_name, String std_className,
                    String case1, String case2, String case3, String case4, String case5) {
        this.icon = icon;
        this.std_id = std_id;
        this.std_name = std_name;
        this.std_className = std_className;
        this.case1 = case1;
        this.case2 = case2;
        this.case3 = case3;
        this.case4 = case4;
        this.case5 = case5;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public String getStd_name() {
        return std_name;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
    }

    public String getStd_className() {
        return std_className;
    }

    public void setStd_className(String std_className) {
        this.std_className = std_className;
    }

    public String getCase1() {
        return case1;
    }

    public void setCase1(String case1) {
        this.case1 = case1;
    }

    public String getCase2() {
        return case2;
    }

    public void setCase2(String case2) {
        this.case2 = case2;
    }

    public String getCase3() {
        return case3;
    }

    public void setCase3(String case3) {
        this.case3 = case3;
    }

    public String getCase4() {
        return case4;
    }

    public void setCase4(String case4) {
        this.case4 = case4;
    }

    public String getCase5() {
        return case5;
    }

    public void setCase5(String case5) {
        this.case5 = case5;
    }

    @Override
    public String toString() {
        return "stuSheet{" +
                "icon='" + icon + '\'' +
                ", std_id='" + std_id + '\'' +
                ", std_name='" + std_name + '\'' +
                ", std_className='" + std_className + '\'' +
                ", case1='" + case1 + '\'' +
                ", case2='" + case2 + '\'' +
                ", case3='" + case3 + '\'' +
                ", case4='" + case4 + '\'' +
                ", case5='" + case5 + '\'' +
                '}';
    }
}
