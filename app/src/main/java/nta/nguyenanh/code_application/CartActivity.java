package nta.nguyenanh.code_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.model.Product;

public class CartActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DiaLogProgess progess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        progess = new DiaLogProgess(CartActivity.this);

//        findCart("2DhfKW5XRl2NPYtmwq0I");

        ArrayList<Product> products = new ArrayList<>();
        ArrayList<String> color = new ArrayList<>();
        color.add("#000000");
        products.add(new Product("", 1800000F, color, 1));

        Map<String, Object> cart = new HashMap<>();
        cart.put("id_user", "2DhfKW5XRl2NPYtmwq0I");
        cart.put("id_product", products);

        db.collection("detailcart")
                .add(cart)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG>>>", "Thêm dữ liệu thành công "+documentReference.getId());
                        documentReference.getId();
                        HashMap<String, String> map = new HashMap<>();
                        map.put(documentReference.getId(), documentReference.getId());
                        db.collection("cart").document("2DhfKW5XRl2NPYtmwq0I")
                                .set(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG>>>", "Thêm dữ liệu thành công vào Cart");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG>>>", "Thêm dữ liệu lỗi");
                    }
                });


    }

    public void findCart(String s) {
        progess.showDialog("Loading");
        db.collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document:task.getResult()){
                            String id_user = document.getString("id_user");
                            if (id_user.contains(s)){
                                Log.d("TAG 1000", "onComplete: "+document.getData().get("id_product"));

//                                listResult.add(product);
                            }
                        }
                        progess.hideDialog();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progess.hideDialog();
                        Toast.makeText(CartActivity.this, "Không tìm thấy", Toast.LENGTH_LONG).show();
                    }
                });
    }
}