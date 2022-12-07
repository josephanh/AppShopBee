package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MD5.MD5.getMd5;
import static nta.nguyenanh.code_application.listener.FirebaseQuery.USERS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nta.nguyenanh.code_application.fragment.otp.NumberPhoneFragment;
import nta.nguyenanh.code_application.fragment.otp.RegisterFragment;
import nta.nguyenanh.code_application.model.User;


public class RegisterActivity extends AppCompatActivity {

//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private EditText txt_newname,txt_newpassword,txt_confirmpassword, txtsodienthoai;
//    private TextView txtdangnhapngay;
//    private String username2;
//    private Button btn_register;
//    private String username;
//    private String password;
//    private String confirmpassword;
//    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        txt_newname = findViewById(R.id.txt_newname);
//        txt_newpassword = findViewById(R.id.txt_newpassword);
//        txt_confirmpassword = findViewById(R.id.txt_confirmpassword);
//        txtsodienthoai = findViewById(R.id.txtsodienthoai);
//        txtdangnhapngay = findViewById(R.id.txtdangnhapngay);
//        btn_register = findViewById(R.id.btn_register);
//        txtdangnhapngay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onRegister();
//            }
//        });
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_container_register, NumberPhoneFragment.class, null).commit();

    }

//    public void onRegister() {
//        username = txt_newname.getText().toString();
//        password = txt_newpassword.getText().toString();
//        confirmpassword = txt_confirmpassword.getText().toString();
//        phonenumber = txtsodienthoai.getText().toString();
//        if (username.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
//            Toast.makeText(this, "Không được để trống username,password,confirmpassword", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!password.equals(confirmpassword)) {
//            Toast.makeText(this, "Mật khẩu không trùng", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        db.collection("user")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot snapshots = task.getResult();
//                            for (QueryDocumentSnapshot document : snapshots) {
//                                username2 = document.get("username").toString();
//                                Log.d("TAG", "onComplete: " + username2);
//                                Log.d("TAG", "onComplete2: " + username);
//                                if (username.equals(username2)) {
//                                    Toast.makeText(RegisterActivity.this, "Tài khoản bị trùng", Toast.LENGTH_SHORT).show();
//
//                                    return;
//                                }
//                            }
//                            Map<String, Object> item = new HashMap<>();
//                            item.put("username", phonenumber);
//                            item.put("fullname", username);
//                            item.put("password", getMd5(password));
//                            item.put("address",null);
//                            item.put("phonenumber", phonenumber);
//                            db.collection("user")
//                                    .add(item)
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                        @Override
//                                        public void onSuccess(DocumentReference documentReference) {
//                                            Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
//                                            addCartUser(documentReference.getId());
//                                            signUp(documentReference.getId(),txt_newname.getText().toString());
//                                            txt_newname.setText("");
//                                            txt_newpassword.setText("");
//                                            txt_confirmpassword.setText("");
//                                            txtsodienthoai.setText("");
//                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(RegisterActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        }
//                    }
//
//                });
//    }
//    public void readdata(){
//        db.collection("user")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            ArrayList<User> list = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Map<String,Object> map = document.getData();
//                                String username = map.get("username").toString();
//                                String password = map.get("password").toString();
//
//                                User userModel =new User(-1, null,null,null,null,password,null,username);
//                                userModel.setUserID(document.getId());
//                                list.add(userModel);
//                            }
//
//                        } else {
//
//                        }
//                    }
//                });
//    }
//    public void addCartUser(String id) {
//        Map<String, Object> cart = new HashMap<>();
//        cart.put("id_user", id);
//        db.collection("cart").document(id)
//                .set(cart)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d("TAG>>>", "Thêm dữ liệu thành công ");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("TAG>>>", "Thêm dữ liệu thất bại");
//                    }
//                });
//    }
//    public void signUp(String id,String name) {
//        // get all value in edit text
//
//        // dont forget validating value
//
//        // pls do it yourself
//
//        // Firstly, we check username is exits or not
//
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference users = database.getReference(USERS).child(id);
//
//        users.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                // chua co user voi username duoc nhap
//                if (dataSnapshot.getValue() == null) {
//
//                    // hoc vien tu khoi tao model User
//                    User user = new User();
//                    user.setUsername(id);
//                    user.setFullname(name);
//
//
//                    // them user vao nhanh Users
//                    users.setValue(user, new DatabaseReference.CompletionListener() {
//
//                        @Override
//                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//
//                            // hoc vien tu viet va kiem tra su kien loi va thanh cong
//                            if (databaseError == null) {
//                                String username = databaseReference.getRef().getKey();
//                                Intent intent = new Intent();
//                                intent.putExtra("data", username);
//                                setResult(999, intent);
//
//                                Toast.makeText(RegisterActivity.this,
//                                        getString(R.string.notify_create_user_successful), Toast.LENGTH_SHORT).show();
//                                finish();
//                            } else {
//                                Toast.makeText(RegisterActivity.this,
//                                        getString(R.string.notify_create_user_failure), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                    // username da ton tai, thong bao chon username khac
//                } else {
//                    Toast.makeText(RegisterActivity.this,
//                            getString(R.string.notify_user_is_exits), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }

}