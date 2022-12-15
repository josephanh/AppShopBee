package nta.nguyenanh.code_application.fragment.address;

import static android.content.Context.MODE_PRIVATE;
import static nta.nguyenanh.code_application.MainActivity.userModel;
import static nta.nguyenanh.code_application.PayActivity.indexDistrict;
import static nta.nguyenanh.code_application.PayActivity.indexDivision;
import static nta.nguyenanh.code_application.PayActivity.indexWard;
import static nta.nguyenanh.code_application.PayActivity.listDistrict;
import static nta.nguyenanh.code_application.PayActivity.listDistricts;
import static nta.nguyenanh.code_application.PayActivity.listDivision;
import static nta.nguyenanh.code_application.PayActivity.listWards;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.PayActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.adapter.ViewPagerAdapter;
import nta.nguyenanh.code_application.fragment.pay.PayFragment;
import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.ProductCart;

public class AddressFragment extends Fragment {

    ArrayList<ProductCart> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static BottomSheetDialog bottomSheetDialog;
    public static TabLayout tableLayoutAddress;
    public static ViewPager2 viewPagerAddress;
    public static ViewPagerAdapter viewPagerAdapterAddress;
    public static TextView tinhhuyenxa;

    EditText edt_namereceiver, edt_numberphone, edt_streets, edt_house;
    Button btn_confirm;

    Toolbar toolbar;
    int pos;

    public static String divisionAddress, districtAddress, ward;


    public static AddressFragment newInstance(ArrayList<ProductCart> list, int position) {
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        args.putInt("position", position);
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            list = (ArrayList<ProductCart>) getArguments().getSerializable("list");
            pos = getArguments().getInt("position");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unitUI(view);

        if(pos != -1) {
            Log.d("position", "onViewCreated: "+pos);
            edt_namereceiver.setText(userModel.getAddress().get(pos).getNameReceiver());
            edt_numberphone.setText(userModel.getAddress().get(pos).getPhonenumber());
            tinhhuyenxa.setText(userModel.getAddress().get(pos).getAddress());
        }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress(new Update());
            }
        });

        tinhhuyenxa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheet();
            }
        });

        new fetchData().start();

    }

    private void unitUI(View view){
        toolbar = view.findViewById(R.id.toolbar);
        tinhhuyenxa = view.findViewById(R.id.tinhhuyenxa);
        edt_namereceiver = view.findViewById(R.id.edt_namereceiver);
        edt_numberphone = view.findViewById(R.id.edt_numberphone);
        edt_streets = view.findViewById(R.id.edt_streets);
        edt_house = view.findViewById(R.id.edt_house);
        btn_confirm = view.findViewById(R.id.btn_confirm);
    }

    public void addAddress(Update update) {

        String phonenumber = edt_numberphone.getText().toString().trim();
        String namereceiver = edt_namereceiver.getText().toString().trim();
        String place = tinhhuyenxa.getText().toString().trim();
        int availble;

        if(phonenumber.isEmpty() || namereceiver.isEmpty() || place.isEmpty()) {
            Toast.makeText(getContext(), "Bạn hãy điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        // Thay thế chuỗi cho gọn
        place = tinhhuyenxa.getText().toString() ;
        Address address;
        if(pos != -1) {
            address = new Address(userModel.getAddress().get(pos).getIdAddress(),place, namereceiver, phonenumber, 0);
            userModel.getAddress().set(pos, address);
            HashMap<String, Object> places = new HashMap<>();
            places.put("address"+userModel.getAddress().get(pos).getIdAddress(), address);
            update.updateAddress(places);
        } else {
            address = new Address("address"+System.currentTimeMillis(),place, namereceiver, phonenumber, 0);
            userModel.getAddress().add(address);
            HashMap<String, Object> places = new HashMap<>();
            for (int i = 0; i < userModel.getAddress().size(); i++){
                places.put(userModel.getAddress().get(i).getIdAddress(), userModel.getAddress().get(i));
            }
            update.updateAddress(places);
        }





        // thêm ngay tại list -- không cần load lại dữ liệu

        Gson gson = new Gson();
        String json = gson.toJson(userModel.getAddress());

        // cập nhật trong máy thông tin địa chỉ
        SharedPreferences preferences = getActivity().getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address", json);
        editor.commit();
    }

    class fetchData extends Thread {
        String data = "";

        @Override
        public void run() {
            super.run();
            try {
                URL url = new URL("https://provinces.open-api.vn/api/?depth=2");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    data = data + line;
//                    Log.d("DATA DEMO: NAME", "run: " + data);
                    if (!data.isEmpty()) {
//                         code lấy tỉnh và huyện
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // code lấy hiện
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONArray quanhuyen = jsonObject.getJSONArray("districts");
                            String name = jsonObject.getString("name");
                            int code = jsonObject.getInt("code");
                            Address addressDiv = new Address(name, code, -1);
                            listDivision.add(addressDiv);

                            for (int j = 0; j < quanhuyen.length(); j++) {
                                JSONObject quan = quanhuyen.getJSONObject(j);
                                String nameDis = quan.getString("name");
                                int province_code = quan.getInt("province_code");
                                int codeDis = quan.getInt("code");
                                Address address = new Address(nameDis, codeDis, province_code);
                                listDistricts.add(address);
                            }
//                            String json = String.valueOf(jsonObject.getJSONObject("name"));
                        }
                        Log.d("DATA DEMO: ", "run: " + listDivision.get(40).getName());
                        Log.d("DATA DEMO: ", "run: " + listDistricts.get(100).getName());
                    }

                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Update{

        public void updateAddress(HashMap<String, Object> address) {
            db.collection("user").document(userModel.getUserID())
                    .update("address", address)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Thành Công", Toast.LENGTH_SHORT).show();
                            ((PayActivity)getContext()).changeFragment(new PayFragment().newInstance(list));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    public void bottomsheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.layout_address_bottom_sheet, null);
        tableLayoutAddress = view.findViewById(R.id.tablayout);
        viewPagerAddress = view.findViewById(R.id.viewpager2);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        viewPagerAdapterAddress = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPagerAddress.setAdapter(viewPagerAdapterAddress);

        tableLayoutAddress.setSelectedTabIndicator(R.drawable.round_back_theme_diachi);
        if(indexDivision != -1 && indexDistrict != -1 && indexWard != -1){
            tableLayoutAddress.addTab(tableLayoutAddress.newTab().setText(listDivision.get(indexDivision).getName()));
            tableLayoutAddress.addTab(tableLayoutAddress.newTab().setText(listDistrict.get(indexDistrict).getName()));
            tableLayoutAddress.addTab(tableLayoutAddress.newTab().setText(listWards.get(indexWard).getName()));
        } else {
            tableLayoutAddress.addTab(tableLayoutAddress.newTab().setText("Tỉnh / Thành phố"));
        }

//        tableLayout.addTab(tableLayout.newTab().setText("Quận / Huyện"));
//        tableLayout.addTab(tableLayout.newTab().setText("Phường / Xã"));

        tableLayoutAddress.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerAddress.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerAddress.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tableLayoutAddress.selectTab(tableLayoutAddress.getTabAt(position));
            }
        });

        if (viewPagerAddress.getCurrentItem() == 0) {
            tableLayoutAddress.setTabRippleColor(ColorStateList.valueOf(Color.parseColor("#3D3D3D")));
//            tableLayout.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
//            tableLayout.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
//            tableLayout.getTabAt(1).removeBadge();
//            tableLayout.getTabAt(2).removeBadge();
        }


        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
    static class OnClick{
        public void onClickItemAddress(int i, ArrayList<Address> list, Context context) {

            if (list.get(i).getProvince_code() == -1) {
                divisionAddress = listDivision.get(i).getName();
                indexDivision = i;
                indexDistrict = -1;
                indexWard = -1;
                if (tableLayoutAddress.getTabCount() == 2) {
                    tableLayoutAddress.removeTab(tableLayoutAddress.getTabAt(1));
                }
                if (tableLayoutAddress.getTabCount() == 3) {
                    tableLayoutAddress.removeTab(tableLayoutAddress.getTabAt(1));
                    tableLayoutAddress.removeTab(tableLayoutAddress.getTabAt(1));
                }
                tableLayoutAddress.addTab(tableLayoutAddress.newTab().setText("Quận/Huyện"));
                tableLayoutAddress.getTabAt(0).setText(listDivision.get(i).getName());
                listDistrict.clear();
                for (int j = 0; j < listDistricts.size(); j++) {
                    if (listDistricts.get(j).getProvince_code() == listDivision.get(i).getCode()) {
                        listDistrict.add(listDistricts.get(j));
                    }
                }
                viewPagerAdapterAddress.notifyDataSetChanged();
                Log.d("DATA", "onClickItemAddress: " + listDistrict.size());
                tableLayoutAddress.setScrollPosition(1, 0f, true);
                tableLayoutAddress.selectTab(tableLayoutAddress.getTabAt(1));
                viewPagerAddress.setCurrentItem(1);
            } else if (list.get(i).getProvince_code() == -2) {
                indexWard = i;
                tableLayoutAddress.getTabAt(2).setText(listWards.get(i).getName());
                Toast.makeText(context, "Chọn địa chỉ thành công", Toast.LENGTH_SHORT).show();
                ward = listWards.get(i).getName();
                bottomSheetDialog.hide();
                divisionAddress = divisionAddress.replace("Thành phố", "Tp.");
                divisionAddress = divisionAddress.replace("Tỉnh", "");
                districtAddress = districtAddress.replace("Quận", "Q.");
                districtAddress = districtAddress.replace("Huyện", "H.");
                ward = ward.replace("Phường", "P.");
                tinhhuyenxa.setText(divisionAddress +"/"+ districtAddress +"/"+ward);
            } else {
                indexDistrict = i;
                districtAddress = listDistrict.get(i).getName();
                listWards.clear();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data = "";
                        try {
                            URL url = new URL("https://provinces.open-api.vn/api/d/" + listDistrict.get(i).getCode() + "?depth=2");
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                            InputStream inputStream = httpURLConnection.getInputStream();

                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;

                            while ((line = bufferedReader.readLine()) != null) {
                                data = data + line;
                                Log.d("DATA DEMO: NAME", "run: " + data);
                                if (!data.isEmpty()) {
                                    JSONObject jsonObject = new JSONObject(data);
                                    JSONArray jsonArray = jsonObject.getJSONArray("wards");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json = jsonArray.getJSONObject(i);
                                        String name = json.getString("name");
                                        int code = json.getInt("code");

                                        listWards.add(new Address(name, code, -2));
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }).start();
                if (tableLayoutAddress.getTabCount() > 2) {
                    tableLayoutAddress.removeTab(tableLayoutAddress.getTabAt(2));
                }
                tableLayoutAddress.addTab(tableLayoutAddress.newTab().setText("Phường/xã"));
                tableLayoutAddress.getTabAt(1).setText(listDistrict.get(i).getName());
                tableLayoutAddress.setScrollPosition(2, 0f, true);
                viewPagerAddress.setCurrentItem(2);
            }

        }
    }

}
