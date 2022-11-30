package nta.nguyenanh.code_application;


import static nta.nguyenanh.code_application.SplashScreen.Flashsalelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


import java.lang.reflect.Type;
import java.util.ArrayList;

import nta.nguyenanh.code_application.dialog.DialogConfirm;
import nta.nguyenanh.code_application.fragment.main.FlashSaleFragment;
import nta.nguyenanh.code_application.fragment.main.HomeFragment;
import nta.nguyenanh.code_application.fragment.main.NotificationFragment;
import nta.nguyenanh.code_application.fragment.main.ProfileFragment;
import nta.nguyenanh.code_application.interfaces.OnClickDiaLogConfirm;
import nta.nguyenanh.code_application.interfaces.OnClickItemProduct;
import nta.nguyenanh.code_application.listener.CheckLogin;
import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.Product;
import nta.nguyenanh.code_application.model.ProductCart;
import nta.nguyenanh.code_application.model.User;

public class MainActivity extends AppCompatActivity implements OnClickItemProduct {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static User userModel;

    FragmentManager manager;
    private BottomNavigationView bottomnavigation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.framelayout, homeFragment).commit();

        bottomnavigation = findViewById(R.id.bottomNavigation);
        bottomnavigation.setItemIconTintList(null);
        bottomnavigation.setOnNavigationItemSelectedListener(setMenuBottom);
        Log.d("TAG 2000", "onComplete: "+Flashsalelist.size());

//        db.collection("product").limit(10)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                ArrayList<String> color = (ArrayList<String>) document.getData().get("color");
//                                ArrayList<String> images = (ArrayList<String>) document.getData().get("image");
//                                Product product = new Product(document.getId(),
//                                        document.getData().get("nameproduct").toString(),
//                                        document.getData().get("describe").toString(),
//                                        Float.parseFloat(document.getData().get("price").toString()),
//                                        Integer.parseInt(document.getData().get("available").toString()),
//                                        color, images,
//                                        Integer.parseInt(document.getData().get("sale").toString()),
//                                        Integer.parseInt(document.getData().get("sold").toString()),
//                                        Integer.parseInt(document.getData().get("total").toString()),
//                                        document.getData().get("id_category").toString());
//                                listProduct.add(product);
//                            }
//                            // sau khi lấy dữ liệu xong thì sẽ tắt dialog
//                            progess.hideDialog();
//
//                            HomeFragment homeFragment = new HomeFragment();
//                            FragmentManager manager = getSupportFragmentManager();
//                            manager.beginTransaction().replace(R.id.framelayout, homeFragment).commit();
//
//                        } else {
//                            Log.w("readDataProduct", "Error getting documents.", task.getException());
//                        }
//                    }
//                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckLogin(MainActivity.this).readLogin();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        readlogin();
//        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }

    public void relaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
    public void goToCart(View view) {
        if (userModel == null) {
            DialogConfirm dialogConfirm = new DialogConfirm(MainActivity.this, new OnClickDiaLogConfirm() {
                @Override
                public void ClickButtonAgree() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            dialogConfirm.showDialog();
        } else {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public BottomNavigationView.OnNavigationItemSelectedListener setMenuBottom = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.bt_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.bt_flashsale:
                    fragment = new FlashSaleFragment();
                    break;
                case  R.id.bt_notification:
                    fragment = new NotificationFragment();
                    break;
                case R.id.bt_user:
                    fragment = new ProfileFragment();
                    break;
                default:
                    fragment = new HomeFragment();
            }

            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.framelayout, fragment);
            fragmentTransaction.commit();

            return true;
        }
    };


    @Override
    public void GoToActivity(Product product) {
        Intent intent = new Intent(MainActivity.this, DetailProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
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

            // xem trên yt https://www.youtube.com/watch?v=xjOyvwRinK8&ab_channel=TechProjects
            Gson gson = new Gson();
            String json = preferences.getString("address", null);
            Type type = new TypeToken<ArrayList<Address>>(){
            }.getType();
            ArrayList<Address> addressList = gson.fromJson(json, type);
            if(addressList == null) {
                addressList  = new ArrayList<>();
            }
            userModel = new User(addressList, null, fullname, password, numberphone, username, userid);
        }
    }

    public void goToPay(ArrayList<ProductCart> list) {

    }

}