package nta.nguyenanh.code_application;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.dao.DAO_History;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.fragment.search.HistorySearchFragment;
import nta.nguyenanh.code_application.fragment.search.SearchFragment;
import nta.nguyenanh.code_application.interfaces.OnClickItemSearchHistory;
import nta.nguyenanh.code_application.model.History;
import nta.nguyenanh.code_application.model.Product;

public class SearchActivity extends AppCompatActivity implements OnClickItemSearchHistory {

    Button btn_search;
    EditText edt_searchView;
    RecyclerView recyclerViewHistory, recyclerViewResult;
    DAO_History dao_history;
//    Adapter_History adapterHistory;
    ProductAdapter productAdapter;
    List<History> ds = new ArrayList<>();
    List<Product> listResult = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView deleteAll;

    DiaLogProgess progess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, HistorySearchFragment.class, null).commit();
        btn_search = findViewById(R.id.btn_search);
        edt_searchView = findViewById(R.id.edt_searchView);
//        recyclerViewHistory = findViewById(R.id.rv_History_Search);
//        recyclerViewResult = findViewById(R.id.rv_Result_Search);
//        deleteAll = findViewById(R.id.deleteAll);

        dao_history = new DAO_History(SearchActivity.this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerViewHistory.setLayoutManager(linearLayoutManager);

        progess = new DiaLogProgess(SearchActivity.this);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_searchView.getText().toString().isEmpty()) {
                    progess.showDialog("Searching");
                    String s = edt_searchView.getText().toString();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
                    dao_history.insert(edt_searchView.getText().toString());
                    progess.hideDialog();
                }
            }
        });
        edt_searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if(!edt_searchView.getText().toString().isEmpty()) {
                        progess.showDialog("Searching");
                        String s = edt_searchView.getText().toString();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
                        dao_history.insert(edt_searchView.getText().toString());
                        progess.hideDialog();
                    }
                    handled = true;
                }
                return handled;
            }
        });
//        deleteAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dao_history.deleteall();
//                fillData();
//            }
//        });
//
//        fillData();
    }

    @Override
    public void OnClickItemSearchHistory(String s) {
        edt_searchView.setText(s);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();

    }

    @Override
    public void OnClickText() {
        onBackPressed();
    }
//
//    public  void fillData() {
//        ds = dao_history.getAll();
//        Collections.reverse(ds);
//        adapterHistory = new Adapter_History(SearchActivity.this,ds);
//        recyclerViewHistory.setAdapter(adapterHistory);
//    }

//    public void findData(String s) {
//        listResult.clear();
//        db.collection("product")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (DocumentSnapshot document:task.getResult()){
//                            String nameproduct = document.getString("nameproduct");
//                            if (nameproduct.toLowerCase().contains(s.toLowerCase())){
//                                Log.d("TAG 1000", "onComplete: "+document.getString("nameproduct"));
//                                ArrayList<String> color = (ArrayList<String>) document.getData().get("color");
//                                ArrayList<String> images = (ArrayList<String>) document.getData().get("image");
//
//                                Product product = new Product(document.getId(),
//                                        document.getData().get("nameproduct").toString(),
//                                        document.getData().get("describe").toString(),
//                                        Float.parseFloat(document.getData().get("price").toString()),
//                                        Integer.parseInt(document.getData().get("available").toString()),
//                                        color, images,
//                                        Integer.parseInt(document.getData().get("sale").toString()),
//                                        Integer.parseInt(document.getData().get("sold").toString()),
//                                        Integer.parseInt(document.getData().get("total").toString()),
//                                        document.getData().get("id_category").toString());
//                                listResult.add(product);
//                            }
//                        }
//                        progess.hideDialog();
//                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                        productAdapter = new ProductAdapter(listResult, SearchActivity.this);
//                        recyclerViewResult.setLayoutManager(staggeredGridLayoutManager);
//                        recyclerViewResult.setAdapter(productAdapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progess.hideDialog();
//                        Toast.makeText(SearchActivity.this, "Không tìm thấy", Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
}
