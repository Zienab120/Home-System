package com.example.pervasive_projcect;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;


public class DBHelper extends SQLiteOpenHelper {
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    public DBHelper( Context context) {
        super(context, "Users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table Users(email Text primary key,name Text,userName Text,password Text,birthdate Text,profilePicture Text,image BLOB, uri Text)");

        db.execSQL("create table Logs (eventID text primary key, email text  references Users(email) on delete cascade"+
                ",activityName text not null,event text not null,timeStamp text)");

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Users");
    }

    public Boolean insert(String email, String name, String userName, String password, String birthdate, String profilePicture, Bitmap image,String uri){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("email",email);
        contentValues.put("name",name);
        contentValues.put("userName",userName);
        contentValues.put("password",password);
        contentValues.put("birthdate",birthdate);
        contentValues.put("profilePicture",profilePicture);
        if(image!=null){
            byteArrayOutputStream=new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            imageInBytes=byteArrayOutputStream.toByteArray();
            contentValues.put("image",imageInBytes);
            contentValues.put("uri",uri);
        }

        long result=db.insert("Users",null,contentValues);
        db.close();
        if(result==-1)
            return false;
        else
            return true;

    }

    public Boolean insertLog(String eventID,String email,String activityName,String event,String timeStamp){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("eventID",eventID);
        contentValues.put("email",email);
        contentValues.put("activityName",activityName);
        contentValues.put("event",event);
        contentValues.put("timeStamp",timeStamp);
        long result=db.insert("Logs",null,contentValues);
        db.close();
        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor fetchAllLogs(String email)
    {
        SQLiteDatabase db=getReadableDatabase();
        String [] arg={email};
        Cursor cursor=db.rawQuery("select eventID , activityName , event , timeStamp from Logs where email like ?",arg);
        if(cursor != null)
            cursor.moveToFirst();
        db.close();
        return cursor;
    }


    public Boolean update(String email,String name,String userName,String password,String birthdate,String profilePicture){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("name",name);
        contentValues.put("userName",userName);
        contentValues.put("password",password);
        contentValues.put("birthdate",birthdate);
        contentValues.put("profilePicture",profilePicture);

        Cursor cursor=db.rawQuery("Select * from Users where email = ?",new String[]{email});
        if(cursor.getCount()>0) {


            long result = db.update("Users", contentValues, "email=?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public Boolean delete(String email){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select * from Users where email = ?",new String[]{email});
        if(cursor.getCount()>0) {


            long result = db.delete("Users", "email=?", new String[]{email});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public Cursor get(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select * from Users",null);

        return cursor;
    }

    public Boolean getURIFromSql (String email){
        SQLiteDatabase db=this.getWritableDatabase();
        String ur=null;
        Cursor cursor=db.rawQuery("Select * from Users where email = ?",new String[]{email});
        if(cursor.moveToNext()){
            ur=cursor.getString(7);
        }
        if(ur == null)
            return false;
        else
            return true;

    }
    public Bitmap getImageFromSql (String email){
        SQLiteDatabase db=this.getWritableDatabase();
        Bitmap bt=null;
        Cursor cursor=db.rawQuery("Select * from Users where email = ?",new String[]{email});
        if(cursor.moveToNext()){
            byte[] imag=cursor.getBlob(6);
            if(imag.length==0){
              bt=null;
            }else {
                bt= BitmapFactory.decodeByteArray(imag,0,imag.length);

            }
        }
        return bt;

    }

    public Boolean updateUrlInSql(String email,String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("profilePicture", url);

        Cursor cursor = db.rawQuery("Select * from Users where email = ?", new String[]{email});
        if (cursor.getCount() > 0) {


            long result = db.update("Users", contentValues, "email=?", new String[]{email});
            db.close();
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Cursor fetchAllUsers()
    {
        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.rawQuery("Select * from Users",null);
        if(cursor != null)
            cursor.moveToFirst();
        db.close();
        return cursor;
    }

    public String fetchUserName(String email)
    {
        SQLiteDatabase db = getWritableDatabase();
//        String [] arg={email};
        Cursor cursor=db.rawQuery("Select * from users where email = ?",new String[]{email});
        cursor.moveToFirst();
        db.close();
        if (cursor.getCount()>0)
            return cursor.getString(2);
        else
            return "NULL";
    }

    public Boolean checkusernamepassword(String email, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ? and password = ?", new String[] {email,password});
//        MyDB.close();
        if(cursor.getCount()>0)
            return true;
        else
            return false;


    }

}