package com.fteam.supportapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by N1cK0 on 15/06/06.
 */
public class LocalStorage extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "comsupp.db";
    private static final String ID = "_id";
    private static final String TABLE_USER = "user";
    private static final String USER_NAME = "name";
    private static final String USER_POS = "position";
    private static final String TABLE_CONTACT = "contact";
    private static final String CONTACT_PHONE = "phone";
    private static final String CONTACT_EMAIL = "email";
    private static final String CONTACT_NAME = "nameContact";
    Context context;

    private static final String TABLE_USER_CREATE
            = "CREATE TABLE " + TABLE_USER
            + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_NAME + " VARCHAR(25), "
            + USER_POS + " VARCHAR(25));";

    private static final String TABLE_CONTACT_CREATE
            = "CREATE TABLE " + TABLE_CONTACT
            + " (" + ID + " INTEGER PRIMARY KEY, "
            + CONTACT_NAME + " VARCHAR(25), "
            + CONTACT_EMAIL + " VARCHAR(25), "
            + CONTACT_PHONE + " INTEGER(25));";

    private static final String TABLE_CONTACT_DROP =
            "DROP TABLE IF EXISTS "
                    + TABLE_CONTACT;

    private static final String TABLE_USER_DROP =
            "DROP TABLE IF EXISTS "
                    + TABLE_USER;

    public LocalStorage(Context context){
        super(context,DATABASE_NAME,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CONTACT_CREATE);
        db.execSQL(TABLE_USER_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_CONTACT_DROP);
        db.execSQL(TABLE_USER_DROP);
        onCreate(db);
    }

    public void insertUser(String user){
        long rowID = -1;
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(USER_NAME,user);
            rowID=db.insert(TABLE_USER,null,values);
            db.close();
        } catch (SQLiteException e){
            Log.e("error", e.toString());
        } finally {
            Log.d("LocalStorage ="," "+rowID);
        }
    }

    public void modifyUser(String user){
        long rowID = -1;
        try{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(USER_NAME,user);
            rowID=db.update(TABLE_USER,values,ID+"=1",null);
            db.close();
        } catch (SQLiteException e){
            Log.e ("error",e.toString());
        } finally {
            Log.d("LocalStorage ="," "+rowID);
        }
    }

    public void insertContact(String name,String mail, String phone){
        long rowId = -1;
        try{

            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CONTACT_NAME, name);
            values.put(CONTACT_EMAIL, mail);
            values.put(CONTACT_PHONE, phone);
            rowId = db.insert(TABLE_CONTACT, null, values);
            db.close();
        } catch (SQLiteException e){
            Log.e ("insert()", e.toString());
        } finally {
            Log.d("LocalStorage","insert(): rowId=" + rowId);
        }

    }

    /*public Cursor getContact(String sender, String receiver) {

        SQLiteDatabase db = getWritableDatabase();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_CONTACT + " WHERE " + CONTACT_NAME + " LIKE '" + sender + "' AND " + MESSAGE_RECEIVER + " LIKE '" + receiver + "' OR " + MESSAGE_SENDER + " LIKE '" + receiver + "' AND " + MESSAGE_RECEIVER + " LIKE '" + sender + "' ORDER BY " + ID + " ASC";
        return db.rawQuery(SELECT_QUERY,null);
    }*/

    public Cursor getContact(){
        SQLiteDatabase db = getReadableDatabase();
        String[] values = {CONTACT_NAME,CONTACT_EMAIL,CONTACT_PHONE};
        Cursor c = db.query(TABLE_CONTACT,values,null,null,null,null,null);
        if(c != null) {
            c.moveToFirst();
        }
        db.close();
        c.close();
        return c;
    }

    public String getName(){
        SQLiteDatabase db = getReadableDatabase();
        String[] values = {CONTACT_NAME,CONTACT_EMAIL,CONTACT_PHONE};
        Cursor c = db.query(TABLE_CONTACT,values,null,null,null,null,null);
        if(c != null){
            c.moveToFirst();
            return c.getString(0);
        }else return null;
    }

    public String getMail(){
        SQLiteDatabase db = getReadableDatabase();
        String[] values = {CONTACT_NAME,CONTACT_EMAIL,CONTACT_PHONE};
        Cursor c = db.query(TABLE_CONTACT,values,null,null,null,null,null);
        if(c != null){
            c.moveToFirst();
            return c.getString(1);
        }else return null;
    }
    public String getPhone(){
        SQLiteDatabase db = getReadableDatabase();
        String[] values = {CONTACT_NAME,CONTACT_EMAIL,CONTACT_PHONE};
        Cursor c = db.query(TABLE_CONTACT,values,null,null,null,null,null);
        if(c != null){
            c.moveToFirst();
            return c.getString(2);
        }else return null;
    }
/*
    public void deleteUser(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USER, "_id=1",null);
        db.close();
    }*/
}

