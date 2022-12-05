package nta.nguyenanh.code_application.fragment.otp;

import static nta.nguyenanh.code_application.MD5.MD5.getMd5;
import static nta.nguyenanh.code_application.listener.FirebaseQuery.USERS;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import nta.nguyenanh.code_application.LoginActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private static final String ARG_NUMBERPHONE = "numberphone";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText txt_newname,txt_newpassword,txt_confirmpassword, txtsodienthoai;
    private TextView txtdangnhapngay;
    private String username2;
    private Button btn_register;
    private String username;
    private String password;
    private String confirmpassword;
    private String phonenumber;

    // TODO: Rename and change types of parameters
    public RegisterFragment() {
    }

    public static RegisterFragment newInstance(String numberphone) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBERPHONE, numberphone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mPhoneNumber = getArguments().getString(ARG_NUMBERPHONE);
            if (mPhoneNumber.length() == 9){
                phonenumber = "0"+mPhoneNumber;
            }else {
                phonenumber = mPhoneNumber;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        });
    }

    public void onRegister() {
        username = txt_newname.getText().toString();
        password = txt_newpassword.getText().toString();
        confirmpassword = txt_confirmpassword.getText().toString();
        if (username.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
            Toast.makeText(getActivity(), "Không được để trống username,password,confirmpassword", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmpassword)) {
            Toast.makeText(getActivity(), "Mật khẩu không trùng", Toast.LENGTH_SHORT).show();
            return;
        }
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
                                if (username.equals(username2)) {
                                    Toast.makeText(getActivity(), "Tài khoản bị trùng", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            Map<String, Object> item = new HashMap<>();
                            item.put("username", phonenumber);
                            item.put("fullname", username);
                            item.put("password", getMd5(password));
                            item.put("address",null);
                            item.put("phonenumber", phonenumber);
                            db.collection("user")
                                    .add(item)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                            addCartUser(documentReference.getId());
                                            signUp(documentReference.getId(),txt_newname.getText().toString());
                                            txt_newname.setText("");
                                            txt_newpassword.setText("");
                                            txt_confirmpassword.setText("");
                                            txtsodienthoai.setText("");
                                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                });
    }
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
    public void signUp(String id,String name) {
        // get all value in edit text

        // dont forget validating value

        // pls do it yourself

        // Firstly, we check username is exits or not

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference(USERS).child(id);

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // chua co user voi username duoc nhap
                if (dataSnapshot.getValue() == null) {

                    // hoc vien tu khoi tao model User
                    User user = new User();
                    user.setUsername(id);
                    user.setFullname(name);


                    // them user vao nhanh Users
                    users.setValue(user, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            // hoc vien tu viet va kiem tra su kien loi va thanh cong
                            if (databaseError == null) {
                                String username = databaseReference.getRef().getKey();
                                Intent intent = new Intent();
                                intent.putExtra("data", username);
                                getActivity().setResult(999, intent);
                                Toast.makeText(getActivity(), getString(R.string.notify_create_user_successful), Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.notify_create_user_failure), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // username da ton tai, thong bao chon username khac
                } else {
                    Toast.makeText(getActivity(), getString(R.string.notify_user_is_exits), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}