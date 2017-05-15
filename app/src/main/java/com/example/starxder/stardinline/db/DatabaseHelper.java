package com.example.starxder.stardinline.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/25.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String UserTbale = "create table Book(id integer primary key autoincrement,comment text,description text,loginName text,modifyTime,password text,userName text,groupId integer)";

    private Context mContext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTbale);
        Toast.makeText(mContext, "数据库创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
