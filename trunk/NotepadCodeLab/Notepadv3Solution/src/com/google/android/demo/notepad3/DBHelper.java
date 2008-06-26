package com.google.android.demo.notepad3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper {
    class Row extends Object {
        public String body;
        public long rowId;
        public String title;
    }

    private static final String DATABASE_CREATE =
        "create table todo (rowid integer primary key autoincrement, "
            + "title text not null, body text not null);";

    private static final String DATABASE_NAME = "data";

    private static final String DATABASE_TABLE = "todo";

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public DBHelper(Context ctx) {
        try {
            db = ctx.openDatabase(DATABASE_NAME, null);
        } catch (FileNotFoundException e) {
            try {
                db =
                    ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
                        null);
                db.execSQL(DATABASE_CREATE);
            } catch (FileNotFoundException e1) {
                db = null;
            }
        }
    }

    public void close() {
        db.close();
    }

    public void createRow(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("title", title);
        initialValues.put("body", body);
        db.insert(DATABASE_TABLE, null, initialValues);
    }

    public void deleteRow(long rowId) {
        db.delete(DATABASE_TABLE, "rowid=" + rowId, null);
    }

    public List<Row> fetchAllRows() {
        ArrayList<Row> ret = new ArrayList<Row>();
        try {
            Cursor c =
                db.query(DATABASE_TABLE, new String[] {
                    "rowid", "title", "body"}, null, null, null, null, null);
            int numRows = c.count();
            c.first();
            for (int i = 0; i < numRows; ++i) {
                Row row = new Row();
                row.rowId = c.getLong(0);
                row.title = c.getString(1);
                row.body = c.getString(2);
                ret.add(row);
                c.next();
            }
        } catch (SQLException e) {
            Log.e("booga", e.toString());
        }
        return ret;
    }

    public Row fetchRow(long rowId) {
        Row row = new Row();
        Cursor c =
            db.query(true, DATABASE_TABLE, new String[] {
                "rowid", "title", "body"}, "rowid=" + rowId, null, null,
                null, null);
        if (c.count() > 0) {
            c.first();
            row.rowId = c.getLong(0);
            row.title = c.getString(1);
            row.body = c.getString(2);
            return row;
        } else {
            row.rowId = -1;
            row.body = row.title = null;
        }
        return row;
    }

    public void updateRow(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put("title", title);
        args.put("body", body);
        db.update(DATABASE_TABLE, args, "rowid=" + rowId, null);
    }
}


