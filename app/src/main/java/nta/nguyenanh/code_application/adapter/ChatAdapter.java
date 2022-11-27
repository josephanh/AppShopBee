package nta.nguyenanh.code_application.adapter;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.tvUser.setText(chat.sendBy);
        holder.tvText.setText(chat.text);
        Date date = new Date(chat.time);
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(date);
        holder.tvTime.setText(formattedDate + "");
        Log.d("TAG mes", "onBindViewHolder: "+chats.get(position).getSendBy());
        Log.d("TAG mes", "onBindViewHolder: "+userModel.getUserID());
        if (chats.get(position).getSendBy().equals(userModel.getUserID())){
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.gravity = Gravity.RIGHT;
//            holder.item_chat.setLayoutParams(params);
            holder.item_chat.setGravity(Gravity.RIGHT);
            Log.d("TAG mes", "bên phải ");
        }else {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.gravity = Gravity.LEFT;
//            holder.item_chat.setLayoutParams(params);
            holder.item_chat.setGravity(Gravity.LEFT);
            Log.d("TAG mes", "bên trái ");
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
