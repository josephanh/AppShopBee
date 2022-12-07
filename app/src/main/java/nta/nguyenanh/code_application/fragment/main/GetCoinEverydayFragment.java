package nta.nguyenanh.code_application.fragment.main;

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
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.helper.database;

public class GetCoinEverydayFragment extends Fragment {

    private String DATE_FORMAT = "yyyyMMdd";
    private CoordinatorLayout btn_getcoin_check;
    private CoordinatorLayout btn_getcoin_check_done;

    private LinearLayout getcoin_day1_check;
    private LinearLayout getcoin_day1_check_done;
    private LinearLayout getcoin_day2_check;
    private LinearLayout getcoin_day2_check_done;
    private LinearLayout getcoin_day3_check;
    private LinearLayout getcoin_day3_check_done;
    private LinearLayout getcoin_day4_check;
    private LinearLayout getcoin_day4_check_done;
    private LinearLayout getcoin_day5_check;
    private LinearLayout getcoin_day5_check_done;
    private LinearLayout getcoin_day6_check;
    private LinearLayout getcoin_day6_check_done;
    private LinearLayout getcoin_day7_check;
    private LinearLayout getcoin_day7_check_done;

    private nta.nguyenanh.code_application.helper.database sql_date;
    private SQLiteDatabase db;
    private String lastday;
    private String today;
    private Integer whatday;
    private ImageView back;


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


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).onBackPressed();
            }
        });


        btn_getcoin_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_test();
                btn_getcoin_check.setVisibility(View.GONE);
                btn_getcoin_check_done.setVisibility(View.VISIBLE);
                db = sql_date.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("_id", 0);
                values.put("date", today);
                values.put("whatday", whatday+1);
                db.update("date_tb", values, "_id=0", null);
                get_date();
                date_test();
            }
        });

    }

    private void date_test(){
        if (Integer.parseInt(today) <= Integer.parseInt(lastday))
        {
            btn_getcoin_check.setVisibility(View.GONE);
            btn_getcoin_check_done.setVisibility(View.VISIBLE);
        }
        else
        {
            btn_getcoin_check.setVisibility(View.VISIBLE);
            btn_getcoin_check_done.setVisibility(View.GONE);
            if (whatday == 7){
                whatday = 0;
                Log.d(">>>>TAG:", "Today: "+ today);
                Log.d(">>>>TAG:", "Lastday: "+lastday+" Whatday: "+whatday);
            }

        }
        if (whatday != null){
                if (whatday > 0){
                    getcoin_day1_check.setVisibility(View.GONE);
                    getcoin_day1_check_done.setVisibility(View.VISIBLE);
                }
                if (whatday > 1){
                    getcoin_day2_check.setVisibility(View.GONE);
                    getcoin_day2_check_done.setVisibility(View.VISIBLE);
                }
                if (whatday > 2){
                    getcoin_day3_check.setVisibility(View.GONE);
                    getcoin_day3_check_done.setVisibility(View.VISIBLE);
                }
                if (whatday > 3){
                    getcoin_day4_check.setVisibility(View.GONE);
                    getcoin_day4_check_done.setVisibility(View.VISIBLE);
                }
                if (whatday > 4){
                    getcoin_day5_check.setVisibility(View.GONE);
                    getcoin_day5_check_done.setVisibility(View.VISIBLE);
                }
                if (whatday > 5){
                    getcoin_day6_check.setVisibility(View.GONE);
                    getcoin_day6_check_done.setVisibility(View.VISIBLE);
                }
                if (whatday > 6){
                    getcoin_day7_check.setVisibility(View.GONE);
                    getcoin_day7_check_done.setVisibility(View.VISIBLE);
                }
        }
    }

    private void get_date(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        today = sdf.format(currentTime);
        Log.d(">>>>TAG:", "Today: "+ today);
        Cursor c = db.rawQuery("select * from date_tb", null);
        if (c.moveToFirst()){
            lastday = c.getString(1);
            whatday = Integer.parseInt(c.getString(2));
            Log.d(">>>>TAG:", "Lastday: "+lastday+" Whatday: "+whatday);
        }
    }

    private void initUI(View view){
        btn_getcoin_check = view.findViewById(R.id.btn_getcoin_check);
        btn_getcoin_check_done = view.findViewById(R.id.btn_getcoin_check_done);


        getcoin_day1_check = view.findViewById(R.id.getcoin_day1_check);
        getcoin_day1_check_done = view.findViewById(R.id.getcoin_day1_check_done);
        getcoin_day2_check = view.findViewById(R.id.getcoin_day2_check);
        getcoin_day2_check_done = view.findViewById(R.id.getcoin_day2_check_done);
        getcoin_day3_check = view.findViewById(R.id.getcoin_day3_check);
        getcoin_day3_check_done = view.findViewById(R.id.getcoin_day3_check_done);
        getcoin_day4_check = view.findViewById(R.id.getcoin_day4_check);
        getcoin_day4_check_done = view.findViewById(R.id.getcoin_day4_check_done);
        getcoin_day5_check = view.findViewById(R.id.getcoin_day5_check);
        getcoin_day5_check_done = view.findViewById(R.id.getcoin_day5_check_done);
        getcoin_day6_check = view.findViewById(R.id.getcoin_day6_check);
        getcoin_day6_check_done = view.findViewById(R.id.getcoin_day6_check_done);
        getcoin_day7_check = view.findViewById(R.id.getcoin_day7_check);
        getcoin_day7_check_done = view.findViewById(R.id.getcoin_day7_check_done);
        back = view.findViewById(R.id.btn_back);



        sql_date = new database(getActivity());
        db = sql_date.getWritableDatabase();


    }
}