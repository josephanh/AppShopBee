package nta.nguyenanh.code_application.adapter;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nta.nguyenanh.code_application.ChatActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.listener.FirebaseQuery;
import nta.nguyenanh.code_application.model.Chat;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

    private Context context;

    private List<Chat> chats;

    public ChatAdapter(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat, parent, false);
        return new ChatHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, @SuppressLint("RecyclerView") int position) {
        Chat chat = chats.get(position);
        holder.tvUser.setText(chat.sendBy);
        Date date = new Date(chat.time);
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(date);
        holder.tvTime.setText(formattedDate + "");
        Log.d("TAG mes", "onBindViewHolder: "+chats.get(position).getSendBy());
        Log.d("TAG mes", "onBindViewHolder: "+userModel.getUserID());

        holder.tvText.setMaxWidth((int) (ChatActivity.width*0.6));
        Log.d("HEIGHT", "onBindViewHolder: "+chat.getText());
        String url = chat.getText();
        Glide.with(context).load(url).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.tvText.setText(chat.getText());
                holder.imageText.setVisibility(View.GONE);
                holder.item_text.setVisibility(View.VISIBLE);
                if (chats.get(position).getSendBy().equals(userModel.getUserID())){
                    holder.item_chat.setGravity(Gravity.RIGHT|Gravity.CENTER);
                    holder.iconLeft.setVisibility(View.GONE);
                    holder.iconRight.setVisibility(View.GONE);
                    Log.d("TAG mes", "bên phải ");

                }else {
                    holder.item_chat.setGravity(Gravity.LEFT|Gravity.CENTER);
                    holder.iconRight.setVisibility(View.GONE);
                    holder.iconLeft.setVisibility(View.VISIBLE);
                    Log.d("TAG mes", "bên trái ");
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.item_text.setVisibility(View.GONE);
                holder.imageText.setVisibility(View.VISIBLE);
                Glide.with(context).load(url).into(holder.imageText);
                if (chats.get(position).getSendBy().equals(userModel.getUserID())){
                    holder.item_chat.setGravity(Gravity.RIGHT|Gravity.CENTER);
                    holder.iconLeft.setVisibility(View.GONE);
                    holder.iconRight.setVisibility(View.GONE);
                    Log.d("TAG mes", "bên phải ");

                }else {
                    holder.item_chat.setGravity(Gravity.LEFT|Gravity.CENTER);
                    holder.iconRight.setVisibility(View.GONE);
                    holder.iconLeft.setVisibility(View.VISIBLE);
                    Log.d("TAG mes", "bên trái ");
                }
                return true;
            }
        }).into(holder.iconRight);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
