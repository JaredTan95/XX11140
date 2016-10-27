package cn.tanjianff.sheetsmana.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tanjian on 16/10/27.
 * 数据库初始化及其连接创建等操作
 */

public class dbOpenHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SheetMana.db";

    public dbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,DATABASE_NAME, null, 1);
    }

    public dbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建stuSheet表
        db.execSQL("CREATE TABLE IF NOT EXISTS stuSheet"
                +"(id INTEGER PRIMARY KEY AUTOINCREMENT,icon VARCHAR,std_id VARCHAR NOT NULL,"
                + "std_name VARCHAR NOT NULL,std_className VARCHAR NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE stuSheet ADD COLUMN VARCHAR(12) NULL");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }


}
