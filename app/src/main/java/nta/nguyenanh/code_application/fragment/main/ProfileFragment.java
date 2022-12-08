package nta.nguyenanh.code_application.fragment.main;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nta.nguyenanh.code_application.R;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView fullname = view.findViewById(R.id.fullname);
        if (userModel != null){
            fullname.setText(userModel.getFullname());
        }else {
            fullname.setText("");
        }

    }
}
