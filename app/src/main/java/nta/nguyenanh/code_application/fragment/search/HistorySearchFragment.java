package nta.nguyenanh.code_application.fragment.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.SearchActivity;
import nta.nguyenanh.code_application.adapter.Adapter_History;
import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.dao.DAO_History;
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
import nta.nguyenanh.code_application.interfaces.OnClickItemSearchHistory;
import nta.nguyenanh.code_application.model.History;
import nta.nguyenanh.code_application.model.Product;


public class HistorySearchFragment extends Fragment {

    Button btn_search;
    EditText edt_searchView;
    RecyclerView recyclerViewHistory, recyclerViewResult;
    DAO_History dao_history;
    Adapter_History adapterHistory;
    ProductAdapter productAdapter;

    List<History> ds = new ArrayList<>();
    List<Product> listResult = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView deleteAll;

    DiaLogProgess progess;

    public HistorySearchFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao_history = new DAO_History(getActivity());
        recyclerViewHistory = view.findViewById(R.id.rv_History_Search);
        recyclerViewResult = view.findViewById(R.id.rv_Result_Search);
        deleteAll = view.findViewById(R.id.deleteAll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHistory.setLayoutManager(linearLayoutManager);
        btn_search = view.findViewById(R.id.btn_search);
        edt_searchView = view.findViewById(R.id.edt_searchView);
        dao_history = new DAO_History(getActivity());
        ds = dao_history.getAll();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_searchView.getText().toString().isEmpty()) {
                    if (!edt_searchView.getText().toString().isEmpty()) {
                        String s = edt_searchView.getText().toString();
                        ds = dao_history.getAll();
                        boolean check = false;
                        for (int i = 0; i < ds.size(); i++) {
                            if (edt_searchView.getText().toString().equals(ds.get(i).getName_history())) {
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            dao_history.insert(edt_searchView.getText().toString());
                            ds = dao_history.getAll();
                            fillData();
                            check = false;
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.from_right,R.animator.to_left, R.animator.from_left, R.animator.to_right).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").commit();
                        } else {
                            check = false;
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.from_right,R.animator.to_left, R.animator.from_left, R.animator.to_right).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").commit();

                        }
                    }
                }
            }
        });
        edt_searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if(!edt_searchView.getText().toString().isEmpty()) {
                        String s = edt_searchView.getText().toString();
                        boolean check = false;
                        ds = dao_history.getAll();
                        for (int i = 0; i < ds.size(); i++) {
                            if (edt_searchView.getText().toString().equals(ds.get(i).getName_history())){
                                Log.d(">>>>TAG:",edt_searchView.getText().toString()+"><"+ds.get(i).getName_history());
                                check = true;
                                break;
                            }
                        }
                        if(!check) {
                            dao_history.insert(edt_searchView.getText().toString());
                            ds = dao_history.getAll();
                            check = false;
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.from_right,R.animator.to_left, R.animator.from_left, R.animator.to_right).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
                        }
                        else {
                            check = false;
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.from_right,R.animator.to_left, R.animator.from_left, R.animator.to_right).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
                        }
                    }
                    handled = true;
                }
                return handled;
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao_history.deleteall();
                fillData();
            }
        });
        fillData();
    }
    public void fillData() {
        ds = dao_history.getAll();
        Collections.reverse(ds);
        adapterHistory = new Adapter_History(getActivity(), ds, new OnClickItemSearchHistory() {
            @Override
            public void OnClickItemSearchHistory(String s) {
                edt_searchView.setText(s);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.from_right,R.animator.to_left, R.animator.from_left, R.animator.to_right).replace(R.id.fragment_container_search, SearchFragment.newInstance(s), "SearchFragment").addToBackStack("").commit();
            }
        });
        recyclerViewHistory.setAdapter(adapterHistory);
    }

}