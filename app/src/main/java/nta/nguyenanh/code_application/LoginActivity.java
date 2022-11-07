package nta.nguyenanh.code_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_name = findViewById(R.id.txt_name);
        txt_password = findViewById(R.id.txt_password);

    }
    public void oncheckLogin(View v) {
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot snapshots = task.getResult();
                            for (QueryDocumentSnapshot document : snapshots) {

                                String username = document.get("username").toString();
                                String password = document.get("password").toString();
                                Log.d("TAG", "onComplete: "+username);
                                Log.d("TAG", "onComplete: "+password);
                                if (txt_name.getText().toString().isEmpty()||txt_password.getText().toString().isEmpty()){
                                    Toast.makeText(LoginActivity.this, "Không được để trống tài khoản ,mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                                if (txt_name.getText().toString().equals(username) && txt_password.getText().toString().equals(password)) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    break;
                                } else {
                                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
    }
}