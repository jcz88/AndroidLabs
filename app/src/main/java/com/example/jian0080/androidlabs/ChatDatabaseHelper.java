package com.example.jian0080.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Messages";
    private static final int VERSION_NUM = 4;
    static final String TABLE_NAME = "MessageTable";
    static final String KEY_ID = "id";
    static final String KEY_MESSAGE = "MESSAGE";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    public void onCreate(SQLiteDatabase db){

        db.execSQL(" CREATE TABLE "+TABLE_NAME+"("+
                    KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    KEY_MESSAGE+" TEXT);");
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        db.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion="+oldVer+" newVersion= "+newVer);
    }

}
