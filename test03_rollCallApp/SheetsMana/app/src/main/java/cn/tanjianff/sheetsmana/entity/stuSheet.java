package cn.tanjianff.sheetsmana.entity;

import cn.tanjianff.sheetsmana.util.ImagBiStorage;

/**
 * Created by tanjian on 16/10/27.
 * 花名册的实体
 */

public class stuSheet {
    private String ID;
    private byte[] icon;//头像
    private String std_id;//学号
    private String std_name;//姓名
    private String std_className;//班级
    private String caseSelection;//考勤

    public stuSheet() {

    }

    /*注意:传入图像时应该传入将Bitmap装换成byte字节的自己流*/
    public stuSheet(String id, byte[] icon, String std_id, String std_name, String std_className, String caseSelection) {
        this.ID = id;
        this.icon = icon;
        this.std_id = std_id;
        this.std_name = std_name;
        this.std_className = std_className;
        this.caseSelection = caseSelection;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public String getID() {
        return this.ID;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
    }

    public void setStd_className(String std_className) {
        this.std_className = std_className;
    }

    public void setCaseSelection(String caseSelection) {
        this.caseSelection = caseSelection;
    }

    public byte[] getIcon() {
        return icon;
    }

    public String getIconString(){
       return ImagBiStorage.byte2String(getIcon());
    }

    public String getStd_id() {
        return std_id;
    }

    public String getStd_name() {
        return std_name;
    }

    public String getStd_className() {
        return std_className;
    }

    public String getCaseSelection() {
        return caseSelection;
    }
}
