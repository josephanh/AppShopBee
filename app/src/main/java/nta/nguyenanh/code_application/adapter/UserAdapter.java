package nta.nguyenanh.code_application.adapter;

import static nta.nguyenanh.code_application.MainActivity.userModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.User2;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
    private Context context;
    private List<User2> users;

    public UserAdapter(Context context, List<User2> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User2 user = users.get(position);
        if (userModel.getUserID().equals("f7xs0HqMzaYhs8QdW3xO")){


            holder.tvUsername.setText(user.username);
        }else {
            if (user.username.contains("f7xs0HqMzaYhs8QdW3xO")){
                holder.tvUsername.setText(user.username);
                Toast.makeText(context, "có chủ shop", Toast.LENGTH_SHORT).show();
            }else{
                holder.tvUsername.setVisibility(View.GONE);
                holder.imgAvatar.setVisibility(View.GONE);
            }
            Log.d("TAG user2", "onBindViewHolder: "+user.username);
        }





    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
