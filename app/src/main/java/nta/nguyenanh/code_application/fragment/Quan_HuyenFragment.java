package nta.nguyenanh.code_application.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nta.nguyenanh.code_application.R;

public class Quan_HuyenFragment extends Fragment {


    public Quan_HuyenFragment() {
        // Required empty public constructor
    }

    public static Quan_HuyenFragment newInstance(String param1, String param2) {
        Quan_HuyenFragment fragment = new Quan_HuyenFragment();
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
        return inflater.inflate(R.layout.fragment_quan__huyen, container, false);
    }
}