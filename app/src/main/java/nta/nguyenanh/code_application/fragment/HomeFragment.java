package nta.nguyenanh.code_application.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.PhotoAdapter;
import nta.nguyenanh.code_application.model.Photo_banner;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    PhotoAdapter photoAdapter;


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

        photoAdapter = new PhotoAdapter(getContext(), listBanner());
        viewPager.setAdapter(photoAdapter);
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
}