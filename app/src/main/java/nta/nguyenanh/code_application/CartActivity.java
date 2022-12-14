package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nta.nguyenanh.code_application.adapter.CartAdapter;
import nta.nguyenanh.code_application.bottomsheet.BottomSheetGoToPay;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.dialog.DialogConfirm;
import nta.nguyenanh.code_application.interfaces.OnclickItemCart;
import nta.nguyenanh.code_application.model.ProductCart;

public class CartActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DiaLogProgess progess;
    ArrayList<ProductCart> listCart = new ArrayList<>();

    RecyclerView recyclerViewCart, recyclerViewMore;
    CartAdapter adapterCart;
    Toolbar toolbar;
    LinearLayoutManager manager;
    Float totalMoney = 0F;

    TextView totalMoneyProduct, shipMoney, voucher, totalMoneys;
    Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        progess = new DiaLogProgess(CartActivity.this);


        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewMore = findViewById(R.id.recyclerViewMore);

        toolbar = findViewById(R.id.toolbar);

        totalMoneyProduct = findViewById(R.id.totalMoneyProduct);
        shipMoney = findViewById(R.id.shipMoney);
        voucher = findViewById(R.id.voucher);
        totalMoneys = findViewById(R.id.totalMoney);
        btn_continue = findViewById(R.id.btn_continue);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Giỏ hàng của tôi");
        getSupportActionBar().setSubtitle(userModel.getFullname());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel == null) {
                    Toast.makeText(CartActivity.this, "Cần đăng nhập trước khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                } else {
                    if(listCart != null) {
                        if(userModel.getAddress() == null || userModel.getAddress().size() == 0) {
                            Intent intent = new Intent(CartActivity.this, PayActivity.class);
                            intent.putExtra("addressCheck", false);
                            startActivity(intent);
                        } else {
                            Log.d("TAG>>>>>", "onClick: "+userModel.getAddress());
                            Intent intent = new Intent(CartActivity.this, PayActivity.class);
                            intent.putExtra("addressCheck", true);
                            intent.putExtra("listPay", listCart);
                            startActivity(intent);
                        }
                    } else {
                        return;
                    }

                }
            }
        });

        manager = new LinearLayoutManager(CartActivity.this);
        getDataCart();

    }




    public void changeTotal(int total, int oldTotal, float price){
        if(total < oldTotal) {
            totalMoney = totalMoney - (oldTotal - total)*price;
        } else {
            totalMoney = totalMoney + (total - oldTotal)*price;
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        totalMoneyProduct.setText(formatter.format(totalMoney)+"đ");
        shipMoney.setText("30.000đ");
        voucher.setText("-"+formatter.format(totalMoney*0.02)+"đ");
        totalMoneys.setText(formatter.format((totalMoney*0.98)+30000)+"đ");
    }

    public void changeChecked(boolean check,int total, float price) {
        if(check) {
            totalMoney = totalMoney + total*price;
        } else {
            totalMoney = totalMoney - total*price;
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        totalMoneyProduct.setText(formatter.format(totalMoney)+"đ");
        if(totalMoney != 0) {
            shipMoney.setText("30.000đ");
            totalMoneys.setText(formatter.format((totalMoney*0.98)+30000)+"đ");
        } else {
            shipMoney.setText("0đ");
            totalMoneys.setText("0đ");
        }
        voucher.setText("-"+formatter.format(totalMoney*0.02)+"đ");
    }

    public void getDataCart() {
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

                                            String id = null, name = null, color = null, image = null;
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
                                                if(listOf.get(j).getKey().equals("color")) {
                                                    color = result;
                                                }

                                                if(listOf.get(j).getKey().equals("image")) {
                                                    image = result;
                                                }

                                            }
                                            Log.d("KEYDATA", "-----------\n");
                                            listCart.add(new ProductCart(id, name, image, price,color, total, key));
                                            totalMoney += price*total;
                                        }

                                    }
                                }

                            }
                            progess.hideDialog();
                            setDataMoney();
                            adapterCart = new CartAdapter(listCart, CartActivity.this, new OnclickItemCart() {
                                @Override
                                public void onClickMinus(int totalNew, int totalOld, float price) {
                                    changeTotal(totalNew, totalOld, price);
                                    setDataMoney();
                                }

                                @Override
                                public void onClickPlus(int totalNew, int totalOld, float price) {
                                    changeTotal(totalNew, totalOld, price);
                                    setDataMoney();
                                }

                                @Override
                                public void onClickCheck(boolean isCheck, int total, float price) {
                                    changeChecked(isCheck, total, price);
                                    setDataMoney();
                                }

                                @Override
                                public void hideCheck(CheckBox checkBox, ImageView imageView) {

                                }

                                @Override
                                public void onClickDelete(String id, int position) {
                                    if (listCart.size() > 0){
                                        Map<String,Object> updates = new HashMap<>();
                                        updates.put(listCart.get(position).getIdItemCart(), FieldValue.delete());
                                        db.collection("cart").document(userModel.getUserID())
                                                .update(updates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        totalMoney = (totalMoney - listCart.get(position).getPrice());
                                                        listCart.remove(position);
                                                        adapterCart.notifyDataSetChanged();
                                                        setDataMoney();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }
                                }
                            });
                            recyclerViewCart.setLayoutManager(manager);
                            recyclerViewCart.setAdapter(adapterCart);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "Lấy dữ liệu lỗi", Toast.LENGTH_SHORT).show();
                        progess.hideDialog();
                    }
                });

    }

    private void setDataMoney(){
        if (listCart.size() < 1){
            Log.d(">>>>TAG:", ""+listCart.size());
            totalMoneyProduct.setText("0đ");
            shipMoney.setText("0đ");
            voucher.setText("0đ");
            totalMoneys.setText("0đ");
        }else {
            Log.d(">>>>TAG:", ""+listCart.size());
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            totalMoneyProduct.setText(formatter.format(totalMoney)+"đ");
            shipMoney.setText("30.000đ");
            voucher.setText("-"+formatter.format(totalMoney*0.02)+"đ");
            totalMoneys.setText(formatter.format((totalMoney*0.98)+30000)+"đ");
        }
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
    private void codeTest(){
        //        ArrayList<Product> products = new ArrayList<>();
        //        ArrayList<String> color = new ArrayList<>();
        //        color.add("#000000");
        //        products.add(new Product("092L0PpqaprCFh7bP3Ru", 1200000F, color, 1));
        //
        //        Map<String, Object> cart = new HashMap<>();
        //        cart.put("id_user", "2DhfKW5XRl2NPYtmwq0I");
        //        cart.put("092L0PpqaprCFh7bP3Ru-" + System.currentTimeMillis(), products);
        //
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
}