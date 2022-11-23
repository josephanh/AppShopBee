package nta.nguyenanh.code_application;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    Toolbar toolbar;

    public static ArrayList<Address> listDivision = new ArrayList<>(), listDistricts = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Địa chỉ nhận hàng");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        new fetchData().start();
//        addAddress();

    }

    public void addAddress() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "name");
        hashMap.put("city", "địa chỉ nhận hàng");

        db.collection("user").document(userModel.getUserID())
                .update("address", hashMap)
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

    public void bottomsheet(View v) {
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_address_bottom_sheet, null);
        tableLayout = view.findViewById(R.id.tablayout);
        viewPager2 = view.findViewById(R.id.viewpager2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter);


        tableLayout.addTab(tableLayout.newTab().setText("Tỉnh / Thành phố"));
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

        if(viewPager2.getCurrentItem() == 0) {
            tableLayout.setTabRippleColor(ColorStateList.valueOf(Color.parseColor("#3D3D3D")));
//            tableLayout.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
//            tableLayout.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
//            tableLayout.getTabAt(1).removeBadge();
//            tableLayout.getTabAt(2).removeBadge();
        }


        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
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
                            Address addressDiv = new Address(name, code);
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
                        Log.d("DATA DEMO: ", "run: "+listDivision.get(40).getName());
                        Log.d("DATA DEMO: ", "run: "+listDistricts.get(100).getName());
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

    public Integer onClickItemAddress(int i) {
        int position = i;
        if(tableLayout.getTabCount() > 1) {
            tableLayout.removeTab(tableLayout.getTabAt(1));
        }
        tableLayout.addTab(tableLayout.newTab().setText("Quận / Huyện"));
        return position;
    }
}