package nta.nguyenanh.code_application;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_History extends RecyclerView.Adapter<Adapter_History.ViewHolder> {
    public static Context context;
    public static List<History_model> ds;
    public Adapter_History(Context context,List<History_model> ds){
        this.context= context;
        this.ds = ds;
    }
    @NonNull
    @Override
    public Adapter_History.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_history_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_History.ViewHolder holder, int position) {
        holder.txt_name_history.setText(ds.get(position).getName_history());
    }
    @Override
    public int getItemCount() {
        return ds.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name_history;
        int MaNews;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_name_history =itemView.findViewById(R.id.txt_item_history);
        }
    }
}