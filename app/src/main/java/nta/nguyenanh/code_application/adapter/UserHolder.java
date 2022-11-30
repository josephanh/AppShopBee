package nta.nguyenanh.code_application.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nta.nguyenanh.code_application.R;


class UserHolder extends RecyclerView.ViewHolder {

    public ImageView imgAvatar;
    public TextView tvUsername;


    public UserHolder(@NonNull View convertView) {
        super(convertView);


        imgAvatar = convertView.findViewById(R.id.imgAvatar);
        tvUsername = convertView.findViewById(R.id.tvUsername);
//        tvName = convertView.findViewById(R.id.tvName);

    }


}
