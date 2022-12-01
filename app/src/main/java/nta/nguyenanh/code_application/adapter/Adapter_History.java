package nta.nguyenanh.code_application.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.dao.DAO_History;
import nta.nguyenanh.code_application.interfaces.OnClickItemSearchHistory;
import nta.nguyenanh.code_application.model.History;

public class Adapter_History extends RecyclerView.Adapter<Adapter_History.ViewHolder> {
    public static Context context;
    public static List<History> ds;
    OnClickItemSearchHistory onClickItemSearchHistory;

    public Adapter_History(Context context,List<History> ds,OnClickItemSearchHistory onClickItemSearchHistory){
        this.context= context;
        this.ds = ds;
        this.onClickItemSearchHistory = onClickItemSearchHistory;
    }

    @NonNull
    @Override
    public Adapter_History.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_History.ViewHolder holder, int position) {
        holder.txt_name_history.setText(ds.get(position).getName_history());
        holder.MaNews = ds.get(position).getId_history();
        holder.txt_delete_item_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO_History dh =new DAO_History(context);
                dh.delete(holder.MaNews+"");
                ds= dh.getAll();
                notifyDataSetChanged();
            }
        });
        holder.txt_name_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onClickItemSearchHistory.OnClickItemSearchHistory(holder.txt_name_history.getText().toString());
//                onClickItemSearchHistory.OnClickItemSearchHistory(holder.txt_name_history.getText().toString());
            }
        });
    }
    @Override
    public int getItemCount() {
        return ds.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name_history;
        ImageView txt_delete_item_history;
        int MaNews;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txt_name_history =itemView.findViewById(R.id.txt_item_history);
            txt_delete_item_history = itemView.findViewById(R.id.txt_delete_item_history);
        }
    }

}