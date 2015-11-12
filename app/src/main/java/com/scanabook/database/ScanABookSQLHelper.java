package com.scanabook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kshitiz on 11/8/15.
 */
public class ScanABookSQLHelper extends SQLiteOpenHelper {

    public static final String TABLE_BOOKS = "Books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ISBN = "isbn";
    public static final String COLUMN_BOOK_NAME="bookname";
    public static final String COLUMN_BOOK_AUTHOR="author";
    public static final String COLUMN_BOOK_PAGES="totalpages";
    public static final String COLUMN_BOOK_READ="bookread";
    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_BOOKS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_ISBN
            + " text not null, " + COLUMN_BOOK_NAME
            + " text not null, " + COLUMN_BOOK_AUTHOR
            + " text not null, " + COLUMN_BOOK_PAGES
            + " integer not null, " +COLUMN_BOOK_READ
            + " boolean);";


    public ScanABookSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }
}
