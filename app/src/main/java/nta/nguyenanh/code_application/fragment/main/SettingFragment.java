package nta.nguyenanh.code_application.fragment.main;

import static android.content.Context.MODE_PRIVATE;
import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import nta.nguyenanh.code_application.LoginActivity;
import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;

public class SettingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView fullname = view.findViewById(R.id.fullname);
        Button buttonLogout = view.findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });

        fullname.setText(userModel.getFullname());
    }

    private void onLogout() {
        userModel = null;
        SharedPreferences preferences = getActivity().getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(null);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedin", false);
        editor.putString("userid", null);
        editor.putString("username", null);
        editor.putString("fullname", null);
        editor.putString("password", null);
        editor.putString("address", json);
        editor.putString("numberphone", null);
        editor.commit();
        LoginActivity.checkLogout = true;
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}
