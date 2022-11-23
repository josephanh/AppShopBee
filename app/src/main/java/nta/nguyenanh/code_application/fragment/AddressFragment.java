package nta.nguyenanh.code_application.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nta.nguyenanh.code_application.AddressActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.AddressAdapter;
import nta.nguyenanh.code_application.model.Address;

public class AddressFragment extends Fragment {


    ArrayList<Address> list = new ArrayList<>();

    public AddressFragment() {
        // Required empty public constructor
    }


    public static AddressFragment newInstance(ArrayList<Address> list) {
        AddressFragment fragment = new AddressFragment();
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
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listviewAddress = view.findViewById(R.id.listviewAddress);
        AddressAdapter adapter = new AddressAdapter(getContext(), list);
        listviewAddress.setAdapter(adapter);
        listviewAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((AddressActivity)getActivity()).onClickItemAddress(list.get(position).getCode());
            }
        });
    }
}