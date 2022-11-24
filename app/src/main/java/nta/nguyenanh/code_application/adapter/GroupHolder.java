package nta.nguyenanh.code_application.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nta.nguyenanh.code_application.R;


class GroupHolder extends RecyclerView.ViewHolder {


    public TextView tvGroupName;
    public TextView tvLastUpdate;
    public TextView tvTime;
    public View item;
    public GroupHolder(@NonNull View convertView) {
        super(convertView);
        item = convertView;
        tvGroupName = convertView.findViewById(R.id.tvGroupName);
        tvLastUpdate = convertView.findViewById(R.id.tvLastUpdate);
        tvTime = convertView.findViewById(R.id.tvTime);
    }

}
