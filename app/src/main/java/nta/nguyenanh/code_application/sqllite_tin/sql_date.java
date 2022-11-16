package nta.nguyenanh.code_application.sqllite_tin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sql_date extends SQLiteOpenHelper {

    public static String dbname = "sql_date_tin";
    public static int dbvertion = 1;

    public sql_date(@Nullable Context context) {
        super(context, dbname, null, dbvertion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_date_tin = "create table date_tb" +
                "( " +
                "_id integer primary key, " +
                "date text," +
                "whatday integer" +
                ")";
        db.execSQL(sql_date_tin);
        db.execSQL("INSERT INTO date_tb VALUES (0, '19090101', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists date_tb");
        onCreate(db);
    }
}
