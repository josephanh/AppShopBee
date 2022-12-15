package nta.nguyenanh.code_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nta.nguyenanh.code_application.model.Product;

public class SplashScreen extends AppCompatActivity {
    // thời gian chờ
    int SLASH_TIME_OUT = 3000;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static DocumentSnapshot lastVisibleProduct, lastVisibleFlashSale;
    public static List<Product> listProduct = new ArrayList<>();
    public static List<Product> Flashsalelist = new ArrayList<>();
    Float sale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_creen);
        getDataProduct(new getDataFlashSale());

    }

    public void getDataProduct(getDataFlashSale getDataFlashSale) {
        getDataFlashSale.onGetItemTimeZone();
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
                            lastVisibleProduct = task.getResult().getDocuments().get(task.getResult().size() - 1);
                            SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
                            Boolean checkFirst = preferences.getBoolean("firstGo", true);
                            if(checkFirst) {
                                preferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("firstGo", false);
                                editor.commit();
                                Intent intent = new Intent(SplashScreen.this, OnBoardingActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            Toast.makeText(SplashScreen.this, "Lỗi khi lấy dữ liệu. Thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SplashScreen.this, "Lỗi khi lấy dữ liệu. Thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class getDataFlashSale {
        public void onGetItemTimeZone() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            db.collection("product").limit(5)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshots = task.getResult();

                                for (QueryDocumentSnapshot document : snapshots) {
                                    sale = Float.parseFloat(document.get("sale").toString());
                                    Long start = Long.parseLong(document.get("datestart").toString());
                                    Long end = Long.parseLong(document.get("dateend").toString());

                                    Log.d("DATE", "onComplete: "+System.currentTimeMillis());
                                    if (sale > 0 && Integer.parseInt(document.get("total") + "") > 0) {
                                        if (System.currentTimeMillis() >= start * (1000L) && System.currentTimeMillis() < end * (1000L)) {
                                            Log.d("TAG 2000", "Đang sale: " + document.getId());
                                            Log.d("TAG 2000", "sale: " + sale);
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
                                                    document.getData().get("id_category").toString(),
                                                    Long.parseLong(document.get("datestart").toString()),
                                                    Long.parseLong(document.get("dateend").toString()));
                                            Flashsalelist.add(product);

                                        } else if (System.currentTimeMillis() < start * (1000L)) {
                                            Log.d("TAG 2000", "Sắp sale: " + document.getId());
                                            Log.d("TAG 2000", "sale: " + sale);
                                        }
                                    }
                                }
                                lastVisibleFlashSale = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                Log.d("TAG 2000 L", "onComplete: "+Flashsalelist.size());
                            }
                        }
                    });
        }
    }
}