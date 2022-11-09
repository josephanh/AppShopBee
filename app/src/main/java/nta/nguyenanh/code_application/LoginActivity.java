package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MD5.MD5.getMd5;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nta.nguyenanh.code_application.model.UserModel;

public class LoginActivity extends AppCompatActivity {
    private EditText txt_name,txt_password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppCompatButton btn_login;
    private String username,password;
    private CheckBox chktkmk;
    private TextView txtdangkyngay;
    private Button btngoogle,btnfacebook;
    //google
    GoogleSignInClient gsc ;
    private String tempmail=null;
    private String idfacebook = null;
    String username2;
    //facebook
    GraphRequest request;
    ProfileTracker profileTracker;
    CallbackManager callbackManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
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
        gsc = GoogleSignIn.getClient(LoginActivity.this,gso);
        //kiểm tra có login google hay chưa
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if (account!=null){
            Intent homeintent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(homeintent);
            finish();
        }
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
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //facebook

        callbackManager = CallbackManager.Factory.create();
        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("openid","public_profile","email"));
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                Profile.getCurrentProfile().getId();
                Log.d("TAG", "facebook:onSuccess:" + Profile.getCurrentProfile().getId());
                Map<String, Object> item = new HashMap<>();
                item.put("username",  Profile.getCurrentProfile().getId());
                item.put("address",null);
                item.put("phonenumber",null);
                item.put("fullname",Profile.getCurrentProfile().getName());
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
                                        if ( Profile.getCurrentProfile().getId().equals(username2)) {
                                            Toast.makeText(LoginActivity.this, "Login thành công", Toast.LENGTH_SHORT).show();
                                            Intent homeintent = new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(homeintent);
                                            finish();
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
                        Log.d(">>>>>>TAG","onActivityResult"+email);
                        String name = account.getDisplayName();
                        Log.d(">>>>>>TAG","Name :"+name);
                        tempmail =email;
                        Log.d("TAG", tempmail);
                        Map<String, Object> item = new HashMap<>();
                        item.put("username", tempmail);
                        item.put("phonenumber",null);
                        item.put("address",null);
                        item.put("fullname",name);
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
                                                if (tempmail.equals(username2)) {
                                                    Toast.makeText(LoginActivity.this, "Login thành công", Toast.LENGTH_SHORT).show();
                                                    Intent homeintent = new Intent(LoginActivity.this,MainActivity.class);
                                                    startActivity(homeintent);
                                                    finish();
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
                    }catch (Exception e){
                        Log.d(">>>>>TAG","onActivityResult" +e.getMessage());
                    }
                }
            }
    );

    @Override
    protected void onResume() {
//        if (chktkmk.isChecked()){
//            readlogin();
//        }

        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        profileTracker.stopTracking();
    }
    public void readlogin(){
//        SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
//        Boolean isLoggedin = preferences.getBoolean("isLoggedin",false);
//        if (isLoggedin){
//            Intent homeintent =new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(homeintent);
//            finish();
//        }
    }
    public void oncheckLogin(View v) {
        if (txt_name.getText().toString().isEmpty()||txt_password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Không được để trống tài khoản ,mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot snapshots = task.getResult();
                            for (QueryDocumentSnapshot document : snapshots) {
                                 username = document.get("username").toString();
                                 password = document.get("password").toString();
                                Log.d("TAG", "onComplete: "+username);
                                Log.d("TAG", "onComplete: "+password);
                                if (txt_name.getText().toString().equals(username) && getMd5(txt_password.getText().toString()).equals(password)) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;

                                }
                            }

                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}