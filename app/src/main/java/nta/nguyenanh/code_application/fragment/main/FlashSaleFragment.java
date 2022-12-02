package nta.nguyenanh.code_application.fragment.main;

import static nta.nguyenanh.code_application.SplashScreen.Flashsalelist;
import static nta.nguyenanh.code_application.SplashScreen.lastVisibleFlashSale;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.BannerAdapter;
import nta.nguyenanh.code_application.adapter.FlashsaleAdapter;
import nta.nguyenanh.code_application.model.Product;

public class FlashSaleFragment extends Fragment {

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

    TextView txt_Flashsale_1,txt_Flashsale_2,txt_Flashsale_3,txt_Flashsale_4;
    ViewPager banner;
    private boolean isScrolling;
    private boolean isLastItem;

    private ArrayList<String> listBanner = new ArrayList<>();
    private BannerAdapter photoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_flash_sale, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listBanner = (ArrayList<String>) listBanner();

        ln_flashsale_1 = view.findViewById(R.id.ln_flashsale_1);
        ln_flashsale_2 = view.findViewById(R.id.ln_flashsale_2);
        ln_flashsale_3 = view.findViewById(R.id.ln_flashsale_3);
        ln_flashsale_4 = view.findViewById(R.id.ln_flashsale_4);
        txt_Flashsale_1 = view.findViewById(R.id.txt_TimeFlashsale_1);
        txt_Flashsale_2 = view.findViewById(R.id.txt_TimeFlashsale_2);
        txt_Flashsale_3 = view.findViewById(R.id.txt_TimeFlashsale_3);
        txt_Flashsale_4 = view.findViewById(R.id.txt_TimeFlashsale_4);
        rv_Flashsale_now = view.findViewById(R.id.rv_Flassale_now);
        tv_hour = view.findViewById(R.id.tv_hour);
        tv_minute = view.findViewById(R.id.tv_minute);
        tv_second = view.findViewById(R.id.tv_second);
        banner = view.findViewById(R.id.banner_flashsale);


        photoAdapter = new BannerAdapter(getContext(), listBanner);
        banner.setAdapter(photoAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv_Flashsale_now.setLayoutManager(linearLayoutManager);
        TimeToCast();
        Log.d("DATA", "onViewCreated: "+Flashsalelist.size());
        if(Flashsalelist == null) {
            onGetItemTimeZone();
        } else {
            flashsaleAdapter = new FlashsaleAdapter(getContext(),Flashsalelist);
            rv_Flashsale_now.setAdapter(flashsaleAdapter);
            countDownStart(Flashsalelist.get(1).getDateend());
        }

        rv_Flashsale_now.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItems  = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItem = linearLayoutManager.getItemCount();
                if(isScrolling && (firstVisibleItems + visibleItemCount) == totalItem && !isLastItem) {
                    isScrolling = false;
                    Query nextQuery = FirebaseFirestore.getInstance().collection("product")
                            .startAfter(lastVisibleFlashSale)
                            .limit(10);
                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                            Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                            flashsaleAdapter.notifyDataSetChanged();
                            if(task.getResult().size() < 10 ){
                                isLastItem = true;
                                return;
                            }
                            lastVisibleFlashSale = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        }
                    });

                }
            }
        });
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
                        //
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
                            flashsaleAdapter = new FlashsaleAdapter(getContext(),Flashsalelist);
                            rv_Flashsale_now.setAdapter(flashsaleAdapter);
                            countDownStart(Flashsalelist.get(1).getDateend());
                        }
                    }
                });
    }

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

    private List<String> listBanner() {
        List<String> list = new ArrayList<>();
        list.add("https://jaxtina.com/wp-content/uploads/2021/12/433.WEB_-1-e1639540751794.png");
        list.add("https://indecalnhanh.net/wp-content/uploads/2022/08/6.png");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_3.jpg?alt=media&token=b4de77ab-c432-477f-800a-0f9085ed5737");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_4.jpg?alt=media&token=75bf780e-9c85-4423-9a3c-6dd1ac8a7b2d");
        list.add("https://firebasestorage.googleapis.com/v0/b/image-5fa63.appspot.com/o/banner_5.jpg?alt=media&token=5b86d2af-1f5c-4f14-9bf0-e471440779f0");
        return list;
    }

//    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @SuppressLint("ResourceAsColor")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Date date = new Date();
//            int Hours = date.getHours();
//            Log.d("TAG FFFF", Hours + "");
//            if (Hours >= 9 && Hours < 12) {
//                txt_Flashsale_1.setTextColor(Color.parseColor("#F65858"));
//                txt_Flashsale_2.setTextColor(R.color.black);
//                txt_Flashsale_3.setTextColor(R.color.black);
//                txt_Flashsale_4.setTextColor(R.color.black);
//                txt_Flashsale_1.setText("9:00\nĐang diễn ra");
//                txt_Flashsale_2.setText("12:00\nSắp diễn ra");
//                txt_Flashsale_3.setText("15:00\nSắp diễn ra");
//                txt_Flashsale_4.setText("18:00\nSắp diễn ra");
//                onGetItemTimeZone();
//
//            } else if (Hours >= 12 && Hours < 15) {
//                txt_Flashsale_1.setTextColor(R.color.black);
//                txt_Flashsale_2.setTextColor(Color.parseColor("#F65858"));
//                txt_Flashsale_3.setTextColor(R.color.black);
//                txt_Flashsale_4.setTextColor(R.color.black);
//                txt_Flashsale_2.setText("12:00\nĐang diễn ra");
//                txt_Flashsale_1.setText("9:00\nĐã diễn ra");
//                txt_Flashsale_3.setText("15:00\nSắp diễn ra");
//                txt_Flashsale_4.setText("18:00\nSắp diễn ra");
//                onGetItemTimeZone();
//
//            } else if (Hours >= 15 && Hours < 18) {
//                txt_Flashsale_3.setTextColor(Color.parseColor("#F65858"));
//                txt_Flashsale_2.setTextColor(R.color.black);
//                txt_Flashsale_1.setTextColor(R.color.black);
//                txt_Flashsale_4.setTextColor(R.color.black);
//                txt_Flashsale_3.setText("15:00\nĐang diễn ra");
//                txt_Flashsale_1.setText("9:00\nĐã diễn ra");
//                txt_Flashsale_2.setText("12:00\nĐã diễn ra");
//                txt_Flashsale_4.setText("18:00\nSắp diễn ra");
//                onGetItemTimeZone();
//
//            } else if (Hours >= 18 && Hours < 21) {
//                txt_Flashsale_4.setTextColor(Color.parseColor("#F65858"));
//                txt_Flashsale_2.setTextColor(R.color.black);
//                txt_Flashsale_3.setTextColor(R.color.black);
//                txt_Flashsale_1.setTextColor(R.color.black);
//                txt_Flashsale_4.setText("18:00\nĐang diễn ra");
//                txt_Flashsale_1.setText("9:00\nĐã diễn ra");
//                txt_Flashsale_2.setText("12:00\nĐã diễn ra");
//                txt_Flashsale_3.setText("15:00\nĐã diễn ra");
//                onGetItemTimeZone();
//
//            } else {
//                ln_flashsale_1.setVisibility(View.GONE);
//                ln_flashsale_2.setVisibility(View.GONE);
//                ln_flashsale_3.setVisibility(View.GONE);
//                ln_flashsale_4.setVisibility(View.GONE);
//
//            }
//            flashsaleAdapter.notifyDataSetChanged();
//        }
//    };
}
