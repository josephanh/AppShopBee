package nta.nguyenanh.code_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nta.nguyenanh.code_application.model.UserModel;

public class RegisterActivity extends AppCompatActivity {
    private EditText txt_newname,txt_newpassword,txt_confirmpassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppCompatButton btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_newname = findViewById(R.id.txt_newname);
        txt_newpassword = findViewById(R.id.txt_newpassword);
        txt_confirmpassword = findViewById(R.id.txt_confirmpassword);
    }
    public void readdata(){
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<UserModel> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> map = document.getData();
                                String username = map.get("username").toString();
                                String password = map.get("password").toString();

                                UserModel userModel =new UserModel(-1,null,null,null,password,null,username);
                                userModel.setUserID(document.getId());
                                list.add(userModel);
                            }

                        } else {

                        }
                    }
                });
    }
    public void onRegister(View v){
        String username = txt_newname.getText().toString();
        String password = txt_newpassword.getText().toString();
        String confirmpassword = txt_confirmpassword.getText().toString();
        if (username.isEmpty()||password.isEmpty()||confirmpassword.isEmpty()) {
            Toast.makeText(this, "Không được để trống username,password,confirmpassword", Toast.LENGTH_SHORT).show();
        }else if(password.equals(confirmpassword)){

        // Create a new user with a first and last name
        Map<String, Object> item = new HashMap<>();
        item.put("username", username);
        item.put("password", password);


// Add a new document with a generated ID
            db.collection("user")
                    .add(item)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(RegisterActivity.this, "Insert" + documentReference.getId(), Toast.LENGTH_SHORT).show();
                                readdata();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{
            Toast.makeText(this, "Mật khẩu không trùng", Toast.LENGTH_SHORT).show();
        }

    }
}