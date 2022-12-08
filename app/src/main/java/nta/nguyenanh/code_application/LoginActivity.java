package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MD5.MD5.getMd5;
import static nta.nguyenanh.code_application.MainActivity.userModel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nta.nguyenanh.code_application.adapter.CartAdapter;
import nta.nguyenanh.code_application.listener.CheckLogin;
import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.ProductCart;
import nta.nguyenanh.code_application.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText txt_name, txt_password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppCompatButton btn_login;
    private String username, password;
    private CheckBox chktkmk;
    private TextView txtdangkyngay;
    private Button btngoogle, btnfacebook;
    //google
    GoogleSignInClient gsc;
    private String tempmail = null;
    private String idfacebook = null;
    String username2;
    boolean checklogin = false;
    boolean checkUser = false;
    public static boolean checkLogout;
    //facebook
    CallbackManager callbackManager;
    ArrayList<Address> addressList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        txt_name = findViewById(R.id.txt_name);
        txt_password = findViewById(R.id.txt_password);
        chktkmk = findViewById(R.id.chktkmk);
        txtdangkyngay = findViewById(R.id.txtdangkyngay);
        btngoogle = findViewById(R.id.btngoogle);
        btnfacebook = findViewById(R.id.btnfacebook);

        //google
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(LoginActivity.this, gso);
        //kiểm tra có login google hay chưa
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
//        if (account!=null){
//            Intent homeintent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(homeintent);
//            finish();
//        }
        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = gsc.getSignInIntent();
                googleLauncher.launch(googleIntent);
            }
        });
        txtdangkyngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //facebook

        callbackManager = CallbackManager.Factory.create();
        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("openid", "public_profile", "email"));
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                Profile.getCurrentProfile().getId();
                Log.d("TAG", "facebook:onSuccess:" + Profile.getCurrentProfile().getId());
                Map<String, Object> item = new HashMap<>();
                item.put("username", Profile.getCurrentProfile().getId());
                item.put("address", null);
                item.put("phonenumber", null);
                item.put("fullname", Profile.getCurrentProfile().getName());
                db.collection("user")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot snapshots = task.getResult();
                                    for (QueryDocumentSnapshot document : snapshots) {
                                        username2 = document.get("username").toString();
                                        Log.d("TAG", "onComplete: " + username2);
                                        Log.d("TAG", "onComplete2: " + username);
                                        if (Profile.getCurrentProfile().getId().equals(username2)) {
                                            Toast.makeText(LoginActivity.this, "Login thành công", Toast.LENGTH_SHORT).show();
//                                            Intent homeintent = new Intent(LoginActivity.this,MainActivity.class);
//                                            startActivity(homeintent);
//                                            finish();
                                            return;
                                        }
                                    }
                                    db.collection("user")
                                            .add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(LoginActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(LoginActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }

                        });
//                Intent homeintent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(homeintent);
//                finish();
            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError", error);
            }
        });


    }

    ActivityResultLauncher<Intent> googleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        String email = account.getEmail();
                        Log.d(">>>>>>TAG", "onActivityResult" + email);
                        String name = account.getDisplayName();
                        Log.d(">>>>>>TAG", "Name :" + name);
                        tempmail = email;
                        Log.d("TAG", tempmail);
                        Map<String, Object> item = new HashMap<>();
                        item.put("username", tempmail);
                        item.put("phonenumber", null);
                        item.put("address", null);
                        item.put("fullname", name);

                        db.collection("user")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot snapshots = task.getResult();
                                            for (QueryDocumentSnapshot document : snapshots) {
                                                username2 = document.get("username").toString();
//                                                Log.d("TAG", "onComplete: " + username2);
//                                                Log.d("TAG", "onComplete2: " + username);
                                                if (tempmail.equals(username2)) {
//                                                    writeLogin((User) document.getData());
                                                    new getAddress().getDataAddress(document);
                                                    checklogin = true;
                                                    onBackPressed();

                                                }
                                            }
                                            if (!checklogin) {
                                                db.collection("user")
                                                        .add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                addCartUser(documentReference.getId());
                                                                Log.d("ADD DATA", "onSuccess: Thêm thành công");
                                                                onBackPressed();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(LoginActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }

                                });
                    } catch (Exception e) {
                        Log.d(">>>>>TAG", "onActivityResult" + e.getMessage());
                    }
                }
            }
    );

    @Override
    protected void onResume() {
//        readlogin();
        new CheckLogin(LoginActivity.this).readLogin();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        profileTracker.stopTracking();
    }

    // lưu trạng thái login vào shored preferences
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

    // đọc trạng thái login
//    public void readlogin() {
//        SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
//        Boolean isLoggedin = preferences.getBoolean("isLoggedin", false);
//        if (isLoggedin) {
//            String userid = preferences.getString("userid", null);
//            String username = preferences.getString("username", null);
//            String fullname = preferences.getString("fullname", null);
//            String password = preferences.getString("password", null);
//
//            String numberphone = preferences.getString("numberphone", null);
//            userModel = new User(address, null, fullname, password, numberphone, username, userid);
//            onBackPressed();
//        }
//    }

    public void oncheckLogin(View v) {


        if (txt_name.getText().toString().isEmpty() || txt_password.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Không được để trống tài khoản ,mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
            if (txt_name.getText().toString().length() ==10){
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
                                Log.d("TAG>>>>>>", "onComplete: " + username);
                                Log.d("TAG>>>>>>", "onComplete: " + password);
                                if (txt_name.getText().toString().equals(username) && getMd5(txt_password.getText().toString()).equals(password)) {
                                    new getAddress().getDataAddress(document);
                                    checkUser = true;
                                    break;
                                }
                            }
                            if(!checkUser) {
                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG>>>>>>", "onComplete: Thất bại khi lấy dữ liệu ");
                    }
                });
        }else {
                Toast.makeText(this, "Tối đa 10 số", Toast.LENGTH_SHORT).show();
            }

    }
    private class getAddress {
        private void getDataAddress(QueryDocumentSnapshot doc) {
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
                                                Log.d("KEYDATA", "Sản phẩm: " + key);
                                                Log.d("KEYDATA", "Sản phẩm: " + address);
                                                // bắt đầu convert thằng cha qua ArrayList --
                                                Log.d("LIST ADDRESS", "onComplete: " + address.get("address"));
                                                if(address.get("address") == null) {
                                                    addressList = null;
                                                    userModel = new User(
                                                            addressList,
                                                            doc.get("datebirth") + "",
                                                            doc.get("fullname") + "",
                                                            doc.get("password") + "",
                                                            doc.get("phonenumber") + "",
                                                            doc.get("username") + "",
                                                            doc.getId());
                                                    writeLogin(userModel);
                                                    onBackPressed();
                                                    return;
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
                                                    Log.d("KEYDATA", listOf.get(j).getValue() + " : " + listOf.get(j).getValue());

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
                                                    addressList.add(new Address(listOf.get(j).getKey(),place, nameReceiver, phonenumber, available));
                                                    Log.d("TAG address 2000", "onComplete: "+listOf.get(j).getKey());
                                                }

                                            }
                                        }
//                                        Log.d("KEYDATA", "-----------\n");
//                                        Log.d("LIST DATA", "onComplete: " + addressList.size());
//                                        Log.d("LIST DATA", "onComplete: " + addressList.get(0).getAddress());
//                                        Log.d("LIST DATA", "onComplete: " + addressList.get(0).getPhonenumber());
//                                        Log.d("LIST DATA", "onComplete: " + addressList.get(0).getNameReceiver());
                                        userModel = new User(
                                                addressList,
                                                doc.get("datebirth") + "",
                                                doc.get("fullname") + "",
                                                doc.get("password") + "",
                                                doc.get("phonenumber") + "",
                                                doc.get("username") + "",
                                                doc.getId());

                                        Log.d("LIST----LIST", "onComplete: " + addressList.size());
                                        writeLogin(userModel);
                                        onBackPressed();
                                        break;
                                    }
                                }

                            }

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                        }
                    });
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    public void addCartUser(String id) {
        Map<String, Object> cart = new HashMap<>();
        cart.put("id_user", id);
        db.collection("cart").document(id)
                .set(cart)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG>>>", "Thêm dữ liệu thành công ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG>>>", "Thêm dữ liệu thất bại");
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if(checkLogout) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}