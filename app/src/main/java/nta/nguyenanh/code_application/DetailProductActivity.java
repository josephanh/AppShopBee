package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import nta.nguyenanh.code_application.adapter.DetailProductImageAdapter;
import nta.nguyenanh.code_application.dialog.DialogConfirm;
import nta.nguyenanh.code_application.interfaces.OnClickDiaLogConfirm;
import nta.nguyenanh.code_application.model.Product;
import nta.nguyenanh.code_application.model.UserModel;

public class DetailProductActivity extends AppCompatActivity implements OnClickDiaLogConfirm {

    Product product;
    ArrayList<String> listUrlImage = new ArrayList<>();
    DetailProductImageAdapter photoAdapter;

    TextView name_product, price_product, describe;


    DialogConfirm dialogConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewpageImage);
        CircleIndicator circleIndicator = findViewById(R.id.circleIndicatorImageDetail);
        ImageView show_sheet = findViewById(R.id.show_sheet);

        setSupportActionBar(toolbar);
//        getSupportActionBar().set
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);


        product = (Product) getIntent().getSerializableExtra("product");

        name_product = findViewById(R.id.name_product);
        price_product = findViewById(R.id.price_product);
        describe = findViewById(R.id.describe);

        name_product.setText(product.getNameproduct());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        price_product.setText(formatter.format(product.getPrice())+"đ");

        describe.setText(android.text.Html.fromHtml(product.getDescribe()), TextView.BufferType.SPANNABLE);

        // gắn hình cho viewpage
        listUrlImage = product.getImage();
        photoAdapter = new DetailProductImageAdapter(this, listUrlImage);
        viewPager.setAdapter(photoAdapter);
        // Gắn circleIndicator
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        show_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel == null) {
                    dialogConfirm = new DialogConfirm(DetailProductActivity.this);
                    dialogConfirm.showDialog(product);
                } else {
                    showSheet();
                }
            }
        });
    }
    @SuppressLint("MissingInflatedId")
    private void showSheet() {
        View viewDiaLog = getLayoutInflater().inflate(R.layout.custom_layout_addcart, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.ThemeOverlay_App_BottomSheetDialog);
        bottomSheetDialog.setContentView(viewDiaLog);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

        RoundedImageView color_1, color_2, color_3, color_4;
        ImageView image_product_sheet;
        TextView price, total;

        color_1 = viewDiaLog.findViewById(R.id.color_1);
        color_2 = viewDiaLog.findViewById(R.id.color_2);
        color_3 = viewDiaLog.findViewById(R.id.color_3);
        color_4 = viewDiaLog.findViewById(R.id.color_4);

        image_product_sheet = viewDiaLog.findViewById(R.id.image_product_sheet);
        price = viewDiaLog.findViewById(R.id.price_product);
        total = viewDiaLog.findViewById(R.id.total_product);

        Glide.with(DetailProductActivity.this).load(product.getImage().get(1)).into(image_product_sheet);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        price.setText(formatter.format(product.getPrice())+"đ");
        total.setText("Kho: " +product.getTotal());


        color_1.setBackgroundColor(Color.parseColor(product.getColor().get(0)));
        color_2.setBackgroundColor(Color.parseColor(product.getColor().get(1)));
        color_3.setBackgroundColor(Color.parseColor(product.getColor().get(2)));
        color_4.setBackgroundColor(Color.parseColor(product.getColor().get(3)));

    }
    @Override
    public void ClickButtonAgree() {
        Intent intentLogin = new Intent(DetailProductActivity.this, LoginActivity.class);
        startActivity(intentLogin);
    }

    @Override
    protected void onResume() {
        readlogin();
        super.onResume();
    }

    public void readlogin(){
        SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        Boolean isLoggedin = preferences.getBoolean("isLoggedin",false);
        if (isLoggedin){
            String userid = preferences.getString("userid", null);
            String username = preferences.getString("username", null);
            String fullname = preferences.getString("fullname", null);
            String password = preferences.getString("password", null);
            String address = preferences.getString("address", null);
            String numberphone = preferences.getString("numberphone", null);
            userModel = new UserModel(address, null, fullname, password, numberphone, username, userid);
        }
    }
}
