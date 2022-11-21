package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.model.CartModel;
import nta.nguyenanh.code_application.model.Product;

public class CartActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DiaLogProgess progess;
    ArrayList<CartModel> listCart = new ArrayList<>();

    RecyclerView recyclerViewCart, recyclerViewMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        progess = new DiaLogProgess(CartActivity.this);
//        ArrayList<Product> products = new ArrayList<>();
//        ArrayList<String> color = new ArrayList<>();
//        color.add("#000000");
//        products.add(new Product("092L0PpqaprCFh7bP3Ru", 1200000F, color, 1));
//
//        Map<String, Object> cart = new HashMap<>();
//        cart.put("id_user", "2DhfKW5XRl2NPYtmwq0I");
//        cart.put("092L0PpqaprCFh7bP3Ru-" + System.currentTimeMillis(), products);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewMore = findViewById(R.id.recyclerViewMore);



        db.collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progess.showDialog("Loading");
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // lấy ra giỏ hàng có id trùng với id của người dùng
                                if(document.getId().equals(userModel.getUserID())) {
                                    Map<String, Object> cart = document.getData();
                                    // trả về tất cả dữ liệu của giỏ hàng qua map là cart
                                    for (String key : cart.keySet()) {
                                        // check kiểm tra. Nếu cái key trong map là id_user thì bỏ qua
                                        // vì trong map có chứa id của người dùng và các item của giỏ hàng
                                        if(!key.equals("id_user")) {
                                            // nếu không phải là id thì
                                            // tiếp tục convert item của giỏ hàng qua 1 map tiếp (vì firebase trả về 1 map)
                                            // map nhận được có key là id sản phẩm + timeline
                                            Log.d("KEYDATA", "Sản phẩm: " + key);
                                            // bắt đầu convert Map qua ArrayList --
                                            Map<String, Object> itemCart = (Map<String, Object>) cart.get(key);
                                            Set<Map.Entry<String, Object>> entr = itemCart.entrySet();
                                            ArrayList<Map.Entry<String, Object>> listOf = new ArrayList<Map.Entry<String, Object>>(entr);
                                            // end - convert map qua ArrayList

                                            String id = null, name = null, color = null;
                                            Float price = 0F;
                                            Integer total = 1;
                                            for (int j = 0; j < listOf.size(); j++) {
                                                // chạy vòng lặp để gắn các dữ liệu từ map qua ArrayList
                                                Log.d("KEYDATA", listOf.get(j).getKey()+" : " + listOf.get(j).getValue());
                                                String result = String.valueOf(listOf.get(j).getValue());
                                                if(listOf.get(j).getKey().equals("id")) {
                                                    id = result;
                                                }
                                                if(listOf.get(j).getKey().equals("nameproduct")) {
                                                    name = result;
                                                }
                                                if(listOf.get(j).getKey().equals("price")) {
                                                    price = Float.parseFloat(result) ;
                                                }
                                                if(listOf.get(j).getKey().equals("total")) {
                                                    total = Integer.parseInt(result);
                                                }
                                                if(listOf.get(j).getKey().equals("total")) {
                                                    color = result;
                                                }
                                            }
                                            Log.d("KEYDATA", "-----------\n");
                                            listCart.add(new CartModel(id, name, color, price, total));
                                        }
                                    }
                                }

                            }
                            progess.hideDialog();

                        }
                        Log.d("LISTDATA", "onCreate: "+listCart.get(0).getNameProduct());
                        Log.d("LISTDATA", "list size: "+listCart.size());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "Lấy dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                        progess.hideDialog();
                    }
                });

//        db.collection("cart").document("2EhfKW5XRl2NPYtmwq0I")
//                .update(cart)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d("TAG>>>", "Thêm dữ liệu thành công " + unused);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("TAG>>>", "Thêm dữ liệu thất bạn ");
//                    }
//                });


    }

    public void findCart(String s) {
        progess.showDialog("Loading");
        db.collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String id_user = document.getString("id_user");
                            if (id_user.contains(s)) {
                                Log.d("TAG 1000", "onComplete: " + document.getData().get("id_product"));

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