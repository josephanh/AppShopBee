package nta.nguyenanh.code_application;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    Button btn_search;
    EditText edt_searchView;
    RecyclerView recyclerView;
    DAO_History dao_history;
    Adapter_History adapter;
    List<History_model> ds = new ArrayList<>();
    History_model hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btn_search = findViewById(R.id.btn_search);
        edt_searchView = findViewById(R.id.edt_searchView);
        recyclerView = findViewById(R.id.rv_History_Search);

        dao_history = new DAO_History(Search.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<History_model> ds  = dao_history.getAll();
        adapter = new Adapter_History(Search.this,ds);
        recyclerView.setAdapter(adapter);
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
    public void delete_history() {
    ds  = dao_history.getAll();
    }

}