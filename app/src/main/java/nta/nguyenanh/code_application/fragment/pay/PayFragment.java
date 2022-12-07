package nta.nguyenanh.code_application.fragment.pay;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.PayActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.CartAdapter;
import nta.nguyenanh.code_application.interfaces.OnclickItemCart;
import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.ProductCart;

public class PayFragment extends Fragment {

    ArrayList<ProductCart> list;

    private RecyclerView recyclerViewItemPay;
    private TextView tvUsername, tvNumberPhone, tvAddress, dateReceive;
    private CartAdapter cartAdapter;
    private Double totalMoney = Double.valueOf(0);
    private float price;
    private TextView tv_totalMoney2;
    private TextView tv_totalMoney1;
    private TextView tv_totalMoneyFinal;
    private TextView tv_totalMoney1final;
    private Button btn_buy_d;
    private DecimalFormat formatter;
    private LinearLayout ln_AllAddress;
    FragmentManager manager;

    public static PayFragment newInstance(ArrayList<ProductCart> list) {
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        PayFragment fragment = new PayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            list = (ArrayList<ProductCart>) getArguments().getSerializable("list");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unitUI(view);

        ln_AllAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PayActivity)getContext()).changeFragment(new AllAddressFragment().newInstance(list));
            }
        });

        long unix = System.currentTimeMillis();
        Date date = new Date(unix+432000000L);
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd/MM");
        formatter = new DecimalFormat("###,###,###");
        String formattedDate = sdf.format(date);

        long unixRe = unix+691200000L;
        Date dateRe = new Date(unixRe);
        String formattedDateRe = sdf.format(dateRe);

        Log.d("TAGDATE", "onCreate: "+unix);
        Log.d("TAGDATE", "onCreate: "+unixRe);

        tvUsername.setText(MainActivity.userModel.getAddress().get(0).getNameReceiver());
        tvNumberPhone.setText(MainActivity.userModel.getAddress().get(0).getPhonenumber());
        tvAddress.setText(MainActivity.userModel.getAddress().get(0).getAddress());
        dateReceive.setText("Nhận hàng vào "+formattedDate+" đến "+formattedDateRe);

        price = 0;

        for (int i = 0; i < list.size(); i++) {
            price = list.get(i).getPrice();
            totalMoney += Double.parseDouble(String.valueOf(price))*list.get(i).getTotal();
            Log.d(">>>>TAG:",""+price);

            tv_totalMoney1final.setText(formatter.format(totalMoney)+"đ");
            tv_totalMoney2.setText(formatter.format(totalMoney)+"đ");
            tv_totalMoney1.setText(formatter.format(totalMoney+30000)+"đ");
            tv_totalMoneyFinal.setText(formatter.format(totalMoney+30000)+"đ");
        }

        Log.d("LIST DATA", "onCreate: pay"+list.get(0).getNameproduct());

        cartAdapter = new CartAdapter(list, getContext(), new OnclickItemCart() {
            @Override
            public void onClickMinus(int totalNew, int totalOld, float price) {
                changeTotal(totalNew, totalOld, price);
            }

            @Override
            public void onClickPlus(int totalNew, int totalOld, float price) {
                changeTotal(totalNew, totalOld, price);
            }

            @Override
            public void onClickCheck(boolean isCheck, int total, float price) {
                Log.d(">>>>TAG:", "Hello3");
            }

            @Override
            public void hideCheck(CheckBox checkBox, ImageView imageView) {
                checkBox.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onClickDelete(String id, int position) {
                // ham nay de trong // ko viet
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewItemPay.setLayoutManager(layoutManager);
        recyclerViewItemPay.setAdapter(cartAdapter);

        btn_buy_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PayActivity)getContext()).buyEvent(String.valueOf(tv_totalMoneyFinal.getText().toString()));
            }
        });

    }


    private void unitUI(View view){
        tv_totalMoney2 = view.findViewById(R.id.tv_totalMoney2);
        tv_totalMoney1 = view.findViewById(R.id.tv_totalMoney1);
        tv_totalMoneyFinal = view.findViewById(R.id.tv_totalMoneyFinal);
        tv_totalMoney1final = view.findViewById(R.id.tv_totalMoney1final);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvNumberPhone = view.findViewById(R.id.tvNumberPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        dateReceive = view.findViewById(R.id.dateReceive);
        btn_buy_d = view.findViewById(R.id.btn_buy_d);
        ln_AllAddress = view.findViewById(R.id.ln_AllAddress);
        recyclerViewItemPay = view.findViewById(R.id.recyclerViewPay);
    }

    public void changeTotal(int total, int oldTotal, float price){

        if(total < oldTotal) {
            totalMoney = totalMoney - (oldTotal - total)*price;
        } else {
            totalMoney = totalMoney + (total - oldTotal)*price;
        }
//        totalMoneyProduct.setText(formatter.format(totalMoney)+"đ");
        Log.d(">>>>TAG:",""+formatter.format(totalMoney)+"đ");
        tv_totalMoney1final.setText(formatter.format(totalMoney)+"đ");
        tv_totalMoney2.setText(formatter.format(totalMoney)+"đ");
        tv_totalMoney1.setText(formatter.format(totalMoney+30000)+"đ");
        tv_totalMoneyFinal.setText(formatter.format(totalMoney+30000)+"đ");
    }

    @Override
    public void onResume() {
        super.onResume();
        cartAdapter.notifyDataSetChanged();

        SharedPreferences preferences = getActivity().getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("address", null);
        Type type = new TypeToken<ArrayList<Address>>(){}.getType();
        ArrayList<Address> addressList = gson.fromJson(json, type);

        if(addressList == null) {
            addressList  = new ArrayList<>();
        }

        MainActivity.userModel.setAddress(addressList);
    }
}
