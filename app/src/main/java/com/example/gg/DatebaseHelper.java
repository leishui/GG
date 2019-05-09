package com.example.gg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatebaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER = "create table user ("
            + "account int, "
            + "password varchar(255))";

    public DatebaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i("1","create a Database1111111111111111111111111111111111111111111111111111111111111111111111111111111");
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
