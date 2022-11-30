package nta.nguyenanh.code_application.fragment.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import nta.nguyenanh.code_application.dialog.DiaLogProgess;
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

    FirebaseFirestore db = FirebaseFirestore.getInstance();


//    private DiaLogProgess progess;
    private TextView tv_noResult;

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
        recyclerViewResult = view.findViewById( R.id.rv_Result_Search);
        tv_noResult = view.findViewById(R.id.tv_noResult);

//        progess = new DiaLogProgess(getContext());
//        progess.showDialog("Searching");
        findData(searchString);
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
//                        progess.hideDialog();
                        if (listResult.size()== 0 || listResult == null){



                            recyclerViewResult.setVisibility(View.GONE);
                            tv_noResult.setVisibility(View.VISIBLE);
                        }else {
                            recyclerViewResult.setVisibility(View.VISIBLE);
                            tv_noResult.setVisibility(View.GONE);
                            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            productAdapter = new ProductAdapter(listResult, getContext());
                            recyclerViewResult.setLayoutManager(staggeredGridLayoutManager);
                            recyclerViewResult.setAdapter(productAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        progess.hideDialog();
                        Log.d(">>>>TAG:","");
                    }
                });
    }

}