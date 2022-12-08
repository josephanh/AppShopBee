package nta.nguyenanh.code_application.fragment.otp;

import static nta.nguyenanh.code_application.MD5.MD5.getMd5;
import static nta.nguyenanh.code_application.listener.FirebaseQuery.USERS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.transition.TransitionManager;
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

    OnBackPressedCallback onBackPressedCallback;
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
    private ViewGroup tContainer;
    private Button btn_register_error;
    private int score = 0;

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
        txt_newname = view.findViewById(R.id.txt_newname);
        txt_newpassword = view.findViewById(R.id.txt_newpassword);
        txt_confirmpassword = view.findViewById(R.id.txt_confirmpassword);
        txtdangnhapngay = view.findViewById(R.id.txtdangnhapngay);
        btn_register_error = view.findViewById(R.id.btn_register_error);
        tContainer = view.findViewById(R.id.tContainer1);
        btn_register = view.findViewById(R.id.btn_register);
        TransitionManager.beginDelayedTransition(tContainer);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onRegister();
                    int a = check(score);
                    if (a == 0 ){
                        score++;
                    }
                    Log.d(">>>>TAG:", ""+score);
            }
        });
        setOnBackPressedCallback();
    }

    @SuppressLint("SetTextI18n")
    private int check(int score){
        CountDownTimer count;
        username = txt_newname.getText().toString();
        password = txt_newpassword.getText().toString();
        confirmpassword = txt_confirmpassword.getText().toString();
        int checka = 0;
        int checkb = 0;
            switch (score){
                case 0:
                    if (username.isEmpty()) {
                        checka = 1;
                        checkb = 1;

                    }else if (username.length() <= 6){
                        checka = 1;
                        checkb = 4;
                    }else {
                        TransitionManager.beginDelayedTransition(tContainer);
                        txt_newname.setVisibility(View.GONE);
                        txt_confirmpassword.setVisibility(View.GONE);
                        txt_newpassword.setVisibility(View.VISIBLE);
                        txt_newpassword.setText("");
                    }
                    break;
                case 1:
                    if (password.isEmpty()){
                        checka = 1;
                        checkb = 2;
                    }
                    else {
                        TransitionManager.beginDelayedTransition(tContainer);
                        txt_newname.setVisibility(View.GONE);
                        txt_newpassword.setVisibility(View.VISIBLE);
                        txt_confirmpassword.setVisibility(View.VISIBLE);
                        txt_confirmpassword.setText("");
                    }
                    break;
                case 2:
                    if (confirmpassword.isEmpty()){
                        checka = 1;
                        checkb = 3;
                    }
                    else if (!password.equals(confirmpassword)) {
                        checka = 2;
                    }else {
                        onRegister();
                        Log.d(">>>>TAG", ""+phonenumber);
                        Log.d(">>>>TAG", ""+txt_newname.getText().toString());
                        Log.d(">>>>TAG", ""+txt_newpassword.getText().toString());
                        Log.d(">>>>TAG", ""+txt_confirmpassword.getText().toString());
                    }
                    break;
            }

        switch (checka){
            case 1:
                switch (checkb){
                    case 1:
                        TransitionManager.beginDelayedTransition(tContainer);
                        btn_register.setVisibility(View.GONE);
                        btn_register_error.setVisibility(View.VISIBLE);
                        btn_register_error.setText("Họ tên trống");
                        count = new CountDownTimer(1+1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                TransitionManager.beginDelayedTransition(tContainer);
                                btn_register.setVisibility(View.VISIBLE);
                                btn_register_error.setVisibility(View.GONE);
                                btn_register_error.setText("");
                            }
                        };
                        count.start();
                        break;
                    case 2:
                        TransitionManager.beginDelayedTransition(tContainer);
                        btn_register.setVisibility(View.GONE);
                        btn_register_error.setVisibility(View.VISIBLE);
                        btn_register_error.setText("Mật khẩu trống");
                        count = new CountDownTimer(1+1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                TransitionManager.beginDelayedTransition(tContainer);
                                btn_register.setVisibility(View.VISIBLE);
                                btn_register_error.setVisibility(View.GONE);
                                btn_register_error.setText("");
                            }
                        };
                        count.start();
                        break;

                    case 3:
                        TransitionManager.beginDelayedTransition(tContainer);
                        btn_register.setVisibility(View.GONE);
                        btn_register_error.setVisibility(View.VISIBLE);
                        btn_register_error.setText("Nhập lại mật khẩu trống");
                        count = new CountDownTimer(1+1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                TransitionManager.beginDelayedTransition(tContainer);
                                btn_register.setVisibility(View.VISIBLE);
                                btn_register_error.setVisibility(View.GONE);
                                btn_register_error.setText("");
                            }
                        };
                        count.start();
                        break;
                    case 4:
                        TransitionManager.beginDelayedTransition(tContainer);
                        btn_register.setVisibility(View.GONE);
                        btn_register_error.setVisibility(View.VISIBLE);
                        btn_register_error.setText("Họ tên quá ngắn");
                        count = new CountDownTimer(1+1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                TransitionManager.beginDelayedTransition(tContainer);
                                btn_register.setVisibility(View.VISIBLE);
                                btn_register_error.setVisibility(View.GONE);
                                btn_register_error.setText("");
                            }
                        };
                        count.start();
                        break;
                }
                break;
            case 2:
                TransitionManager.beginDelayedTransition(tContainer);
                btn_register.setVisibility(View.GONE);
                btn_register_error.setVisibility(View.VISIBLE);
                btn_register_error.setText("Mật khẩu không trùng");
                count = new CountDownTimer(1+1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        TransitionManager.beginDelayedTransition(tContainer);
                        btn_register.setVisibility(View.VISIBLE);
                        btn_register_error.setVisibility(View.GONE);
                        btn_register_error.setText("");
                    }
                };
                count.start();
                break;
        }
        return checka;
    }

    private void setOnBackPressedCallback(){
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(score >= 1){
                    score--;
                    TransitionManager.beginDelayedTransition(tContainer);
                    txt_newname.setVisibility(View.GONE);
                    txt_confirmpassword.setVisibility(View.GONE);
                    txt_newpassword.setVisibility(View.VISIBLE);
                    Log.d(">>>>TAG:", "1:"+score);
                }
                if (score == 0){
                    score--;
                    TransitionManager.beginDelayedTransition(tContainer);
                    txt_confirmpassword.setVisibility(View.GONE);
                    txt_newpassword.setVisibility(View.GONE);
                    txt_newname.setVisibility(View.VISIBLE);
                    Log.d(">>>>TAG:", "2:"+score);
                }
                if (score < 0){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.from_left, R.animator.to_right)
                            .replace(R.id.framelayout_container_register, NumberPhoneFragment.newInstance(phonenumber),"NumberPhoneFragment").commit();
                }
            }

        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);
    }

    public void onRegister() {
        username = txt_newname.getText().toString();
        password = txt_newpassword.getText().toString();
        confirmpassword = txt_confirmpassword.getText().toString();
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