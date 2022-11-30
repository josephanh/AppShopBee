package nta.nguyenanh.code_application.fragment.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import nta.nguyenanh.code_application.OnBoardingActivity;
import nta.nguyenanh.code_application.R;

public class GetStarted_Fragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_started, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonGetStart = view.findViewById(R.id.buttonGetStart);
        buttonGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnBoardingActivity)getContext()).goToMain();
            }
        });
    }
}