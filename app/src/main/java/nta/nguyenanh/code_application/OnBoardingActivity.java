package nta.nguyenanh.code_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;

public class OnBoardingActivity extends AppCompatActivity {

    private TextView tv_skip;
    private ViewPager viewPager;
    private LinearLayout layoutButton;
    private CircleIndicator circleIndicator;
    private Button btn_next;
    private ViewPaperAdapter viewPaperAdapter;
    private FrameLayout frameGetStart;
    private LinearLayout layout_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        initUI();
        setViewPager();
        setOnboarding_next();
        setOnboarding_skip();
    }

    private void setViewPager(){
        viewPaperAdapter = new ViewPaperAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPaperAdapter);
        circleIndicator.setViewPager(viewPager);


    }

    private void setOnboarding_skip(){
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setOnboarding_next(){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < 2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
                else{
                    layout_main.setVisibility(View.GONE);
                    frameGetStart.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.alpha_enter, R.animator.alpha_exit).add(R.id.getStart, GetStarted_Fragment.class, null).commit();
                }
            }
        });
    }

    private void initUI(){
        tv_skip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.viewPaper_onboarding);
        layoutButton = findViewById(R.id.layout_button);
        circleIndicator = findViewById(R.id.ciCircleIndicator_on);
        btn_next = findViewById(R.id.btn_next_onboarding);
        frameGetStart = findViewById(R.id.getStart);
        layout_main = findViewById(R.id.layout_main_onboarding);
    }

}