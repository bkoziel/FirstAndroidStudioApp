package com.example.wastic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String database_name="WasticDB";
    public static final String database_products = "Product";

    public DatabaseHelper(@Nullable Context context) {
        super(context, database_name, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + database_products + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, BARCODE TEXT, PHOTOURL TEXT)");
   }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + database_products);
        onCreate(db);
    }

    public boolean writeDataProduct(String name, String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        cv.put("Barcode" , barcode);
        cv.put("PhotoURL","");
        if(db.insert(database_products, null, cv)==-1)
            return false;
        return true;
    }


boolean productExists(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + database_products + " WHERE BARCODE = ?",new String[]{code});
        if(cursor.getCount() > 0) {
            return true;
        }else{
            return false;
        }
    }
String getProductName(String code){
    SQLiteDatabase db = this.getWritableDatabase();
    SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + database_products + " WHERE BARCODE = '" + code +"'",null);
        return cursor.getString(1);
}


    public SQLiteCursor readDataProduct(){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + database_products,null);
        return cursor;
    }

}
