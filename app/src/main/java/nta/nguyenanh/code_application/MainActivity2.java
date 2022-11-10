package nta.nguyenanh.code_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import org.checkerframework.common.subtyping.qual.Bottom;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.ViewPagerAdapter;

public class MainActivity2 extends AppCompatActivity {
    BottomSheetDialog bottomSheetDialog;
    private TabLayout tableLayout;
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }
    public void bottomsheet(View v){
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_address_bottom_sheet,null);
        tableLayout=  view.findViewById(R.id.tablayout);
        viewPager2 = view.findViewById(R.id.viewpager2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter);

        tableLayout.addTab(tableLayout.newTab().setText("Tỉnh / Thành phố"));
        tableLayout.addTab(tableLayout.newTab().setText("Quận / Huyện"));
        tableLayout.addTab(tableLayout.newTab().setText("Phường / Xã"));


        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
               tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });


        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

}