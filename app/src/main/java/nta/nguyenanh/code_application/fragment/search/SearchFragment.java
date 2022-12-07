package nta.nguyenanh.code_application.fragment.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import nta.nguyenanh.code_application.adapter.ProductAdapter;
import nta.nguyenanh.code_application.dao.DAO_History;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.History;
import nta.nguyenanh.code_application.model.Product;

public class SearchFragment extends Fragment {

    private static final String ARG_SEARCH = "param1";
    private String searchString;
    private Button btn_search;
    private EditText edt_searchView;
    private RecyclerView recyclerViewResult;
    private DAO_History dao_history;
    private ProductAdapter productAdapter;
    private List<Product> listResult = new ArrayList<>();
    float a = 1;
    List<History> ds = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


//    private DiaLogProgess progess;
    private LinearLayout ln_noResult;
    private CoordinatorLayout coordinatorContainer;
    private CoordinatorLayout coordinator_progress;
    private ProgressBar progressBar_search;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchString = getArguments().getString(ARG_SEARCH);
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
        findData(searchString);
        recyclerViewResult = view.findViewById( R.id.rv_Result_Search);
        ln_noResult = view.findViewById(R.id.tv_noResult);
        coordinatorContainer = view.findViewById(R.id.coordinatorContainer);
        coordinator_progress = view.findViewById(R.id.coordinator_progress);
        progressBar_search = view.findViewById(R.id.progressBar_search);
        dao_history = new DAO_History(getActivity());
        edt_searchView.setText(searchString);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt_searchView.getText().toString().isEmpty()) {
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
                            check = false;
                            findData(edt_searchView.getText().toString());
                           } else {
                            check = false;
                            findData(edt_searchView.getText().toString());
                        }
                    }
                }
            }
        });

    }

    public void findData(String s) {
        listResult.clear();
        db.collection("product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document:task.getResult()){
                            String nameproduct = document.getString("nameproduct");
                            if (nameproduct.toLowerCase().contains(s.toLowerCase())){
                                Log.d("TAG 1000", "onComplete: "+document.getString("nameproduct"));
                                ArrayList<String> color = (ArrayList<String>) document.getData().get("color");
                                ArrayList<String> images = (ArrayList<String>) document.getData().get("image");
                                Product product = new Product(document.getId(),
                                        document.getData().get("nameproduct").toString(),
                                        document.getData().get("describe").toString(),
                                        Float.parseFloat(document.getData().get("price").toString()),
                                        Integer.parseInt(document.getData().get("available").toString()),
                                        color, images,
                                        Integer.parseInt(document.getData().get("sale").toString()),
                                        Integer.parseInt(document.getData().get("sold").toString()),
                                        Integer.parseInt(document.getData().get("total").toString()),
                                        document.getData().get("id_category").toString());
                                listResult.add(product);
                            }
                        }
                        if (listResult.size()== 0 || listResult == null){
                            recyclerViewResult.setVisibility(View.GONE);
                            coordinatorContainer.setVisibility(View.VISIBLE);
                            Log.d(">>>>TAG:", listResult.size()+"");
                            ln_noResult.setVisibility(View.VISIBLE);
                            coordinator_progress.setVisibility(View.GONE);
                        }else {
                            recyclerViewResult.setVisibility(View.GONE);
                            coordinatorContainer.setVisibility(View.VISIBLE);
                            coordinator_progress.setVisibility(View.VISIBLE);
                            ln_noResult.setVisibility(View.GONE);
                            progressBar_search.setVisibility(View.VISIBLE);
                            Log.d(">>>>TAG:", listResult.size()+"");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (a < listResult.size()+1){
                                        double b=0;
                                        b = (a/(listResult.size()+1))*(100);
                                        progressBar_search.setProgress((int) b);
                                        Log.d(">>>>TAG:", a +"");
                                        Log.d(">>>>TAG:", b +"");
                                        a++;
                                        handler.postDelayed(this, 1);
                                    }
                                    else{
                                        handler.removeCallbacks(this);
                                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                                        productAdapter = new ProductAdapter(listResult, getContext());
                                        recyclerViewResult.setLayoutManager(staggeredGridLayoutManager);
                                        recyclerViewResult.setAdapter(productAdapter);
                                        coordinatorContainer.setVisibility(View.GONE);
                                        coordinator_progress.setVisibility(View.GONE);
                                        ln_noResult.setVisibility(View.GONE);
                                        progressBar_search.setVisibility(View.GONE);
                                        recyclerViewResult.setVisibility(View.VISIBLE);
                                        a = 1;
                                    }
                                }
                            },1);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(">>>>TAG:","");
                    }
                });
    }

}