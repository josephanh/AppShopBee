package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import nta.nguyenanh.code_application.adapter.DetailProductImageAdapter;
import nta.nguyenanh.code_application.bottomsheet.BottomSheet;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.dialog.DialogConfirm;
import nta.nguyenanh.code_application.interfaces.OnClickDiaLogConfirm;
import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.Product;
import nta.nguyenanh.code_application.model.ProductCart;
import nta.nguyenanh.code_application.model.User;

public class DetailProductActivity extends AppCompatActivity implements OnClickDiaLogConfirm {

    Product product;
    ArrayList<String> listUrlImage = new ArrayList<>();
    DetailProductImageAdapter photoAdapter;

    TextView name_product, price_product, describe;
    ImageView banner_detail;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DiaLogProgess progess;


    DialogConfirm dialogConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        if(savedInstanceState != null) {
            product = (Product) savedInstanceState.getSerializable("product");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewpageImage);
        CircleIndicator circleIndicator = findViewById(R.id.circleIndicatorImageDetail);
        ImageView show_sheet = findViewById(R.id.show_sheet);
        banner_detail = findViewById(R.id.banner_detail);

        Glide.with(this).load("https://gcp-img.slatic.net/lazada/9a6cb2e4-5f74-4733-8435-6e76b4f8ee36_VN-1188-470.gif").into(banner_detail);

        setSupportActionBar(toolbar);
//        getSupportActionBar().set
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Chi tiết sản phẩm");
        Log.d("TAG", "onCreate: "+toolbar.getTitle());
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
                    BottomSheet bottomSheet = new BottomSheet(DetailProductActivity.this, product);
                    bottomSheet.showSheet();
                }
            }
        });
    }
    @SuppressLint("MissingInflatedId")

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

            String numberphone = preferences.getString("numberphone", null);
            userModel = new User(null, null, fullname, password, numberphone, username, userid);
        }
    }

    public void addCartToFirestore(ProductCart products) {
        progess = new DiaLogProgess(DetailProductActivity.this);
        progess.showDialog("Waiting");
        Map<String, Object> cart = new HashMap<>();
        cart.put(products.getId()+"-"+System.currentTimeMillis(), products);

        db.collection("cart").document(userModel.getUserID())
                .update(cart)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progess.hideDialog();
                        Log.d("TAG>>>", "Thêm dữ liệu thành công ");
                        Toast.makeText(DetailProductActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG>>>", "Thêm dữ liệu thất bạn ");
                        progess.hideDialog();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart: {
                if(userModel == null) {
                    dialogConfirm = new DialogConfirm(DetailProductActivity.this);
                    dialogConfirm.showDialog(product);
                } else {
                    Intent intent = new Intent(DetailProductActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        product = (Product) savedInstanceState.getSerializable("product");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("product", product);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SAVE", "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SAVE", "onStop");
    }
}
