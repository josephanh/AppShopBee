package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.DetailProductActivity.NameFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


import nta.nguyenanh.code_application.fragment.GetCoinEverydayFragment;
import nta.nguyenanh.code_application.fragment.HomeFragment;
import nta.nguyenanh.code_application.fragment.NotificationFragment;
import nta.nguyenanh.code_application.interfaces.OnClickDiaLogConfirm;
import nta.nguyenanh.code_application.interfaces.OnClickItemProduct;
import nta.nguyenanh.code_application.model.Product;
import nta.nguyenanh.code_application.model.UserModel;

public class MainActivity extends AppCompatActivity implements OnClickItemProduct, OnClickDiaLogConfirm {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static UserModel userModel;

    public static List<Product> listProduct = new ArrayList<>();

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


    public void RelaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(NameFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void ClickButtonAgree() {
        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLogin);
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
                    fragment = new GetCoinEverydayFragment();
                    break;
                case  R.id.bt_notification:
                    fragment = new NotificationFragment();
                    break;
                case R.id.bt_user:
                    fragment = new NotificationFragment();
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
}