package nta.nguyenanh.code_application.fragment.pay;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.content.Intent;
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

import java.util.ArrayList;

import nta.nguyenanh.code_application.PayActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.Adapter_AllAddress;
import nta.nguyenanh.code_application.fragment.address.AddressFragment;
import nta.nguyenanh.code_application.interfaces.OnClickUpdateAddress;
import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.ProductCart;


public class AllAddressFragment extends Fragment {

    RecyclerView rv_getAllAddress;
    Adapter_AllAddress adapter_allAddress;
    private ArrayList<Address> list = new ArrayList<>();
    ArrayList<ProductCart> listCart;
    public AllAddressFragment() {
        // Required empty public constructor
    }

    public static AllAddressFragment newInstance(ArrayList<ProductCart> listCart) {
        Bundle args = new Bundle();
        args.putSerializable("list", listCart);
        AllAddressFragment fragment = new AllAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            listCart = (ArrayList<ProductCart>) getArguments().getSerializable("list");
        }
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
        Button themdiachi = view.findViewById(R.id.themdiachi);

        themdiachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PayActivity)getContext()).changeFragment(new AddressFragment().newInstance(listCart, -1));
            }
        });

        list = userModel.getAddress();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_getAllAddress.setLayoutManager(linearLayoutManager);
        adapter_allAddress = new Adapter_AllAddress(getContext(), list, new OnClickUpdateAddress() {
            @Override
            public Void OnClickUpdate(int position) {
//                Intent intent = new Intent(getContext(), PayActivity.class);
//                intent.putExtra("addressCheck", false);
//                intent.putExtra("position", position);
//                startActivity(intent);
                ((PayActivity)getContext()).changeFragment(new AddressFragment().newInstance(listCart, position));
                return null;
            }

            @Override
            public Void OnClickCheckBox(int position) {
                ((PayActivity)getContext()).changeFragment(new PayFragment().newInstance(listCart));
                return null;
            }
        });



        rv_getAllAddress.setAdapter(adapter_allAddress);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter_allAddress.notifyDataSetChanged();
    }
}