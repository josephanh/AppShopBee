package nta.nguyenanh.code_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nta.nguyenanh.code_application.adapter.FlashsaleAdapter;
import nta.nguyenanh.code_application.model.Product;

public class FlashSaleActivity extends AppCompatActivity {
    private String EVENT_DATE_TIME = "2022-11-13 16:30:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    long unixSeconds = 1668388555;
    private LinearLayout ln_flashsale_1, ln_flashsale_2, ln_flashsale_3, ln_flashsale_4;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private Handler handler = new Handler();
    private Runnable runnable;
    RecyclerView rv_Flashsale_now;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FlashsaleAdapter flashsaleAdapter;
    Float sale;
    public static List<Product> Flashsalelist = new ArrayList<>();
    TextView txt_Flashsale_1,txt_Flashsale_2,txt_Flashsale_3,txt_Flashsale_4;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_flash_sale);
        ln_flashsale_1 = findViewById(R.id.ln_flashsale_1);
        ln_flashsale_2 = findViewById(R.id.ln_flashsale_2);
        ln_flashsale_3 = findViewById(R.id.ln_flashsale_3);
        ln_flashsale_4 = findViewById(R.id.ln_flashsale_4);
        txt_Flashsale_1 = findViewById(R.id.txt_TimeFlashsale_1);
        txt_Flashsale_2 = findViewById(R.id.txt_TimeFlashsale_2);
        txt_Flashsale_3 = findViewById(R.id.txt_TimeFlashsale_3);
        txt_Flashsale_4 = findViewById(R.id.txt_TimeFlashsale_4);
        rv_Flashsale_now = findViewById(R.id.rv_Flassale_now);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        tv_second = findViewById(R.id.tv_second);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FlashSaleActivity.this,LinearLayoutManager.VERTICAL,false);
        rv_Flashsale_now.setLayoutManager(linearLayoutManager);
        TimeToCast();
        onGetItemTimeZone();

    }


    public void countDownStart(long time) {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    Date date = new Date(time*1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                    String formattedDate = sdf.format(date);
                    Date event_date = sdf.parse(formattedDate);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
//                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));

                    } else {
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public void onGetItemTimeZone() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        db.collection("product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshots = task.getResult();

                            for (QueryDocumentSnapshot document : snapshots) {
                                sale = Float.parseFloat(document.get("sale").toString());
                                Long start = Long.parseLong(document.get("datestart").toString());
                                Long end = Long.parseLong(document.get("dateend").toString());
                                if (sale > 0 && Integer.parseInt(document.get("total") + "") > 0) {
                                    if (System.currentTimeMillis() >= start * (1000L) && System.currentTimeMillis() < end * (1000L)) {
                                        Log.d("TAG 2000", "Đang sale: " + document.getId());
                                        Log.d("TAG 2000", "sale: " + sale);
                                        ArrayList<String> color = (ArrayList<String>) document.getData().get("color");
                                        ArrayList<String> images = (ArrayList<String>) document.getData().get("image");
                                        Product product = new Product(document.getId(),
                                                document.getData().get("nameproduct").toString(),
                                                document.getData().get("describe").toString(),
                                                Float.parseFloat(document.getData().get("price").toString()),
                                                Integer.parseInt(document.getData().get("available").toString()),
                                                color, images,
                                                Integer.parseInt(document.getData().get("sale").toString()),
                                                Integer.parseInt(document.getData().get("sold").toString()),
                                                Integer.parseInt(document.getData().get("total").toString()),
                                                document.getData().get("id_category").toString(),
                                                Long.parseLong(document.get("datestart").toString()),
                                                Long.parseLong(document.get("dateend").toString()));
                                        Flashsalelist.add(product);

                                    } else if (System.currentTimeMillis() < start * (1000L)) {
                                        Log.d("TAG 2000", "Sắp sale: " + document.getId());
                                        Log.d("TAG 2000", "sale: " + sale);
                                    }
                                }
                            }
                            flashsaleAdapter = new FlashsaleAdapter(FlashSaleActivity.this,Flashsalelist);
                            rv_Flashsale_now.setAdapter(flashsaleAdapter);
                            countDownStart(Flashsalelist.get(1).getDateend());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onReceive(Context context, Intent intent) {
            Date date = new Date();
            int Hours = date.getHours();
            Log.d("TAG FFFF", Hours + "");
            if (Hours >= 9 && Hours < 12) {
                txt_Flashsale_1.setTextColor(Color.parseColor("#F65858"));
                txt_Flashsale_2.setTextColor(R.color.black);
                txt_Flashsale_3.setTextColor(R.color.black);
                txt_Flashsale_4.setTextColor(R.color.black);
                txt_Flashsale_1.setText("9:00\nĐang diễn ra");
                txt_Flashsale_2.setText("12:00\nSắp diễn ra");
                txt_Flashsale_3.setText("15:00\nSắp diễn ra");
                txt_Flashsale_4.setText("18:00\nSắp diễn ra");
                onGetItemTimeZone();

            } else if (Hours >= 12 && Hours < 15) {
                txt_Flashsale_1.setTextColor(R.color.black);
                txt_Flashsale_2.setTextColor(Color.parseColor("#F65858"));
                txt_Flashsale_3.setTextColor(R.color.black);
                txt_Flashsale_4.setTextColor(R.color.black);
                txt_Flashsale_2.setText("12:00\nĐang diễn ra");
                txt_Flashsale_1.setText("9:00\nĐã diễn ra");
                txt_Flashsale_3.setText("15:00\nSắp diễn ra");
                txt_Flashsale_4.setText("18:00\nSắp diễn ra");
                onGetItemTimeZone();

            } else if (Hours >= 15 && Hours < 18) {
                txt_Flashsale_3.setTextColor(Color.parseColor("#F65858"));
                txt_Flashsale_2.setTextColor(R.color.black);
                txt_Flashsale_1.setTextColor(R.color.black);
                txt_Flashsale_4.setTextColor(R.color.black);
                txt_Flashsale_3.setText("15:00\nĐang diễn ra");
                txt_Flashsale_1.setText("9:00\nĐã diễn ra");
                txt_Flashsale_2.setText("12:00\nĐã diễn ra");
                txt_Flashsale_4.setText("18:00\nSắp diễn ra");
                onGetItemTimeZone();

            } else if (Hours >= 18 && Hours < 21) {
                txt_Flashsale_4.setTextColor(Color.parseColor("#F65858"));
                txt_Flashsale_2.setTextColor(R.color.black);
                txt_Flashsale_3.setTextColor(R.color.black);
                txt_Flashsale_1.setTextColor(R.color.black);
                txt_Flashsale_4.setText("18:00\nĐang diễn ra");
                txt_Flashsale_1.setText("9:00\nĐã diễn ra");
                txt_Flashsale_2.setText("12:00\nĐã diễn ra");
                txt_Flashsale_3.setText("15:00\nĐã diễn ra");
                onGetItemTimeZone();

            } else {
                ln_flashsale_1.setVisibility(View.GONE);
                ln_flashsale_2.setVisibility(View.GONE);
                ln_flashsale_3.setVisibility(View.GONE);
                ln_flashsale_4.setVisibility(View.GONE);

            }
            flashsaleAdapter.notifyDataSetChanged();
        }
    };
    @SuppressLint("ResourceAsColor")
    public void TimeToCast(){
        Date date = new Date();
        int Hours = date.getHours();
        Log.d("TAG FFFF", Hours + "");
        if (Hours >= 9 && Hours < 12) {
            txt_Flashsale_1.setTextColor(Color.parseColor("#F65858"));
            txt_Flashsale_2.setTextColor(R.color.black);
            txt_Flashsale_3.setTextColor(R.color.black);
            txt_Flashsale_4.setTextColor(R.color.black);
            txt_Flashsale_1.setText("9:00\nĐang diễn ra");
            txt_Flashsale_2.setText("12:00\nSắp diễn ra");
            txt_Flashsale_3.setText("15:00\nSắp diễn ra");
            txt_Flashsale_4.setText("18:00\nSắp diễn ra");


        } else if (Hours >= 12 && Hours < 15) {
            txt_Flashsale_1.setTextColor(R.color.black);
            txt_Flashsale_2.setTextColor(Color.parseColor("#F65858"));
            txt_Flashsale_3.setTextColor(R.color.black);
            txt_Flashsale_4.setTextColor(R.color.black);
            txt_Flashsale_2.setText("12:00\nĐang diễn ra");
            txt_Flashsale_1.setText("9:00\nĐã diễn ra");
            txt_Flashsale_3.setText("15:00\nSắp diễn ra");
            txt_Flashsale_4.setText("18:00\nSắp diễn ra");


        } else if (Hours >= 15 && Hours < 18) {
            txt_Flashsale_3.setTextColor(Color.parseColor("#F65858"));
            txt_Flashsale_2.setTextColor(R.color.black);
            txt_Flashsale_1.setTextColor(R.color.black);
            txt_Flashsale_4.setTextColor(R.color.black);
            txt_Flashsale_3.setText("15:00\nĐang diễn ra");
            txt_Flashsale_1.setText("9:00\nĐã diễn ra");
            txt_Flashsale_2.setText("12:00\nĐã diễn ra");
            txt_Flashsale_4.setText("18:00\nSắp diễn ra");


        } else if (Hours >= 18 && Hours < 21) {
            txt_Flashsale_4.setTextColor(Color.parseColor("#F65858"));
            txt_Flashsale_2.setTextColor(R.color.black);
            txt_Flashsale_3.setTextColor(R.color.black);
            txt_Flashsale_1.setTextColor(R.color.black);
            txt_Flashsale_4.setText("18:00\nĐang diễn ra");
            txt_Flashsale_1.setText("9:00\nĐã diễn ra");
            txt_Flashsale_2.setText("12:00\nĐã diễn ra");
            txt_Flashsale_3.setText("15:00\nĐã diễn ra");


        } else {
            if (Hours<9){
                txt_Flashsale_1.setText("9:00\nSắp diễn ra");
                txt_Flashsale_2.setText("12:00\nSắp diễn ra");
                txt_Flashsale_3.setText("15:00\nSắp diễn ra");
                txt_Flashsale_4.setText("18:00\nSắp diễn ra");
            }else{
                txt_Flashsale_1.setText("9:00\nĐã diễn ra");
                txt_Flashsale_2.setText("12:00\nĐã diễn ra");
                txt_Flashsale_3.setText("15:00\nĐã diễn ra");
                txt_Flashsale_4.setText("18:00\nĐã diễn ra");
            }


        }
    }
}