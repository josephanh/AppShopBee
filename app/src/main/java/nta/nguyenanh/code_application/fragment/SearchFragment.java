package nta.nguyenanh.code_application.fragment;

import static nta.nguyenanh.code_application.MainActivity.bottomnavigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nta.nguyenanh.code_application.adapter.Adapter_History;
import nta.nguyenanh.code_application.dao.DAO_History;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.History_model;

public class SearchFragment extends Fragment {


    Button btn_search;
    EditText edt_searchView;
    RecyclerView recyclerView;
    DAO_History dao_history;
    Adapter_History adapter;
    List<History_model> ds = new ArrayList<>();

    TextView deleteAll;

    ArrayList<String> listSuggest = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_search = view.findViewById(R.id.btn_search);
        edt_searchView = view.findViewById(R.id.edt_searchView);
        recyclerView = view.findViewById(R.id.rv_History_Search);
        deleteAll = view.findViewById(R.id.deleteAll);

        dao_history = new DAO_History(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!edt_searchView.getText().toString().isEmpty()) {
                        dao_history.insert(edt_searchView.getText().toString());
                        Log.d("HISTORY", "onClick: ");
                        fillData();
                    }

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
        AddlistSuggest();



    }

    public  void fillData() {
        ds = dao_history.getAll();
        adapter = new Adapter_History(getContext(),ds);
        recyclerView.setAdapter(adapter);
    }

    public void AddlistSuggest() {
        listSuggest.add("Iphone 14 promax");
        listSuggest.add("Tivi màn hình Oled");
        listSuggest.add("Sam sung ngon");
        listSuggest.add("Laptop Gaming");
        listSuggest.add("Tai nghe chiến game");
        listSuggest.add("Pc siêu cấp");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                do {
                    try {
                        edt_searchView.setHint(listSuggest.get(i));
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                } while (i < listSuggest.size()-1);
            }
        });
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bottomnavigation.setVisibility(View.VISIBLE);
    }
}