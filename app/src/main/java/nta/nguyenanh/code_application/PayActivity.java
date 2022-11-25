package nta.nguyenanh.code_application;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nta.nguyenanh.code_application.adapter.CartAdapter;
import nta.nguyenanh.code_application.interfaces.OnclickItemCart;
import nta.nguyenanh.code_application.model.ProductCart;

public class PayActivity extends AppCompatActivity {

    ArrayList<ProductCart> list = new ArrayList<>();

    private RecyclerView recyclerViewItemPay;
    private TextView tvUsername, tvNumberPhone, tvAddress, dateReceive;
    private CartAdapter cartAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        tvUsername = findViewById(R.id.tvUsername);
        tvNumberPhone = findViewById(R.id.tvNumberPhone);
        tvAddress = findViewById(R.id.tvAddress);
        dateReceive = findViewById(R.id.dateReceive);

        long unix = System.currentTimeMillis();
        Date date = new Date(unix+432000000L);
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd/MM");
        String formattedDate = sdf.format(date);

        long unixRe = unix+691200000L;
        Date dateRe = new Date(unixRe);
        String formattedDateRe = sdf.format(dateRe);

        Log.d("TAGDATE", "onCreate: "+unix);
        Log.d("TAGDATE", "onCreate: "+unixRe);

        recyclerViewItemPay = findViewById(R.id.recyclerViewPay);

        tvUsername.setText(MainActivity.userModel.getAddress().get(0).getNameReceiver());
        tvNumberPhone.setText(MainActivity.userModel.getAddress().get(0).getPhonenumber());
        tvAddress.setText(MainActivity.userModel.getAddress().get(0).getAddress());
        dateReceive.setText("Nhận hàng vào "+formattedDate+" đến "+formattedDateRe);

        list.clear();
        list = (ArrayList<ProductCart>) getIntent().getSerializableExtra("listPay");
        Log.d("LIST DATA", "onCreate: pay"+list.get(0).getNameproduct());

        cartAdapter = new CartAdapter(list, this, new OnclickItemCart() {
            @Override
            public void onClickMinus(int totalNew, int totalOld, float price) {
                Toast.makeText(PayActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickPlus(int totalNew, int totalOld, float price) {
                Toast.makeText(PayActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickCheck(boolean isCheck, int total, float price) {
                Toast.makeText(PayActivity.this, "Hello", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void hideCheck(CheckBox checkBox, ImageView imageView) {
                checkBox.setVisibility(View.GONE);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewItemPay.setLayoutManager(layoutManager);;
        recyclerViewItemPay.setAdapter(cartAdapter);

    }

    @Override
    protected void onResume() {
//        list.clear();
//        list = (ArrayList<ProductCart>) getIntent().getSerializableExtra("listPay");
        cartAdapter.notifyDataSetChanged();
        super.onResume();
    }
}