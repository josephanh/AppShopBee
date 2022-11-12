package nta.nguyenanh.code_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DAO_History {
    private SQLiteDatabase db;
    public DAO_History(Context context){
        database dbHelper = new database(context);
        db = dbHelper.getWritableDatabase();
    }
    public static ArrayList<History_model> getAll(Context context){
        database dbHelper;
        dbHelper = new database(context);
        ArrayList<History_model> ds = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT * FROM History";

        Cursor c = db.rawQuery(sql,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            try {
                int id_history = c.getInt(0);
                String name_history = c.getString(1);
                History_model tvm = new History_model(id_history, name_history);
                ds.add(tvm);

            }catch (Exception e){
                e.printStackTrace();
            }
            c.moveToNext();


        }
        return ds;
    }
    public long insert(History_model obj){
        ContentValues values = new ContentValues();
        values.put("id_history",obj.getId_history());
        values.put("name_history",obj.getName_history());
        return db.insert("History",null,values);
    }

}
