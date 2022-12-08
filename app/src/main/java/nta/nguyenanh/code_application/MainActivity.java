package nta.nguyenanh.code_application;


import static nta.nguyenanh.code_application.MD5.MD5.getMd5;
import static nta.nguyenanh.code_application.SplashScreen.Flashsalelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nta.nguyenanh.code_application.dialog.DialogConfirm;
import nta.nguyenanh.code_application.fragment.main.FlashSaleFragment;
import nta.nguyenanh.code_application.fragment.main.HomeFragment;
import nta.nguyenanh.code_application.fragment.main.NotificationFragment;
import nta.nguyenanh.code_application.fragment.main.UserFragment;
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
    boolean isLoggedin = false;
    String username, password;

    ArrayList<Address> list = new ArrayList<>();

    FragmentManager manager;
    private BottomNavigationView bottomnavigation;
    private Product product;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginActivity.checkLogout = false;

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
        readlogin();
        if(isLoggedin == true) {
            oncheckLogin();
        }
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Date date = new Date();
            Log.d("TIME", "onReceive: "+date.getHours());
            if(date.getHours() == 23) {
                Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
                    fragment = new UserFragment();
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
        this.product = product;
        Intent intent = new Intent(MainActivity.this, DetailProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    public void readlogin(){
        SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        isLoggedin = preferences.getBoolean("isLoggedin",false);
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
            ArrayList<Address> listAddress = gson.fromJson(json, type);
            if(listAddress == null) {
                listAddress  = new ArrayList<>();
            }
            userModel = new User(listAddress, null, fullname, password, numberphone, username, userid);
        }
    }
    public void oncheckLogin() {
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("TAG>>>>>>", "onComplete: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshots = task.getResult();
                            Log.d("TAG>>>>>>", "onComplete: "+snapshots.size());
                            for (QueryDocumentSnapshot document : snapshots) {

                                username = String.valueOf(document.get("username"));
                                password = String.valueOf(document.get("password"));
//                                Log.d("TAG>>>>>>", "onComplete: " + username);
//                                Log.d("TAG>>>>>>", "onComplete: " + password);

                                if (username.equals(userModel.getUsername())) {
                                    new MainActivity.getAddress().getDataAddress(document);
                                    Log.d("TAG>>>>>>", "onComplete: " + userModel.getUsername());
                                    break;
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG>>>>>>", "onComplete: Thất bại khi lấy dữ liệu ");
                    }
                });
    }
    class getAddress {
        public void getDataAddress(QueryDocumentSnapshot doc) {
            db.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    // lấy ra tài khoản với id của người dùng
                                    if (document.getId().equals(doc.getId())) {
                                        Map<String, Object> address = document.getData();
                                        // trả về tất cả dữ liệu của địa chỉ qua map là address
                                        for (String key : address.keySet()) {
                                            // check kiểm tra. Nếu key là address thì tiếp tục công việc đọc -- nếu không thì bỏ qua
                                            // vì trong map có chứa id của người dùng và các item của giỏ hàng
                                            if (key.equals("address")) {
                                                // tiếp tục convert thằng cha address qua một arraylist
                                                // map nhận được có key là id sản phẩm + timeline
//                                                Log.d("KEYDATA", "Sản phẩm: " + key);
//                                                Log.d("KEYDATA", "Sản phẩm: " + address);
                                                // bắt đầu convert thằng cha qua ArrayList --
                                                Map<String, Object> itemA = new HashMap<>();
                                                Log.d("LIST ADDRESS", "onComplete: " + address.get("address"));
                                                if(address.get("address") == null) {
                                                    list = new ArrayList<>();
                                                    userModel = new User(
                                                            list,
                                                            doc.get("datebirth") + "",
                                                            doc.get("fullname") + "",
                                                            doc.get("password") + "",
                                                            doc.get("phonenumber") + "",
                                                            doc.get("username") + "",
                                                            doc.getId());
                                                    writeLogin(userModel);
                                                    readlogin();
                                                    break;
                                                }

                                                Map<String, Object> itemAddress = (Map<String, Object>) address.get("address");
                                                Set<Map.Entry<String, Object>> entr = itemAddress.entrySet();
                                                ArrayList<Map.Entry<String, Object>> listOf = new ArrayList<Map.Entry<String, Object>>(entr);
                                                // end - convert map qua ArrayList

                                                String place = null, nameReceiver = null, phonenumber = null;
                                                Integer available = 0;
                                                for (int j = 0; j < listOf.size(); j++) {
                                                    // chạy vòng lặp để gắn các dữ liệu từ map qua ArrayList
                                                    // chạy thằng cha để lấy ra thằng con address item
//                                                    Log.d("KEYDATA", listOf.get(j).getValue() + " : " + listOf.get(j).getValue());

                                                    Map<String, Object> item = (Map<String, Object>) listOf.get(j).getValue();
                                                    Set<Map.Entry<String, Object>> entrAd = item.entrySet();
                                                    ArrayList<Map.Entry<String, Object>> listOfAd = new ArrayList<Map.Entry<String, Object>>(entrAd);

                                                    for (int l = 0; l < listOfAd.size(); l++) {
                                                        String result = String.valueOf(listOfAd.get(l).getValue());
                                                        if (listOfAd.get(l).getKey().equals("phonenumber")) {
                                                            phonenumber = result;
                                                        }
                                                        if (listOfAd.get(l).getKey().equals("nameReceiver")) {
                                                            nameReceiver = result;
                                                        }
                                                        if (listOfAd.get(l).getKey().equals("address")) {
                                                            place = result;
                                                        }
                                                    }
                                                    list.add(new Address(listOf.get(j).getKey(),place, nameReceiver, phonenumber, available));
//                                                    Log.d("TAG address 2000", "onComplete: "+listOf.get(j).getKey());
                                                }

                                            }
                                        }
//                                        Log.d("KEYDATA", "-----------\n");
//                                        Log.d("LIST DATA", "onComplete: " + list.size());
//                                        Log.d("LIST DATA", "onComplete: " + list.get(0).getAddress());
//                                        Log.d("LIST DATA", "onComplete: " + list.get(0).getPhonenumber());
//                                        Log.d("LIST DATA", "onComplete: " + list.get(0).getNameReceiver());
                                        userModel = null;
                                        userModel = new User(
                                                list,
                                                doc.get("datebirth") + "",
                                                doc.get("fullname") + "",
                                                doc.get("password") + "",
                                                doc.get("phonenumber") + "",
                                                doc.get("username") + "",
                                                doc.getId());

//                                        Log.d("LIST----LIST", "onComplete: " + list.size());
                                        writeLogin(userModel);
//                                        break;
                                    }

                                }
//                                readlogin();

                            }

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                        }
                    });
        }
    }

    private void writeLogin(User user) {
        SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(user.getAddress());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedin", true);
        editor.putString("userid", user.getUserID());
        editor.putString("username", user.getUsername());
        editor.putString("fullname", user.getFullname());
        editor.putString("password", user.getPassword());
        editor.putString("address", json);
        editor.putString("numberphone", user.getPhonenumber());
        editor.commit();
    }


}