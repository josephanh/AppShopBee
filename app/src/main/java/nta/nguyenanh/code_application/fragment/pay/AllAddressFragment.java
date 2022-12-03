package nta.nguyenanh.code_application.fragment.pay;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.Adapter_AllAddress;
import nta.nguyenanh.code_application.interfaces.OnClickUpdateAddress;
import nta.nguyenanh.code_application.model.Address;


public class AllAddressFragment extends Fragment {

    RecyclerView rv_getAllAddress;
    Adapter_AllAddress adapter_allAddress;
    private ArrayList<Address> list = new ArrayList<>();
    public AllAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_all_address, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_getAllAddress = view.findViewById(R.id.rv_getAllAddress);

        list = MainActivity.userModel.getAddress();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_getAllAddress.setLayoutManager(linearLayoutManager);
        adapter_allAddress = new Adapter_AllAddress(getContext(), list, new OnClickUpdateAddress() {
            @Override
            public Void OnClickUpdate(Address address) {
                return null;
            }
        });
        rv_getAllAddress.setAdapter(adapter_allAddress);
    }
}