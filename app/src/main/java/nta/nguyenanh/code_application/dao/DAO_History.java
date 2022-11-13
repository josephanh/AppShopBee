package nta.nguyenanh.code_application.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nta.nguyenanh.code_application.helper.database;
import nta.nguyenanh.code_application.model.History_model;

public class DAO_History {
    private SQLiteDatabase db;
    database database1;
    public DAO_History(Context context){
        database1 = new database(context);
        db = database1.getWritableDatabase();
    }
    public List<History_model> getAll() {
        String sql = "SELECT * FROM History";
        return getData(sql);
    }
    public List<History_model> getAllid() {
        String sql = "SELECT id_history FROM History";
        return getData(sql);
    }

    public List<History_model> getData(String sql, String ... selectionArgs){
        List<History_model> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            int id  = c.getInt(0);
            String name = c.getString(1);
            History_model obj = new History_model(id, name);
            list.add(obj);
        }
        return list;
    }
    public long insert( String name_history){
        ContentValues values = new ContentValues();
        values.put("name_history",name_history);
        return db.insert("History",null,values);
    }

    public  int delete(String id_history){
        return db.delete("History","id_history=?",new String[]{id_history});
    }
    public  int deleteall(){
        return db.delete("History","1",null);

    }



}