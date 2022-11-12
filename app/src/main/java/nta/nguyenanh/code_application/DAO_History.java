package nta.nguyenanh.code_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    public List<History_model> getData(String sql, String ... selectionArgs){
        List<History_model> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            History_model obj = new History_model();
            obj.id_history = c.getInt(0);
            obj.name_history = c.getString(1);
            Log.d(">>>Tag", "onSucces: ");
            list.add(obj);
        }
        return list;
    }
    public long insert( String name_history){
        ContentValues values = new ContentValues();
        values.put("name_history",name_history);
        return db.insert("History",null,values);
    }
}
