package com.scanabook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kshitiz on 11/8/15.
 */
public class ScanABookDataSource {

    private SQLiteDatabase database;
    private ScanABookSQLHelper dbHelper;
    private String[] allColumns = { ScanABookSQLHelper.COLUMN_ID,
            ScanABookSQLHelper.COLUMN_ISBN,ScanABookSQLHelper.COLUMN_BOOK_NAME,ScanABookSQLHelper.COLUMN_BOOK_AUTHOR,
            ScanABookSQLHelper.COLUMN_BOOK_PAGES,ScanABookSQLHelper.COLUMN_BOOK_READ};

    public ScanABookDataSource(Context context) {
        dbHelper = new ScanABookSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {

        dbHelper.close();
    }
    public long AddABook(BookDetails bookDetails){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScanABookSQLHelper.COLUMN_ISBN,bookDetails.getBookISBN());
        contentValues.put(ScanABookSQLHelper.COLUMN_BOOK_NAME,bookDetails.getBookName());
        contentValues.put(ScanABookSQLHelper.COLUMN_BOOK_AUTHOR,bookDetails.getAuthorName());
        contentValues.put(ScanABookSQLHelper.COLUMN_BOOK_PAGES,bookDetails.getNumOfPages());
        contentValues.put(ScanABookSQLHelper.COLUMN_BOOK_READ,bookDetails.isBookRead());
        long insertId = database.insert(ScanABookSQLHelper.TABLE_BOOKS,null,contentValues);
        return insertId;
    }
    public BookDetails findABook(String bookISBN){
        BookDetails book = null;
        Cursor cursor = database.query(ScanABookSQLHelper.TABLE_BOOKS, allColumns, ScanABookSQLHelper.COLUMN_ISBN + "=" + bookISBN, null, null, null, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            book=new BookDetails();
            book.setBookName(cursor.getString(2));
            book.setAuthorName(cursor.getString(3));
            book.setNumOfPages(cursor.getInt(4));
            book.setBookRead(Boolean.parseBoolean(cursor.getString(5)));
        }
        cursor.close();
        return book;
    }
    public  List<String> getAllBookNames(){
        List<String> allBooks = new ArrayList<>();
        Cursor cursor = database.query(ScanABookSQLHelper.TABLE_BOOKS,allColumns,null,null,null,null,null);
        System.out.println(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String bookName = cursor.getString(2);
            System.out.println(bookName);
            allBooks.add(bookName+","+cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return allBooks;
    }
}
