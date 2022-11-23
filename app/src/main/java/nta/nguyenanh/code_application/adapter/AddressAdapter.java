package nta.nguyenanh.code_application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.Address;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    Context context;
    ArrayList<Address> list;
    onClickItemAddress onClick;

    public AddressAdapter(Context context, ArrayList<Address> list, onClickItemAddress onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameDiv.setText(list.get(position).getName());
        holder.nameDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameDiv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDiv = itemView.findViewById(R.id.content);
        }
    }

    public ArrayList<Address> getList() {
        return list;
    }

    public void setList(ArrayList<Address> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    // các hàm của listview để đổ dữ liệu -- khi thay đổi nhớ extends BaseAdapter

//    public AddressAdapter(Context context, ArrayList<Address> list, onClickItemAddress onClick) {
//        this.context = context;
//        this.list = list;
//        this.onClick =  onClick;
//    }
//
//    @Override
//    public int getCount() {
//        if(list != null) {
//            return list.size();
//        }
//        return 0;
//    }
//
//    public ArrayList<Address> getList() {
//        return list;
//    }
//
//    public void setList(ArrayList<Address> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        TextView content = view.findViewById(R.id.content);
//        content.setText(list.get(position).getName());
//
//        content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClick.onClick(position);
//            }
//        });
//        return view;
//    }
    public interface onClickItemAddress{
        void onClick(int pos);
    }
}
