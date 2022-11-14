package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.listProduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import nta.nguyenanh.code_application.model.Product;

public class SplashScreen extends AppCompatActivity {
    // thời gian chờ
    int SLASH_TIME_OUT = 3000;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static DocumentSnapshot lastVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_creen);

        db.collection("product").limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                                listProduct.add(product);

                            }
                            Log.d("CHECK_SIZE", "onComplete 1: " + listProduct.size());



                            // phân trang
                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);




                        } else {
                            Log.w("readDataProduct", "Error getting documents.", task.getException());

                        }
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        },SLASH_TIME_OUT);

    }
}