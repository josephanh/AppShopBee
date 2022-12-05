package nta.nguyenanh.code_application.fragment.address;

import static nta.nguyenanh.code_application.PayActivity.indexDivision;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nta.nguyenanh.code_application.PayActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.AddressAdapter;
import nta.nguyenanh.code_application.model.Address;

public class DivisionFragment extends Fragment {

    ArrayList<Address> list = new ArrayList<>();
    RecyclerView recyclerAddress;
    AddressAdapter adapter;
    LinearLayoutManager layoutManager;

    public DivisionFragment() {
        // Required empty public constructor
    }


    public static DivisionFragment newInstance(ArrayList<Address> list) {
        DivisionFragment fragment = new DivisionFragment();
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = (ArrayList<Address>) getArguments().get("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address_d_w, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerAddress = view.findViewById(R.id.recyclerViewAddress);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerAddress.setLayoutManager(layoutManager);
        adapter = new AddressAdapter(getContext(), list, new AddressAdapter.onClickItemAddress() {
            @Override
            public void onClick(int position) {
                new AddressFragment.OnClick().onClickItemAddress(position, list, getContext());
                Log.d("DEMO>>>>", "onClick: ");
            }
        });
        recyclerAddress.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerAddress.getContext(), layoutManager.getOrientation());
        recyclerAddress.addItemDecoration(itemDecoration);

    }

    @Override
    public void onResume() {
        if(adapter != null) {
            if(indexDivision == -1) {
                adapter.notifyDataSetChanged();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}