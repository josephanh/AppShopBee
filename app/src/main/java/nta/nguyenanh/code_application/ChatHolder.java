package nta.nguyenanh.code_application;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



class ChatHolder extends RecyclerView.ViewHolder {


    public final TextView tvUser, tvText, tvTime;

    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        tvUser = itemView.findViewById(R.id.tvUser);
        tvText = itemView.findViewById(R.id.tvText);
        tvTime = itemView.findViewById(R.id.tvTime);
    }
}
