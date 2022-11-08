package nta.nguyenanh.code_application.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.PhotoAdapter;
import nta.nguyenanh.code_application.model.Photo_banner;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    ViewPager viewPager_2;

    CircleIndicator circleIndicator, circleIndicator_2;

    PhotoAdapter photoAdapter, photoAdapter_2;

    List<Photo_banner> listPhoto, listRechargeCard;

    Timer timer, timer_2;

    TextView txth,txtm,txts;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Ánh xạ
        viewPager = view.findViewById(R.id.viewpageHome);
        viewPager_2 = view.findViewById(R.id.viewpageHome_2);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        circleIndicator_2 = view.findViewById(R.id.circleIndicator_2);

        // Khởi tạo data cho banner
        listPhoto = listBanner();
        listRechargeCard = listRechargeCard();
        // set Adapter
        photoAdapter = new PhotoAdapter(getContext(), listPhoto);
        viewPager.setAdapter(photoAdapter);
        // Gắn circleIndicator
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideBanner();
        // Tương tự như trên cho banner nạp thẻ
        photoAdapter_2 = new PhotoAdapter(getContext(), listRechargeCard);
        viewPager_2.setAdapter(photoAdapter_2);
        circleIndicator_2.setViewPager(viewPager_2);
        photoAdapter_2.registerDataSetObserver(circleIndicator_2.getDataSetObserver());
        autoSlidereChargeCard();

        // Lấy thời gian hiện tại






    }
    // set data cho listbanner -- banner chính của home
    private List<Photo_banner> listBanner() {
        List<Photo_banner> list = new ArrayList<>();
        list.add(new Photo_banner(R.drawable.banner_1));
        list.add(new Photo_banner(R.drawable.banner_2));
        list.add(new Photo_banner(R.drawable.banner_3));
        list.add(new Photo_banner(R.drawable.banner_4));
        list.add(new Photo_banner(R.drawable.banner_5));
        return list;
    }
    // set data cho listRechargeCard -- banner nạp thẻ và dịch vụ
    private List<Photo_banner> listRechargeCard() {
        List<Photo_banner> list = new ArrayList<>();
        list.add(new Photo_banner(R.drawable.banner_napthe_1));
        list.add(new Photo_banner(R.drawable.banner_napthe_2));
        list.add(new Photo_banner(R.drawable.banner_napthe_3));
        list.add(new Photo_banner(R.drawable.banner_napthe_4));
        return list;
    }

    private void autoSlideBanner() {
        if(listPhoto == null || listPhoto.isEmpty() || viewPager == null) {
            return;
        }
        // Init timer
        if(timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = listPhoto.size() -1;

                        if(currentItem < totalItem) {
                            currentItem ++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }
    private void autoSlidereChargeCard() {
        if(listPhoto == null || listPhoto.isEmpty() || viewPager == null) {
            return;
        }
        // Init timer
        if(timer_2 == null) {
            timer_2 = new Timer();
        }

        timer_2.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager_2.getCurrentItem();
                        int totalItem = listRechargeCard.size() -1;

                        if(currentItem < totalItem) {
                            currentItem ++;
                            viewPager_2.setCurrentItem(currentItem);
                        } else {
                            viewPager_2.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // hủy timer khi fragment bị hủy
        if(timer != null) {
            timer.cancel();
            timer = null;
        }

        if(timer_2 != null) {
            timer_2.cancel();
            timer_2 = null;
        }
    }
}