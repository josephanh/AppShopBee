package nta.nguyenanh.code_application.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.PhotoAdapter;
import nta.nguyenanh.code_application.model.Photo_banner;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    PhotoAdapter photoAdapter;

    List<Photo_banner> listPhoto;

    Timer timer;


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewpageHome);

        listPhoto = listBanner();

        photoAdapter = new PhotoAdapter(getContext(), listPhoto);
        viewPager.setAdapter(photoAdapter);
        autoSlideBanner();


    }

    private List<Photo_banner> listBanner() {
        List<Photo_banner> list = new ArrayList<>();
        list.add(new Photo_banner(R.drawable.banner_1));
        list.add(new Photo_banner(R.drawable.banner_2));
        list.add(new Photo_banner(R.drawable.banner_3));
        list.add(new Photo_banner(R.drawable.banner_4));
        list.add(new Photo_banner(R.drawable.banner_5));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}