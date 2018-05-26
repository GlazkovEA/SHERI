package com.homounikumus1.sheri;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Homo__000 on 21.02.2018.
 */

public class MyDataBase extends SQLiteOpenHelper {
    private static final String NAME = "dishes";
    private static final int VERSION = 1;

    public MyDataBase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE DATA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "ADDRESS TEXT, "
                + "LATLNG TEXT, "
                + "COST TEXT, "
                + "DISH_MARK DOUBLE, "
                + "GENERAL_MARK DOUBLE, "
                + "IMAGE STRING)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
