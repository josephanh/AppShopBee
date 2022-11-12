package nta.nguyenanh.code_application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {
    static final String DBName = "History";
    static final int Dbversion = 1;

    public database(Context context){
        super(context,DBName,null,Dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String History_table = "create table History(" +
                "id_history integer PRIMARY KEY AUTOINCREMENT," +
                "name_history Text not null)";
        db.execSQL(History_table);

        db.execSQL("insert into History (name_history) values ('s22 ultra');");
        db.execSQL("insert into History (name_history) values ('iphone 14');");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableHistory ="drop table if exists History";
        db.execSQL(dropTableHistory);
        onCreate(db);
    }
}
