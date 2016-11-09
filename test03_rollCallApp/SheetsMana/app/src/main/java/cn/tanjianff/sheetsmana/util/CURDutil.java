package cn.tanjianff.sheetsmana.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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
                                , item.getStd_className(), item.getCaseSelection()});
            }
            db.setTransactionSuccessful();//设置事务成功完成
        } catch (Exception e) {

        } finally {
            db.endTransaction();//结束事务
        }
    }

    public boolean addOneItem(stuSheet student) {
        db.beginTransaction();
        boolean isfinished = false;
        try {
            db.execSQL("INSERT INTO stuSheet VALUES (null,?,?,?,?,?)"
                    , new Object[]{student.getIcon(), student.getStd_id(), student.getStd_name()
                            , student.getStd_className(), student.getCaseSelection()});
            db.setTransactionSuccessful();//设置事务成功完成
            isfinished = true;
        } catch (Exception e) {
            isfinished = false;
        } finally {
            db.endTransaction();//结束事务
            return isfinished;
        }
    }

    public List<stuSheet> query() {
        ArrayList<stuSheet> stuSheets = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            stuSheet student = new stuSheet();
            student.setID(c.getString(c.getColumnIndex("ID")));
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

    /**
     * 根据ID主键查询学生的记录
     *
     * @Params id, 学生主键
     * @Parama stuSheet, 学生实体对象
     */
    public stuSheet queryById(String id) {
        stuSheet student = new stuSheet();
        Cursor c = db.rawQuery("SELECT * FROM stuSheet where ID=?", new String[]{id});
        while (c.moveToNext()) {
            student.setID(c.getString(c.getColumnIndex("ID")));
            student.setIcon(c.getBlob(c.getColumnIndex("icon")));
            student.setStd_id(c.getString(c.getColumnIndex("std_id")));
            student.setStd_name(c.getString(c.getColumnIndex("std_name")));
            student.setStd_className(c.getString(c.getColumnIndex("std_className")));
            student.setCaseSelection(c.getString(c.getColumnIndex("caseSelection")));
        }
        c.close();
        return student;
    }

    public boolean updateById(stuSheet item) {
        boolean isfinished;
        try {
            /*db.execSQL("UPDATE stuSheet SET icon= ?,std_id=?,std_name=?, std_className=?,caseSelection=? WHERE ID= ?;"
                    , new Object[]{item.getIcon(), item.getStd_id(),
                            item.getStd_name(), item.getStd_className(), item.getCaseSelection(), item.getID()});*/

            db.execSQL("UPDATE stuSheet SET icon='"+ item.getIcon() +"',std_id='"+item.getStd_id()
                    +"',std_name='"+item.getStd_name() +"',caseSelection='"+item.getCaseSelection()+"' WHERE ID= '"+item.getID()+"';");

          /*  ContentValues values = new ContentValues();
            //key为字段名，value为值
            values.put("icon",item.getIcon());
            values.put("std_id",item.getStd_id());
            values.put("std_name",item.getStd_name());
            values.put("std_className",item.getStd_className());
            values.put("caseSelection",item.getCaseSelection());
            //String table, ContentValues values, String whereClause, String[] whereArgs
            db.update("stuSheet", values, "ID=?", new String[]{item.getID()});*/
            isfinished = true;
        } catch (Exception e) {
            isfinished = false;
        }
        return isfinished;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM stuSheet", null);
        return c;
    }

    //释放数据库资源
    public void closeDB() {
        db.close();
    }
}
