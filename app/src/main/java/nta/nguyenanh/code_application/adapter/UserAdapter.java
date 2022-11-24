package nta.nguyenanh.code_application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        holder.tvName.setText(user.firstName);
        holder.tvUsername.setText(user.username);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
