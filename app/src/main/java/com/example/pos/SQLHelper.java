package com.example.pos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "categoryDB";
    private  static final String TBL_CAT = "tableCategory";
    private  static final String TBL_1 = "table1";
    private  static  final String TBL_TRANS = "tableTransactions";
    private static final String CAT_COL = "category";
    private static final String NAME_COL = "name";
    private static final String CODE_COL = "code";
    private static final String PRICE_COL = "price";
    private static final String ACC_COL = "accName";
    private static final String DATE_COL = "date";
    private static final String DEBIT_COL = "debit";
    private static final String CREDIT_COL = "credit";
    private  CategoryFragment categoryFragment;



    public SQLHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL("create table tableCategory (category TEXT primary key)");
        sqLiteDatabase.execSQL("create table table1 (category TEXT," +
                " name TEXT, code INTEGER, price REAL," +
                " foreign key (category) references tableCategory(category))");
        sqLiteDatabase.execSQL("create table tableTransactions (accName TEXT, date TEXT," +
                " category TEXT, name TEXT, code INTEGER," +
                " debit REAL, credit REAL)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists tableCategory");
        sqLiteDatabase.execSQL("drop table if exists table1");
        sqLiteDatabase.execSQL("drop table if exists tableTransactions");
    }

    public boolean insertItem(String category, String name, int code, double price){
        long result = 0;
        try(SQLiteDatabase database = this.getWritableDatabase()){

            ContentValues values = new ContentValues();
            values.put(CAT_COL, category);
            values.put(NAME_COL, name);
            values.put(CODE_COL, code);
            values.put(PRICE_COL, price);

            result = database.insert(TBL_1, null, values);
        }catch (SQLException e){

            e.printStackTrace();
        }

        return result != -1;
    }

    public boolean recordTransaction(String accName, String date,
                                     String category, String name, int code,
                                     Double debit, Double credit){
        long result = 0;
        try(SQLiteDatabase database = this.getWritableDatabase()){

            ContentValues values = new ContentValues();
            values.put(ACC_COL, accName);
            values.put(DATE_COL, date);
            values.put(CAT_COL, category);
            values.put(NAME_COL, name);
            values.put(CODE_COL, code);
            values.put(DEBIT_COL, debit);
            values.put(CREDIT_COL, credit);

            result = database.insert(TBL_TRANS, null, values);
        }catch (SQLException e){

            e.printStackTrace();
        }

        return result != -1;
    }

    public List<String> getAllCategories() {
        List<String> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tableCategory", null);
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                dataList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }

    public List<Item> specificItems(String category){

        ArrayList<Item> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                "category",
                "name",
                "code",
                "price"
        };

        String selection = "category = ?";
        String[] selectionArgs = { String.valueOf(category) };
        Cursor cursor = db.query(TBL_1, projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String strCategory = cursor.getString(cursor.getColumnIndexOrThrow(CAT_COL));
            String strName = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COL));
            int intCode = cursor.getInt(cursor.getColumnIndexOrThrow(CODE_COL));
            double dblPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE_COL));
            Item item = new Item(strCategory, strName, intCode, dblPrice);
            items.add(item);
        }
        cursor.close();

        return items;
    }

    public ArrayList<SSRecords> getDataFromDatabase() {
        ArrayList<SSRecords> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tableTransactions", null);
        if (cursor.moveToFirst()) {
            do {

                String account = cursor.getString(cursor.getColumnIndexOrThrow(ACC_COL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE_COL));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(CAT_COL));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COL));
                int code = cursor.getInt(cursor.getColumnIndexOrThrow(CODE_COL));
                Double debit = cursor.getDouble(cursor.getColumnIndexOrThrow(DEBIT_COL));
                Double credit = cursor.getDouble(cursor.getColumnIndexOrThrow(CREDIT_COL));
                SSRecords record = new SSRecords(account, date, category, name, code, debit, credit);
                data.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }



    public Item selectedItem(String itemName){

        Item item = null;
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                "category",
                "name",
                "code",
                "price"
        };

        String selection = "name = ?";
        String[] selectionArgs = { String.valueOf(itemName) };
        Cursor cursor = null;
                // The row exists in the database

        cursor = db.query(TBL_1, projection, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
                System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                String strCategory = cursor.getString(cursor.getColumnIndexOrThrow(CAT_COL));
                String strName = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COL));
                int intCode = cursor.getInt(cursor.getColumnIndexOrThrow(CODE_COL));
                double dblPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE_COL));
                item = new Item(strCategory, strName, intCode, dblPrice);
                cursor.close();
        } else {
            // The row does not exist in the database
            System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            item = new Item(null, null, 0, 0.0);
        }
        return item;
    }



    public boolean insertCategory(String category){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAT_COL, category);
        long result = database.insert(TBL_CAT, null, values);
        return result != -1;
    }

    public boolean checkCategory(String category){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from tableCategory where category=?", new String[] {category});
        return cursor.getCount() > 0;
    }


}
