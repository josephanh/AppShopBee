package nta.nguyenanh.code_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashCreen extends AppCompatActivity {
    // thời gian chờ
    int SLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_creen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashCreen.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        },SLASH_TIME_OUT);

    }
}