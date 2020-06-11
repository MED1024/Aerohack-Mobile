package com.example.probamap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="PersonalInfo";
    private static int DB_VER=1;

    public static final String DB_TABLE="Task";
    public static final String DB_TABLE1="Personal";
    public static final String DB_COLOMN="TaskName";
    public static final String DB_COLOMN1="NAME";
    public static final String DB_COLOMN2="EMAIL";
    public static final String DB_COLOMN3="PHONE";
    public static final String DB_COLOMN4="ZONE";
    public static final String DB_COLOMN5="SPEC";
    public static final String DB_COLOMN6="STATUS";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL)",DB_TABLE,DB_COLOMN);
        String query1= String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL)",DB_TABLE1,DB_COLOMN1,DB_COLOMN2,DB_COLOMN3,DB_COLOMN4,DB_COLOMN5,DB_COLOMN6);
        db.execSQL(query);
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXIST %s",DB_TABLE);
        String query1 = String.format("DELETE TABLE IF EXIST %s",DB_TABLE1);
        db.execSQL(query);
        db.execSQL(query1);
        onCreate(db);

    }

    public void insertNewTask(String task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DB_COLOMN,task);

        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        DB_VER=DB_VER+1;
    }

    public void insertPersonal(String fio,String email,String phone, String zone, String spec){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        String stat="No";
        values.put(DB_COLOMN1,fio);
        values.put(DB_COLOMN2,email);
        values.put(DB_COLOMN3,phone);
        values.put(DB_COLOMN4,zone);
        values.put(DB_COLOMN5,spec);
        values.put(DB_COLOMN6,stat);

        db.insertWithOnConflict(DB_TABLE1,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        DB_VER=DB_VER+1;
    }

    public void deleteTask(String task){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLOMN+ " = ?",new String[]{task});
        db.close();
    }

    public ArrayList<String> getTaskList() {
        ArrayList<String> taskList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(DB_TABLE,new String[]{DB_COLOMN},null,null,null,null,null);
        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DB_COLOMN);
            taskList.add(cursor.getString(index));
        }
            cursor.close();
            db.close();
            return taskList;
    }
}
