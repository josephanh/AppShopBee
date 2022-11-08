package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MD5.MD5.getMd5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.ArrayList;
import java.util.Map;

import nta.nguyenanh.code_application.model.UserModel;

public class LoginActivity extends AppCompatActivity {
    private EditText txt_name,txt_password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppCompatButton btn_login;
    private String username,password;
    private CheckBox chktkmk;
    private TextView txtdangkyngay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_name = findViewById(R.id.txt_name);
        txt_password = findViewById(R.id.txt_password);
        chktkmk = findViewById(R.id.chktkmk);
        txtdangkyngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        if (chktkmk.isChecked()){
            readlogin();
        }
        super.onResume();
    }

    public void readlogin(){
        SharedPreferences preferences = getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        Boolean isLoggedin = preferences.getBoolean("isLoggedin",false);
        if (isLoggedin){
            Intent homeintent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(homeintent);
            finish();
        }
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
}