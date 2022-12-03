package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

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

import nta.nguyenanh.code_application.adapter.ViewPagerAdapter;
import nta.nguyenanh.code_application.model.Address;

public class AddressActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomSheetDialog bottomSheetDialog;
    private TabLayout tableLayout;
    private ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    TextView tinhhuyenxa;

    EditText edt_namereceiver, edt_numberphone, edt_streets, edt_house;
    Button btn_confirm;

    Toolbar toolbar;

    public static ArrayList<Address> listDivision = new ArrayList<>(), listDistricts = new ArrayList<>();
    public static ArrayList<Address> listDistrict = new ArrayList<>(), listWards = new ArrayList<>();
    private String division, district, ward;
    public static int indexDivision = -1, indexDistrict = -1, indexWard = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        toolbar = findViewById(R.id.toolbar);
        tinhhuyenxa = findViewById(R.id.tinhhuyenxa);
        edt_namereceiver = findViewById(R.id.edt_namereceiver);
        edt_numberphone = findViewById(R.id.edt_numberphone);
        edt_streets = findViewById(R.id.edt_streets);
        edt_house = findViewById(R.id.edt_house);
        btn_confirm = findViewById(R.id.btn_confirm);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Địa chỉ nhận hàng");
        String NameReceiver = getIntent().getStringExtra("NameReceiver");
        String PhoneNumber = getIntent().getStringExtra("PhoneNumber");
        String Address = getIntent().getStringExtra("Address");
        String Available = getIntent().getStringExtra("Available");



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress(new Update());
            }
        });


        new fetchData().start();


    }
    public void updateAddress(){

    }

    public void addAddress(Update update) {

        String phonenumber = edt_numberphone.getText().toString().trim();
        String namereceiver = edt_namereceiver.getText().toString().trim();
        String place = tinhhuyenxa.getText().toString().trim();
        int availble;

        if(phonenumber.isEmpty() || namereceiver.isEmpty() || place.isEmpty()) {
            Toast.makeText(this, "Bạn hãy điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        // Thay thế chuỗi cho gọn
        division = division.replace("Thành phố", "Tp.");
        district = district.replace("Quận", "Q.");
        district = district.replace("Huyện", "H.");
        ward = ward.replace("Phường", "P.");
        place = ward+"/"+district+"/"+division;
        Address address = new Address(null,place, namereceiver, phonenumber, 0);
        HashMap<String, Object> places = new HashMap<>();
        places.put("address"+System.currentTimeMillis(), address);

        update.updateAddress(places);
    }




    public void bottomsheet(View v) {
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_address_bottom_sheet, null);
        tableLayout = view.findViewById(R.id.tablayout);
        viewPager2 = view.findViewById(R.id.viewpager2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter);

        tableLayout.setSelectedTabIndicator(R.drawable.round_back_theme_diachi);
        if(indexDivision != -1 && indexDistrict != -1 && indexWard != -1){
            tableLayout.addTab(tableLayout.newTab().setText(listDivision.get(indexDivision).getName()));
            tableLayout.addTab(tableLayout.newTab().setText(listDistrict.get(indexDistrict).getName()));
            tableLayout.addTab(tableLayout.newTab().setText(listWards.get(indexWard).getName()));
        } else {
            tableLayout.addTab(tableLayout.newTab().setText("Tỉnh / Thành phố"));
        }

//        tableLayout.addTab(tableLayout.newTab().setText("Quận / Huyện"));
//        tableLayout.addTab(tableLayout.newTab().setText("Phường / Xã"));

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });

        if (viewPager2.getCurrentItem() == 0) {
            tableLayout.setTabRippleColor(ColorStateList.valueOf(Color.parseColor("#3D3D3D")));
//            tableLayout.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
//            tableLayout.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
//            tableLayout.getTabAt(1).removeBadge();
//            tableLayout.getTabAt(2).removeBadge();
        }


        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    public void onClickItemAddress(int i, ArrayList<Address> list) {

        if (list.get(i).getProvince_code() == -1) {
            division = listDivision.get(i).getName();
            indexDivision = i;
            indexDistrict = -1;
            indexWard = -1;
            if (tableLayout.getTabCount() == 2) {
                tableLayout.removeTab(tableLayout.getTabAt(1));
            }
            if (tableLayout.getTabCount() == 3) {
                tableLayout.removeTab(tableLayout.getTabAt(1));
                tableLayout.removeTab(tableLayout.getTabAt(1));
            }
            tableLayout.addTab(tableLayout.newTab().setText("Quận/Huyện"));
            tableLayout.getTabAt(0).setText(listDivision.get(i).getName());
            listDistrict.clear();
            for (int j = 0; j < listDistricts.size(); j++) {
                if (listDistricts.get(j).getProvince_code() == listDivision.get(i).getCode()) {
                    listDistrict.add(listDistricts.get(j));
                }
            }
            viewPagerAdapter.notifyDataSetChanged();
            Log.d("DATA", "onClickItemAddress: " + listDistrict.size());
            tableLayout.setScrollPosition(1, 0f, true);
            tableLayout.selectTab(tableLayout.getTabAt(1));
            viewPager2.setCurrentItem(1);
        } else if (list.get(i).getProvince_code() == -2) {
            indexWard = i;
            tableLayout.getTabAt(2).setText(listWards.get(i).getName());
            Toast.makeText(this, "Chọn địa chỉ thành công", Toast.LENGTH_SHORT).show();
            ward = listWards.get(i).getName();
            bottomSheetDialog.hide();
            division = division.replace("Thành phố", "Tp.");
            division = division.replace("Tỉnh", "");
            district = district.replace("Quận", "Q.");
            district = district.replace("Huyện", "H.");
            ward = ward.replace("Phường", "P.");
            tinhhuyenxa.setText(division+"/"+district+"/"+ward);
        } else {
            indexDistrict = i;
            district = listDistrict.get(i).getName();
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
            if (tableLayout.getTabCount() > 2) {
                tableLayout.removeTab(tableLayout.getTabAt(2));
            }
            tableLayout.addTab(tableLayout.newTab().setText("Phường/xã"));
            tableLayout.getTabAt(1).setText(listDistrict.get(i).getName());
            tableLayout.setScrollPosition(2, 0f, true);
            viewPager2.setCurrentItem(2);
        }

    }

    // lấy dữ liệu ban đầu
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

    // update phonenumber
    class Update{

        public void updateAddress(HashMap<String, Object> address) {
            db.collection("user").document(userModel.getUserID())
                    .update("address", address)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

}