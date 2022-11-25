package nta.nguyenanh.code_application;


import static nta.nguyenanh.code_application.helper.FirebaseQuery.USERS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nta.nguyenanh.code_application.helper.FirebaseQuery;
import nta.nguyenanh.code_application.model.User2;


public class LoginActivity2 extends AppCompatActivity {

    private EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

    }

    public void signIn(View view) {

        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        if (username.isEmpty() | password.isEmpty()) {
            Toast.makeText(this, getString(R.string.notify_user_is_not_exists), Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // truy vấn vào nhánh username mà người dùng nhập
        DatabaseReference users = firebaseDatabase.getReference(USERS).child(username);

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {

                    Toast.makeText(LoginActivity2.this,
                            getString(R.string.notify_user_is_not_exists), Toast.LENGTH_SHORT).show();
                } else {

                    // lấy dữ liệu từ dataSnapshot gán vào model User,
                    // lưu ý : biến ở User cần trùng khớp với tên các giá trị trên firebase
                    User2 user = dataSnapshot.getValue(User2.class);

                    if (user.username.equals(username)) {
                        FirebaseQuery.USERNAME = user.username;
//                        startActivity(new Intent(LoginActivity2.this, HomeActivity.class));

                    } else {

                        Toast.makeText(LoginActivity2.this, getString(R.string.notify_wrong_password), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void signUp(View view) {

        startActivityForResult(new Intent(this, SignUpActivity.class), 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 & resultCode == RESULT_OK) {
            String username = data.getStringExtra("data");
            edtUsername.setText(username);
            edtPassword.requestFocus();
        }
    }
}
