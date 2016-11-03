package cn.tanjianff.sheetsmana.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.tanjianff.sheetsmana.entity.stuSheet;

/**
 * Created by tanjian on 16/10/27.
 * 操作SQLite数据库类
 */

public class CURDutil {
    /*
     *   使用此方法需要从Activity中传入应用上下文context*
     */
    private Context context;
    private dbOpenHelper dbHelper;
    private SQLiteDatabase db;

    public CURDutil(Context context) {
        this.context = context;
        this.dbHelper = new dbOpenHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public void add(List<stuSheet> sheets) {
        db.beginTransaction();//开始事务
        try {
            for (stuSheet item : sheets) {
                db.execSQL("INSERT INTO stuSheet VALUES (null,?,?,?,?,?)"
                        , new Object[]{item.getIcon(), item.getStd_id(), item.getStd_name()
                                ,item.getStd_className(),item.getCaseSelection()});
            }
            db.setTransactionSuccessful();//设置事务成功完成
        } catch (Exception e) {
            //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);//通知显示异常
        } finally {
            db.endTransaction();//结束事务
            //Toast.makeText(context,"添加成功",Toast.LENGTH_LONG);
        }
    }

    public boolean addOneItem(stuSheet student){
        db.beginTransaction();
        boolean isfinished=false;
        try{
            db.execSQL("INSERT INTO stuSheet VALUES (null,?,?,?,?,?)"
                    , new Object[]{student.getIcon(), student.getStd_id(), student.getStd_name()
                            ,student.getStd_className(),student.getCaseSelection()});
            db.setTransactionSuccessful();//设置事务成功完成
            isfinished=true;
        }catch (Exception e){
            isfinished=false;
        }finally {
            db.endTransaction();//结束事务
            return isfinished;
        }
    }

    public void updateIcon(stuSheet student) {
        ContentValues cv = new ContentValues();
        cv.put("icon", student.getIcon());
        db.update("student", cv, "icon=?", new String[]{String.valueOf(student.getIcon())});
    }

    public void updatecaseSelection(){

    }

    public void updatecaseSelection(stuSheet student){
        ContentValues cv = new ContentValues();
        cv.put("case",student.getCaseSelection());
        db.update("student", cv,"caseSelection=?",new String[]{student.getCaseSelection()});
    }
    public List<stuSheet> query() {
        ArrayList<stuSheet> stuSheets = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            stuSheet student = new stuSheet();
            student.setIcon(c.getBlob(c.getColumnIndex("icon")));
            student.setStd_id(c.getString(c.getColumnIndex("std_id")));
            student.setStd_name(c.getString(c.getColumnIndex("std_name")));
            student.setStd_className(c.getString(c.getColumnIndex("std_className")));
            student.setCaseSelection(c.getString(c.getColumnIndex("caseSelection")));
            stuSheets.add(student);
        }
        c.close();
        return stuSheets;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM stuSheet", null);
        return c;
    }

    public List<stuSheet> queryComment(stuSheet std){
        ArrayList<stuSheet> stuSheets=new ArrayList<>();
        Cursor c=db.rawQuery("SELECT * FROM stuSheet WHERE id=?",new String[]{std.getStd_id()});
        while (c.move(0)){
            stuSheet student = new stuSheet();
            ImagBiStorage imagBiStorage=new ImagBiStorage(context);
            byte[] bytes=c.getBlob(c.getColumnIndex("icon"));
            student.setIcon((bytes));
            student.setStd_id(c.getString(c.getColumnIndex("std_id")));
            student.setStd_name(c.getString(c.getColumnIndex("std_name")));
            student.setStd_className(c.getString(c.getColumnIndex("std_className")));
            student.setCaseSelection(c.getString(c.getColumnIndex("caseSelection")));
            stuSheets.add(student);
        }
        c.close();
        return stuSheets;
    }

    //释放数据库资源
    public void closeDB() {
        db.close();
    }
}
