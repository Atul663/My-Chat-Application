package com.example.mychat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {
    private static final String dbname = "chatDb1.db";
    private static final String TABLE_NAME = "MyChats"; // Use a constant for table name

    public DBManager(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_NAME + " (id varchar(50) primary Key)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String addData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the id already exists in the table
        Cursor cursor = db.query(TABLE_NAME, null, "id=?", new String[]{id}, null, null, null);
        boolean idExists = cursor.getCount() > 0;
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put("id", id);

        if (idExists) {
            // The id already exists, so update the existing row
            int updatedRows = db.update(TABLE_NAME, cv, "id=?", new String[]{id});
            if (updatedRows > 0) {
                return "Successfully updated";
            } else {
                return "Update failed";
            }
        } else {
            // The id doesn't exist, so insert a new row
            long res = db.insert(TABLE_NAME, null, cv);
            if (res == -1) {
                return "Insert failed";
            } else {
                return "Successfully inserted";
            }
        }
    }
}
