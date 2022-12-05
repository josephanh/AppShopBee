package nta.nguyenanh.code_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nta.nguyenanh.code_application.adapter.CartAdapter;
import nta.nguyenanh.code_application.fragment.pay.AllAddressFragment;
import nta.nguyenanh.code_application.fragment.pay.PayFragment;
import nta.nguyenanh.code_application.interfaces.OnclickItemCart;
import nta.nguyenanh.code_application.model.ProductCart;
import nta.nguyenanh.code_application.notification.SendNotification;

public class PayActivity extends AppCompatActivity {

    ArrayList<ProductCart> list = new ArrayList<>();
    private Button btn_buy_d;
    Toolbar toolBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        toolBar = findViewById(R.id.toolBar);

        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        list.clear();
        list = (ArrayList<ProductCart>) getIntent().getSerializableExtra("listPay");

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout, new PayFragment().newInstance(list)).commit();

    }

    private void buyEvent(String price){
        btn_buy_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Chúc mừng đặt hàng thành công!";
                String content = MainActivity.userModel.getFullname()+" đã đặt hàng thành công với tổng giá trị: "+price+"đ";
                SendNotification notification = new SendNotification(PayActivity.this);
                notification.customNotification(title, content);
                Toast.makeText(PayActivity.this, "Đặt hàng thành công !!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
//        list.clear();
//        list = (ArrayList<ProductCart>) getIntent().getSerializableExtra("listPay");
        super.onResume();
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout, fragment)
                .addToBackStack(null).commit();
    }
}