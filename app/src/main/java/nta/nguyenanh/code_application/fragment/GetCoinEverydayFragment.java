package nta.nguyenanh.code_application.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.sqllite_tin.sql_date;

public class GetCoinEverydayFragment extends Fragment {

    private String DATE_FORMAT = "ddMMyyyy";
    private CoordinatorLayout btn_getcoin_check;
    private CoordinatorLayout btn_getcoin_check_done;
    private LinearLayout getcoin_day1_check;
    private LinearLayout getcoin_day1_check_done;
    private nta.nguyenanh.code_application.sqllite_tin.sql_date sql_date;
    private SQLiteDatabase db;
    private String today;
    private String formattedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_get_coin_everyday, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        get_date();
        date_test();


        btn_getcoin_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getcoin_day1_check.setVisibility(View.GONE);
                getcoin_day1_check_done.setVisibility(View.VISIBLE);
                btn_getcoin_check.setVisibility(View.GONE);
                btn_getcoin_check_done.setVisibility(View.VISIBLE);
                db = sql_date.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("_id", 0);
                values.put("date", formattedDate);
                db.update("date_tb", values, "_id=0", null);
            }
        });

    }

    private void date_test(){
        Cursor c = db.rawQuery("select * from date_tb", null);
        if (c.moveToFirst()){
            today = c.getString(1);
            Log.d(">>>>TAG:", today);
        }

        if (formattedDate.equals(today)){
            getcoin_day1_check.setVisibility(View.GONE);
            getcoin_day1_check_done.setVisibility(View.VISIBLE);
            btn_getcoin_check.setVisibility(View.GONE);
            btn_getcoin_check_done.setVisibility(View.VISIBLE);
        }else {
            btn_getcoin_check.setVisibility(View.VISIBLE);
            btn_getcoin_check_done.setVisibility(View.GONE);

        }
    }

    private void get_date(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        formattedDate = sdf.format(currentTime);
        Log.d(">>>>TAG:", "Today:--"+ formattedDate);

    }

    private void initUI(View view){
        btn_getcoin_check = view.findViewById(R.id.btn_getcoin_check);
        btn_getcoin_check_done = view.findViewById(R.id.btn_getcoin_check_done);
        getcoin_day1_check = view.findViewById(R.id.getcoin_day1_check);
        getcoin_day1_check_done = view.findViewById(R.id.getcoin_day1_check_done);
        sql_date = new sql_date(getActivity());
        db = sql_date.getWritableDatabase();
    }
}