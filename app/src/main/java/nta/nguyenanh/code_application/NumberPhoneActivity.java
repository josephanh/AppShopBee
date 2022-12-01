package nta.nguyenanh.code_application;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.FrameLayout;

import nta.nguyenanh.code_application.fragment.otp.NumberPhoneFragment;

public class NumberPhoneActivity extends AppCompatActivity {

    private FrameLayout frameAddFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numberphone);
        frameAddFragment = findViewById(R.id.frameAddFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAddFragment, NumberPhoneFragment.class, null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}