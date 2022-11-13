package nta.nguyenanh.code_application.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nta.nguyenanh.code_application.R;

public class Tinh_ThanhphoFragment extends Fragment {




    public Tinh_ThanhphoFragment() {

    }

    public static Tinh_ThanhphoFragment newInstance(String param1, String param2) {
        Tinh_ThanhphoFragment fragment = new Tinh_ThanhphoFragment();
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
        return inflater.inflate(R.layout.fragment_tinh__thanhpho, container, false);
    }
}