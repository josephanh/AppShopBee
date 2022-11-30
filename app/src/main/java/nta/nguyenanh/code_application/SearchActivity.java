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

public class SearchActivity extends AppCompatActivity{

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

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.alpha_enter, R.anim.alpha_exit).replace(R.id.fragment_container_search, HistorySearchFragment.class, null).commit();
//        btn_search = findViewById(R.id.btn_search);
//        edt_searchView = findViewById(R.id.edt_searchView);

//        dao_history = new DAO_History(SearchActivity.this);
//        ds = dao_history.getAll();

//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!edt_searchView.getText().toString().isEmpty()) {
//                    if (!edt_searchView.getText().toString().isEmpty()) {
//                        String s = edt_searchView.getText().toString();
//                        ds = dao_history.getAll();
//                        boolean check = false;
//                        for (int i = 0; i < ds.size(); i++) {
//                            if (edt_searchView.getText().toString().equals(ds.get(i).getName_history())) {
//                                Log.d(">>>>TAG:", edt_searchView.getText().toString() + "==" + ds.get(i).getName_history());
//                                check = true;
//                                break;
//                            }
//                        }
//                        if (!check) {
//                            dao_history.insert(edt_searchView.getText().toString());
//                            ds = dao_history.getAll();
//                            check = false;
//                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_rigth, R.anim.default_anim).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
//                        } else {
//                            check = false;
//                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_rigth, R.anim.default_anim).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
//
//                        }
//                    }
//                }
//            }
//        });
//        edt_searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    if(!edt_searchView.getText().toString().isEmpty()) {
//                        String s = edt_searchView.getText().toString();
//                        boolean check = false;
//                        ds = dao_history.getAll();
//                        for (int i = 0; i < ds.size(); i++) {
//                            if (edt_searchView.getText().toString().equals(ds.get(i).getName_history())){
//                                Log.d(">>>>TAG:",edt_searchView.getText().toString()+"><"+ds.get(i).getName_history());
//                                check = true;
//                                break;
//                            }
//                        }
//                        if(!check) {
//                            dao_history.insert(edt_searchView.getText().toString());
//                            ds = dao_history.getAll();
//                            check = false;
//                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_rigth, R.anim.default_anim).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
//                        }
//                        else {
//                            check = false;
//                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_rigth, R.anim.default_anim).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
//
//                        }
//                    }
//                    handled = true;
//                }
//                return handled;
//            }
//        });
    }

//    @Override
//    public void OnClickItemSearchHistory(String s) {
//        edt_searchView.setText(s);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
//
//    }
//
//    @Override
//    public void OnClickText() {
//        onBackPressed();
//    }

}
