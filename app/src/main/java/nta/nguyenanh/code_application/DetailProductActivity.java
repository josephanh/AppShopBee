package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import nta.nguyenanh.code_application.adapter.DetailProductImageAdapter;
import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.adapter.SuggestAdapter;
import nta.nguyenanh.code_application.bottomsheet.BottomSheetDetail;
import nta.nguyenanh.code_application.bottomsheet.BottomSheetGoToPay;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.dialog.DialogConfirm;
import nta.nguyenanh.code_application.interfaces.OnClickDiaLogConfirm;
import nta.nguyenanh.code_application.listener.CheckLogin;
import nta.nguyenanh.code_application.model.Product;
import nta.nguyenanh.code_application.model.ProductCart;

public class DetailProductActivity extends AppCompatActivity{

    Product product;
    ArrayList<String> listUrlImage = new ArrayList<>();
    ArrayList<ProductCart> listPay = new ArrayList<>();
    DetailProductImageAdapter photoAdapter;

    private List<Product> listResult = new ArrayList<>();

    TextView name_product, price_product, describe;
    ImageView banner_detail,img_openmes;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DiaLogProgess progess;


    DialogConfirm dialogConfirm;
    Button gotoPay;
    private CoordinatorLayout btn_more_content;
    private boolean more_content = false;
    private TextView tv_more_content;
    private ImageView img_more_content;
    private RecyclerView rv_item_suggess;
    private SuggestAdapter suggessAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        if (savedInstanceState != null) {
            product = (Product) savedInstanceState.getSerializable("product");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewpageImage);
        CircleIndicator circleIndicator = findViewById(R.id.circleIndicatorImageDetail);
        ImageView show_sheet = findViewById(R.id.show_sheet);
        banner_detail = findViewById(R.id.banner_detail);
        gotoPay = findViewById(R.id.gotoPay);
        img_openmes = findViewById(R.id.img_openmes);

        rv_item_suggess = findViewById(R.id.lv_item_suggess);
        LinearLayoutManager suggessManeger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rv_item_suggess.setLayoutManager(suggessManeger);



        img_openmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel==null){
                    DialogConfirm dialogConfirm  = new DialogConfirm(DetailProductActivity.this, new OnClickDiaLogConfirm() {
                        @Override
                        public void ClickButtonAgree() {
                            Intent intent = new Intent(DetailProductActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    Intent intent = new Intent(DetailProductActivity.this,HomeActivity.class);
                    startActivity(intent);
                }

            }
        });
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
        Log.d("TAG", "onCreate: " + toolbar.getTitle());
        product = (Product) getIntent().getSerializableExtra("product");

        name_product = findViewById(R.id.name_product);
        price_product = findViewById(R.id.price_product);
        describe = findViewById(R.id.describe);

        name_product.setText(product.getNameproduct());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        price_product.setText(formatter.format(product.getPrice()) + "đ");

        describe.setText(android.text.Html.fromHtml(product.getDescribe()), TextView.BufferType.SPANNABLE);

        img_more_content = findViewById(R.id.img_more_content);
        tv_more_content = findViewById(R.id.tv_more_content);


        btn_more_content = findViewById(R.id.btn_more_content);
        btn_more_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (more_content == false){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    describe.setLayoutParams(layoutParams);
                    Log.d(">>>>TAG:", "1");
                    tv_more_content.setText("Thu gọn");
                    img_more_content.setImageResource(R.drawable.ic_up_red);
                    more_content = true;
                } else {
                    Log.d(">>>>TAG:", "2");
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) DpToPx(120f));
                    describe.setLayoutParams(layoutParams);
                    tv_more_content.setText("Xem thêm");
                    img_more_content.setImageResource(R.drawable.ic_down_red);
                    more_content = false;
                }


            }
        });


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
                if (userModel == null) {
                    dialogConfirm = new DialogConfirm(DetailProductActivity.this, new OnClickDiaLogConfirm() {
                        @Override
                        public void ClickButtonAgree() {
                            goToLogin();
                        }
                    });
                    dialogConfirm.showDialog();
                } else {
                    BottomSheetDetail bottomSheetDetail = new BottomSheetDetail(DetailProductActivity.this, product);
                    bottomSheetDetail.showSheet();
                }
            }
        });

        gotoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel == null) {
                    dialogConfirm = new DialogConfirm(DetailProductActivity.this, new OnClickDiaLogConfirm() {
                        @Override
                        public void ClickButtonAgree() {
                            goToLogin();
                        }
                    });
                    dialogConfirm.showDialog();
                } else {
                    BottomSheetGoToPay bottomSheetGoToPay = new BottomSheetGoToPay(DetailProductActivity.this, product);
                    bottomSheetGoToPay.showSheet();
                }
            }
        });

        findData(product.getId_category());
    }

    public void findData(String s) {
        listResult.clear();
        Log.d(">>>>TAG:", "getId()"+product.getId());
        Log.d(">>>>TAG:", "getId_category()"+product.getId_category());
        db.collection("product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document:task.getResult()){
                            String id_category = document.getString("id_category");
                            if (id_category.contains(s)){
                                if (document.getId().equals(product.getId())){
                                    continue;
                                }
                                Log.d("TAG 1000", "onComplete: "+document.getString("nameproduct"));
                                ArrayList<String> color = (ArrayList<String>) document.getData().get("color");
                                ArrayList<String> images = (ArrayList<String>) document.getData().get("image");
                                Product product = new Product(document.getId(),
                                        document.getData().get("nameproduct").toString(),
                                        document.getData().get("describe").toString(),
                                        Float.parseFloat(document.getData().get("price").toString()),
                                        Integer.parseInt(document.getData().get("available").toString()),
                                        color, images,
                                        Integer.parseInt(document.getData().get("sale").toString()),
                                        Integer.parseInt(document.getData().get("sold").toString()),
                                        Integer.parseInt(document.getData().get("total").toString()),
                                        document.getData().get("id_category").toString());
                                listResult.add(product);
                            }
                        }
                        if (listResult.size() != 0 || listResult != null){
                            suggessAdapter = new SuggestAdapter(listResult, DetailProductActivity.this);
                            rv_item_suggess.setAdapter(suggessAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(">>>>TAG:","");
                    }
                });
    }

    private float DpToPx(float a){
        float dip = a;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        return px;
    }

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onResume() {
        new CheckLogin(DetailProductActivity.this).readLogin();
        super.onResume();
    }

    private void goToLogin() {
        Intent intentLogin = new Intent(DetailProductActivity.this, LoginActivity.class);
        startActivity(intentLogin);
    }

//    public void readLogin() {
//        SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
//        Boolean isLoggedin = preferences.getBoolean("isLoggedin", false);
//        if (isLoggedin) {
//            String userid = preferences.getString("userid", null);
//            String username = preferences.getString("username", null);
//            String fullname = preferences.getString("fullname", null);
//            String password = preferences.getString("password", null);
//            String numberphone = preferences.getString("numberphone", null);
//
//            // xem trên yt https://www.youtube.com/watch?v=xjOyvwRinK8&ab_channel=TechProjects
//            Gson gson = new Gson();
//            String json = preferences.getString("address", null);
//            Type type = new TypeToken<ArrayList<Address>>() {
//            }.getType();
//            ArrayList<Address> addressList = gson.fromJson(json, type);
//            if (addressList == null) {
//                addressList = new ArrayList<>();
//            }
//            userModel = new User(addressList, null, fullname, password, numberphone, username, userid);
//        }
//    }

    public void addCartToFirestore(ProductCart products) {
        progess = new DiaLogProgess(DetailProductActivity.this);
        progess.showDialog("Waiting");
        Map<String, Object> cart = new HashMap<>();
        cart.put(products.getId() + "-" + System.currentTimeMillis(), products);

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

    public void goToPay(ProductCart productCart) {
        if (productCart != null) {
            listPay.clear();
            listPay.add(productCart);
            Log.d("listPay", "onClick: " + productCart.getNameproduct());
            // https://stackoverflow.com/questions/13601883/how-to-pass-arraylist-of-objects-from-one-to-another-activity-using-intent-in-an
            if(userModel.getAddress() == null || userModel.getAddress().size() == 0) {
                Intent intent = new Intent(DetailProductActivity.this, PayActivity.class);
                intent.putExtra("listPay", listPay);
                intent.putExtra("addressCheck", false);
                startActivity(intent);
            } else {
                Intent intent = new Intent(DetailProductActivity.this, PayActivity.class);
                intent.putExtra("listPay", listPay);
                intent.putExtra("addressCheck", true);
                startActivity(intent);
            }

        }
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
                if (userModel == null) {
                    dialogConfirm = new DialogConfirm(DetailProductActivity.this, new OnClickDiaLogConfirm() {
                        @Override
                        public void ClickButtonAgree() {
                            goToLogin();
                        }
                    });
                    dialogConfirm.showDialog();
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
