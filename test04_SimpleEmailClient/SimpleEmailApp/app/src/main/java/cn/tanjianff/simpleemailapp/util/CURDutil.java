package cn.tanjianff.simpleemailapp.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.tanjianff.simpleemailapp.dbEntity.accountRec;


/**
 * Created by tanjian on 16/10/27.
 * 操作SQLite数据库类
 */

public class CURDutil {
    /*
     *   使用此方法需要从Activity中传入应用上下文context*
     */
    private Context context;
    private SQLiteDatabase db;

    public CURDutil(Context context) {
        this.context = context;
        dbOpenHelper dbHelper = new dbOpenHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public void add(List<accountRec> emailaccount) {
        db.beginTransaction();//开始事务
        try {
            for (accountRec item : emailaccount) {
                db.execSQL("INSERT INTO eamilAccount VALUES (null,?,?)"
                        , new Object[]{item.getAccount(), item.getPasswd()});
            }
            db.setTransactionSuccessful();//设置事务成功完成
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG);//通知显示异常
        } finally {
            db.endTransaction();//结束事务
            Toast.makeText(context,"添加成功",Toast.LENGTH_LONG);
        }
    }

    public void addAccount(accountRec account){
        db.beginTransaction();//开始事务
        try{

        }catch (Exception e){
            db.execSQL("INSERT INTO eamilAccount VALUES (null,?,?)"
                    , new Object[]{account.getAccount(), account.getPasswd()});
            db.setTransactionSuccessful();//设置事务成功完成
        }finally {
            db.endTransaction();//结束事务
            Toast.makeText(context,"添加成功",Toast.LENGTH_LONG);
        }
    }

    public List<accountRec> query() {
        ArrayList<accountRec> accountRecords= new ArrayList<accountRec>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            accountRec accountrec=new accountRec();
            accountrec.setAccount(c.getString(c.getColumnIndex("account")));
            accountrec.setPasswd(c.getString(c.getColumnIndex("passwd")));
            accountRecords.add(accountrec);
        }
        c.close();
        return accountRecords;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM eamilAccount", null);
        return c;
    }

    //释放数据库资源
    public void closeDB() {
        db.close();
    }
}
