package nta.nguyenanh.code_application.listener;

import static android.content.Context.MODE_PRIVATE;
import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nta.nguyenanh.code_application.model.Address;
import nta.nguyenanh.code_application.model.User;

public class CheckLogin {
    Context context;

    public CheckLogin(Context context) {
        this.context = context;
    }

    public void readLogin() {
        SharedPreferences preferences = context.getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        Boolean isLoggedin = preferences.getBoolean("isLoggedin", false);
        if (isLoggedin) {
            String userid = preferences.getString("userid", null);
            String username = preferences.getString("username", null);
            String fullname = preferences.getString("fullname", null);
            String password = preferences.getString("password", null);
            String numberphone = preferences.getString("numberphone", null);

            // xem trên yt https://www.youtube.com/watch?v=xjOyvwRinK8&ab_channel=TechProjects
            Gson gson = new Gson();
            String json = preferences.getString("address", null);
            Type type = new TypeToken<ArrayList<Address>>() {
            }.getType();
            ArrayList<Address> addressList = gson.fromJson(json, type);
            if (addressList == null) {
                addressList = new ArrayList<>();
            }
            userModel = new User(addressList, null, fullname, password, numberphone, username, userid);
        }
    }

}
