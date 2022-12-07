package nta.nguyenanh.code_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import nta.nguyenanh.code_application.adapter.ViewPagerAdapter;
import nta.nguyenanh.code_application.dialog.DiaLogSus;
import nta.nguyenanh.code_application.fragment.address.AddressFragment;
import nta.nguyenanh.code_application.fragment.pay.PayFragment;
import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.ProductCart;
import nta.nguyenanh.code_application.notification.SendNotification;

public class PayActivity extends AppCompatActivity {

    ArrayList<ProductCart> listCart = new ArrayList<>();
    private Button btn_buy_d;
    Toolbar toolBar;

    BottomSheetDialog bottomSheetDialog;
    private TabLayout tableLayout;
    private ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    TextView tinhhuyenxa;

    EditText edt_namereceiver, edt_numberphone, edt_streets, edt_house;
    Button btn_confirm;

    public static ArrayList<Address> listDivision = new ArrayList<>(), listDistricts = new ArrayList<>();
    public static ArrayList<Address> listDistrict = new ArrayList<>(), listWards = new ArrayList<>();
    public static int indexDivision = -1, indexDistrict = -1, indexWard = -1;
    private String division, district, ward;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        toolBar = findViewById(R.id.toolBar);

        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        listCart = (ArrayList<ProductCart>) getIntent().getSerializableExtra("listPay");
        boolean addressCheck = getIntent().getBooleanExtra("addressCheck", false);

        if (!addressCheck) {
            changeFragment(new AddressFragment().newInstance(listCart, getIntent().getIntExtra("position", -1)));
            getSupportActionBar().setTitle("Địa chỉ nhận hàng");
        } else {
            changeFragment(new PayFragment().newInstance(listCart));
            getSupportActionBar().setTitle("Thanh toán");
        }
    }

    public void buyEvent(String price) {
        String title = "Chúc mừng đặt hàng thành công!";
        String content = MainActivity.userModel.getFullname() + " đã đặt hàng thành công với tổng giá trị: " + price + "đ";
        SendNotification notification = new SendNotification(PayActivity.this);
        notification.customNotification(title, content);
        Toast.makeText(PayActivity.this, "Đặt hàng thành công !!!", Toast.LENGTH_SHORT).show();
        DiaLogSus diaLogSus = new DiaLogSus(PayActivity.this, new DiaLogSus.goToHome() {
            @Override
            public void gotoMain() {
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        diaLogSus.showDiaLog();

    }

    @Override
    protected void onResume() {
//        list.clear();
//        list = (ArrayList<ProductCart>) getIntent().getSerializableExtra("listPay");
        super.onResume();
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
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
            tinhhuyenxa.setText(division + "/" + district + "/" + ward);
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

    @Override
    protected void onPause() {
        super.onPause();
        listCart.clear();
    }
}