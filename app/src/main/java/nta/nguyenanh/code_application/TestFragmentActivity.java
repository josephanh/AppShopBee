package nta.nguyenanh.code_application;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.FrameLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import nta.nguyenanh.code_application.databinding.ActivityTestFragmentBinding;

public class TestFragmentActivity extends AppCompatActivity {

    private FrameLayout frameAddFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        frameAddFragment = findViewById(R.id.frameAddFragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAddFragment, GetCoinEveryday.class, null).commit();
    }


}