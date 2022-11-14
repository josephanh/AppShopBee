package nta.nguyenanh.code_application;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class Search extends AppCompatActivity {
    Button btn_search;
    EditText edt_searchView;
    RecyclerView recyclerView;
    DAO_History dao_history;
    Adapter_History adapter;
    List<History_model> ds = new ArrayList<>();
    List<Product> ds2 = new ArrayList<>();
    History_model hm;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btn_search = findViewById(R.id.btn_search);
        edt_searchView = findViewById(R.id.edt_searchView);
        recyclerView = findViewById(R.id.rv_History_Search);
        tv = findViewById(R.id.tv);

        dao_history = new DAO_History(Search.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<History_model> ds  = dao_history.getAll();
        adapter = new Adapter_History(Search.this,ds);
        recyclerView.setAdapter(adapter);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findData(edt_searchView.getText().toString());
            }
        });
    }
    public void add_history(View v){
        String edt_search  = edt_searchView.getText().toString();
        dao_history.insert(edt_search);
        filldulieu();
    }
    public void filldulieu(){
        ds = dao_history.getAll();
        adapter = new Adapter_History(Search.this,ds);
        recyclerView.setAdapter(adapter);

    }
    public void delete_history(View v) {
    dao_history.deleteall();
    filldulieu();
    }
    public void findData(String s) {
        db.collection("product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult()){
                            String nameproduct = doc.getString("nameproduct");
                            if (nameproduct.contains(s)){
                                Log.d("TAG 1000", "onComplete: "+doc.getString("nameproduct"));
                                tv.append(""+doc.getString("nameproduct"));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Search.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}