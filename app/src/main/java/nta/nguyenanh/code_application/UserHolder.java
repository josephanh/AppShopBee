package nta.nguyenanh.code_application;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



class UserHolder extends RecyclerView.ViewHolder {

    public ImageView imgAvatar;
    public TextView tvUsername;
    public TextView tvName;

    public UserHolder(@NonNull View convertView) {
        super(convertView);


        imgAvatar = convertView.findViewById(R.id.imgAvatar);
        tvUsername = convertView.findViewById(R.id.tvUsername);
        tvName = convertView.findViewById(R.id.tvName);

    }


}
