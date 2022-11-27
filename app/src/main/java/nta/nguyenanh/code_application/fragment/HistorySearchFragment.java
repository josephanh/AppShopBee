package nta.nguyenanh.code_application.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        // Required empty public constructor
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

        progess = new DiaLogProgess(getActivity());
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
        adapterHistory = new Adapter_History(getActivity(), ds);
        recyclerViewHistory.setAdapter(adapterHistory);
    }
}