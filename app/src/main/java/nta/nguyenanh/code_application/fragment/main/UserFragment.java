package nta.nguyenanh.code_application.fragment.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import nta.nguyenanh.code_application.R;

public class UserFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager manager = getParentFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_profile, new ProfileFragment())
                .commit();

        Button profile = view.findViewById(R.id.profile);
        Button setting = view.findViewById(R.id.setting);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction()
                        .replace(R.id.fragment_profile, new ProfileFragment())
                        .commit();

                // đổi nền cho button
                profile.setBackgroundResource(R.drawable.custom_btn_setting);
                setting.setBackgroundResource(R.drawable.custom_btn_profile);

                // đổi màu chữ
                setting.setTextColor(Color.parseColor("#FFFFFF"));
                profile.setTextColor(Color.parseColor("#87A0F1"));

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction()
                        .replace(R.id.fragment_profile, new SettingFragment())
                        .commit();
                setting.setBackgroundResource(R.drawable.custom_btn_setting);
                profile.setBackgroundResource(R.drawable.custom_btn_profile);

                profile.setTextColor(Color.parseColor("#FFFFFF"));
                setting.setTextColor(Color.parseColor("#87A0F1"));
            }
        });
    }
}
