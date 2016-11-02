package cn.tanjianff.sheetsmana;

/**
 * Created by tanjian on 16/10/27.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Excel {

    /**
     * 导出数据为XLS格式
     *
     * @param fileName 文件的名称，可以设为绝对路径，也可以设为相对路径
     * @param content  数据的内容
     */
    public static void exportExcel(String fileName, Vector<Person> content) {
        WritableWorkbook wwb;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
            wwb = Workbook.createWorkbook(fos);
            WritableSheet ws = wwb.createSheet("三国志武将列表", 10);        // 创建一个工作表

            //    设置单元格的文字格式
            WritableFont wf = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLUE);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf.setAlignment(Alignment.CENTRE);
            ws.setRowView(1, 500);

            //    填充数据的内容
            Person[] p = new Person[content.size()];
            for (int i = 0; i < content.size(); i++) {
                p[i] = (Person) content.get(i);
                ws.addCell(new Label(1, i + 1, p[i].getName(), wcf));
                ws.addCell(new Label(2, i + 1, p[i].getNickname(), wcf));
                ws.addCell(new Label(3, i + 1, p[i].getPower(), wcf));
                ws.addCell(new Label(4, i + 1, p[i].getWit(), wcf));
                ws.addCell(new Label(5, i + 1, p[i].getPolity(), wcf));
                ws.addCell(new Label(6, i + 1, p[i].getCharm(), wcf));
                ws.addCell(new Label(7, i + 1, p[i].getStory(), wcf));
                if (i == 0)
                    wcf = new WritableCellFormat();
            }

            wwb.write();
            wwb.close();
        } catch (IOException e) {
        } catch (RowsExceededException e) {
        } catch (WriteException e) {
        }
    }

    /**
     * 从Excel文件里读取数据保存到Vector里
     *
     * @param fileName Excel文件的名称
     * @return Vector对象, 里面包含从Excel文件里获取到的数据
     */
    public static Vector<Person> importExcel(String fileName) {
        Vector<Person> v = new Vector<Person>();
        try {
            Workbook book = Workbook.getWorkbook(new File(fileName));
            Sheet sheet = book.getSheet(0);        // 获得第一个工作表对象
            int rows = sheet.getRows();

            for (int i = 0; i < rows; i++) {
                Cell[] cell = sheet.getRow(i);
                if (cell.length == 0)
                    continue;

                Person p = new Person();
                p.setName(sheet.getCell(1, i).getContents());
                p.setNickname(sheet.getCell(2, i).getContents());
                p.setPower(sheet.getCell(3, i).getContents());
                p.setWit(sheet.getCell(4, i).getContents());
                p.setPolity(sheet.getCell(5, i).getContents());
                p.setCharm(sheet.getCell(6, i).getContents());
                p.setStory(sheet.getCell(7, i).getContents());
                v.add(p);
            }

            book.close();
        } catch (Exception e) {
        }
        return v;
    }

    public static void main(String[] args) {
        String fileName = "test.xls";
        String fileNameNew = "testNew.xls";

        Person p0 = new Person("姓名", "字", "武力", "智力", "政治", "魅力", "英雄事迹");
        Person p1 = new Person("赵云", "子龙", "98", "84", "83", "87", "单骑救主!!!");
        Person p2 = new Person("马超", "孟起", "98", "62", "40", "88", "杀得曹操割须弃袍!!!");
        Person p3 = new Person("诸葛亮", "孔明", "55", "100", "92", "93", "死后木偶退兵，锦囊杀魏延!!!");

        Vector<Person> v = new Vector<Person>();
        v.add(p0);
        v.add(p1);
        v.add(p2);
        v.add(p3);

        exportExcel(fileName, v);
        System.out.println("成功导出数据到Excel文件(" + fileName + ")了!!!");

        Vector<Person> vector = importExcel(fileName);
        System.out.println("成功从Excel文件(" + fileName + ")导入数据!!!");

        exportExcel(fileNameNew, vector);
        System.out.println("成功将" + fileName + "里的数据手复制到" + fileNameNew + "中!!!");
    }
}
