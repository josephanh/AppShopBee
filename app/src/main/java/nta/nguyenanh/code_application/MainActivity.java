package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.fragment.DetailProductFragment.NameFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


import nta.nguyenanh.code_application.fragment.DetailProductFragment;
import nta.nguyenanh.code_application.fragment.HomeFragment;
import nta.nguyenanh.code_application.interfaces.OnClickItemProduct;
import nta.nguyenanh.code_application.model.History_model;
import nta.nguyenanh.code_application.model.Product;

public class MainActivity extends AppCompatActivity implements OnClickItemProduct {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static List<Product> listProduct = new ArrayList<>();

    FragmentManager manager;
    public static BottomNavigationView bottomnavigation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomnavigation = findViewById(R.id.bottomNavigation);
        bottomnavigation.setItemIconTintList(null);


        HomeFragment homeFragment = new HomeFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.framelayout, homeFragment).commit();
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
    public void ReplaceFragment(Product product) {
        DetailProductFragment fragment = new DetailProductFragment().newInstance(product);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(NameFragment);
        fragmentTransaction.commit();
        bottomnavigation.setVisibility(View.GONE);
    }

    public void RelaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(NameFragment);
        fragmentTransaction.commit();
        bottomnavigation.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}