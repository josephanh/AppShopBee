package nta.nguyenanh.code_application.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import nta.nguyenanh.code_application.R;


class ChatHolder extends RecyclerView.ViewHolder {


    public final TextView tvUser, tvText, tvTime;
    public final LinearLayout item_chat;
    public final ImageView iconLeft, iconRight, imageText;
    public final CardView item_text;
    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        tvUser = itemView.findViewById(R.id.tvUser);
        tvText = itemView.findViewById(R.id.tvText);
        tvTime = itemView.findViewById(R.id.tvTime);
        item_chat = itemView.findViewById(R.id.item_chat);
        iconLeft = itemView.findViewById(R.id.iconLeft);
        iconRight = itemView.findViewById(R.id.iconRight);
        imageText = itemView.findViewById(R.id.imageText);
        item_text = itemView.findViewById(R.id.item_text);

    }
}
