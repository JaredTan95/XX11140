package cn.tanjianff.sheetsmana.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.tanjianff.sheetsmana.entity.stuSheet;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
/**
 * Created by tanjian on 16/10/31.
 * 该类实现导入导出Excel的方法
 */

public class jExcel {

    private static ArrayList<stuSheet> resultList;

    public static ArrayList<stuSheet> readExcelTable(String filePath) throws IOException, BiffException {

        //读取test.xls文件
        Workbook book = Workbook.getWorkbook(new File(filePath));

        //获得第一个工作表对象
        Sheet sheet = book.getSheet("sheet_one");
        //Sheet sheet = book.getSheet(0);

        int rows = sheet.getRows();
        int cols = sheet.getColumns();

        //格式化数据,将其与数据库字段对应
       try{
           resultList=new ArrayList<>();
           for (int i = 0; i < rows; i++) {
               stuSheet item=new stuSheet();
               byte[] bytes=new byte[]{};
               int j = 0;
               while (j<cols){
                   item.setID("");
                   item.setIcon(bytes);//图片采用默认的
                   item.setStd_id(sheet.getCell(++j, i).getContents());
                   item.setStd_name(sheet.getCell(++j, i).getContents());
                   item.setStd_className(sheet.getCell(++j, i).getContents());
                   item.setCaseSelection("0,0,0,0,0");
                   j=0;
               }
               System.out.println(item.getStd_id()+" "+item.getStd_name()+" "+item.getStd_className());
               resultList.add(item);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
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
