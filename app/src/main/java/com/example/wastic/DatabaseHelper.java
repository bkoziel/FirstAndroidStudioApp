package com.example.wastic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String database_name="WasticDB";
    public static final String database_users ="Users";
    public static final String database_products = "Products";

    public DatabaseHelper(@Nullable Context context) {
        super(context, database_name, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + database_users + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, LOGIN TEXT, PASSWORD TEXT, EMAIL TEXT, PERMISSION INTEGER )");
        db.execSQL("create table " + database_products + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, BARCODE TEXT,RATE REAL, TYPE TEXT, PHOTOURL TEXT)");
       // db.execSQL("create table " + database_users + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, LOGIN TEXT, PASSWORD TEXT, EMAIL TEXT, PERMISSION INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + database_users);
        db.execSQL("DROP TABLE IF EXISTS " + database_products);
        onCreate(db);
    }

    public boolean writeDataUser(String login, String password, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Login",login);
        cv.put("Password",password);
        cv.put("Email" , email);
        cv.put("Permission",1);
        if(db.insert(database_users, null, cv)==-1)
            return false;
        return true;
    }

    public boolean writeDataProduct(String name, String barcode, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        cv.put("Barcode" , barcode);
        cv.put("Rate", 0.0);
        cv.put("Type" , type );
        cv.put("PhotoURL","");
        if(db.insert(database_products, null, cv)==-1)
            return false;
        return true;
    }


    public SQLiteCursor readDataUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + database_users,null);
        return cursor;
    }

boolean productExists(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + database_products + " WHERE BARCODE = " + code,null);
        if(cursor.getCount() == 0) {
            return false;
        }else{
            return true;
        }
    }


    public SQLiteCursor readDataProduct(){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + database_products,null);
        return cursor;
    }

}
