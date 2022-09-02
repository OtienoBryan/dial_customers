package com.topline.hub.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by muejacob on 14/12/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context,Tables.Defaults.DATABASE_NAME,null,Tables.Defaults.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tables.TableProduct.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Tables.TableProduct.TABLENAME);
        onCreate(db);
    }

    //Adding data

    public void AddProduct(Product obj){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tables.TableProduct.ID, obj.getId());
        values.put(Tables.TableProduct.NAME, obj.getName());
        values.put(Tables.TableProduct.SKU, obj.getSku());
        values.put(Tables.TableProduct.CATEGORY, obj.getCategory());
        values.put(Tables.TableProduct.PRICE, obj.getPrice());

        db.insert(Tables.TableProduct.TABLENAME, null, values);
        db.close();
    }

    //Getting data

    public ArrayList<String> getProductCategory(){
        ArrayList<String> dataObj=new ArrayList<>();
        String sqlSelect="SELECT DISTINCT "+ Tables.TableProduct.CATEGORY +" FROM "+ Tables.TableProduct.TABLENAME+ " ORDER BY "+ Tables.TableProduct.CATEGORY+ " ASC";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    dataObj.add(cursor.getString(cursor.getColumnIndex(Tables.TableProduct.CATEGORY)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataObj;
    }

    public ArrayList<String> getProducts(String category){
        ArrayList<String> dataObj=new ArrayList<>();
        String sqlSelect="SELECT * FROM "+ Tables.TableProduct.TABLENAME+ " WHERE "+ Tables.TableProduct.CATEGORY +" = '"+category+"' ORDER BY "+ Tables.TableProduct.NAME+ " ASC";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    String result = cursor.getString(cursor.getColumnIndex(Tables.TableProduct.NAME)) + " - " +cursor.getString(cursor.getColumnIndex(Tables.TableProduct.SKU));
                    dataObj.add(result);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataObj;
    }

    public ArrayList<String> getProductIds(String category){
        ArrayList<String> dataObj=new ArrayList<>();
        String sqlSelect="SELECT * FROM "+ Tables.TableProduct.TABLENAME+ " WHERE "+ Tables.TableProduct.CATEGORY +" = '"+category+"' ORDER BY "+ Tables.TableProduct.NAME+ " ASC";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    String result = cursor.getString(cursor.getColumnIndex(Tables.TableProduct.ID));
                    dataObj.add(result);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataObj;
    }

    public String getProductPrice(String product){
        String result = "";
                String sqlSelect="SELECT * FROM "+ Tables.TableProduct.TABLENAME+ " WHERE "+ Tables.TableProduct.ID +" = '"+product+"' ORDER BY "+ Tables.TableProduct.NAME+ " ASC";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sqlSelect, null);
            if (cursor.moveToFirst()) {
                do {
                    result = cursor.getString(cursor.getColumnIndex(Tables.TableProduct.PRICE));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //Deleting Data

    public long DeleteAllRecord(String tableName){
        SQLiteDatabase db=this.getWritableDatabase();
        long result= db.delete(tableName,"1",null);
        db.close();
        return result;
    }

    public void DeleteOneRecordByID(String tableName, String id, String columnName){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(tableName,columnName+"=?",new String[]{id});
        db.close();
    }


    //update data


    public void UpdateOneRecordByID(String tableName, String id, String columnUpdate, String columnID, String value){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(columnUpdate,value);
        db.update(tableName,cv,columnID+" = '"+id+"'",null);
        db.close();
    }

}
