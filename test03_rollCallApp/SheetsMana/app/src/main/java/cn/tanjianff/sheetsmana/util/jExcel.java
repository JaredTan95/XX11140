package cn.tanjianff.sheetsmana.util;


import jxl.*;

import java.io.*;

import jxl.read.biff.BiffException;
import jxl.write.*;
/**
 * Created by tanjian on 16/10/31.
 */

public class jExcel {

    public static String readExcelTable(String filePath) throws IOException, BiffException {

        StringBuffer sb=new StringBuffer();
        //读取test.xls文件
        Workbook book = Workbook.getWorkbook(new File(filePath));

        //获得第一个工作表对象
        Sheet sheet = book.getSheet("sheet_one");
        //Sheet sheet = book.getSheet(0);

        int rows = sheet.getRows();
        int cols = sheet.getColumns();

        System.out.println("总列数：" + cols);
        System.out.println("总行数:" + rows);
        System.out.println("----------------------------");

        int i = 0;
        int j = 0;
        //循环读取数据
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) {
                sb.append(sheet.getCell(j, i).getContents());
            }
        }
        return sb.toString();
    }



    public static void writeExcelTable() throws WriteException {
        WritableWorkbook book;
        try {
            System.out.println("---start---");
            //打开文件
            book = Workbook.createWorkbook(new File("test02.xls"));

            //生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("sheet_one", 0);

            //在Label对象的构造中指名单元格位置是第一列第一行(0,0)
            //以及单元格内容为Hello World
            for(int i=0;i<5;i++){
                //写入5列
                for (int j=0;j<10;j++){
                    //写入10行
                    //Label label = new Label(0, 0, "Hello World");

                    Label label = new Label(i, j, "tanjian");

                    //将定义好的单元格添加到工作表中
                    sheet.addCell(label);
                }
            }



            jxl.write.Number num = new jxl.write.Number(0, 1, 123.456);
            sheet.addCell(num);

            //写入数据并关闭文
            book.write();
            book.close();
            System.out.println("---end---");

        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
