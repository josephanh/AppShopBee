package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.fragment.DetailProductFragment.NameFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.fragment.DetailProductFragment;
import nta.nguyenanh.code_application.fragment.HomeFragment;
import nta.nguyenanh.code_application.interfaces.OnClickItemProduct;
import nta.nguyenanh.code_application.model.Product;

public class MainActivity extends AppCompatActivity implements OnClickItemProduct {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static List<Product> listProduct = new ArrayList<>();

    DiaLogProgess progess;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progess = new DiaLogProgess(MainActivity.this);
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
    }
}