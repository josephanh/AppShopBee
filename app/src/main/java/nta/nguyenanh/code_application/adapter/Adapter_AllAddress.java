package nta.nguyenanh.code_application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.Address;

public class Adapter_AllAddress extends RecyclerView.Adapter<Adapter_AllAddress.ViewHolder>{

    Context context;
    ArrayList<Address> list;
    public Adapter_AllAddress(Context context, ArrayList<Address> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public Adapter_AllAddress.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.detail_alladdress, parent, false);
        return new Adapter_AllAddress.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_AllAddress.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//     holder.nameDiv.setText(list.get(position).getAddress());


    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvUsernameAddress,tvNumberPhoneAddress,tvAddress,availble;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvUsernameAddress = itemView.findViewById(R.id.tvUsernameAddress);
            tvNumberPhoneAddress = itemView.findViewById(R.id.tvNumberPhoneAddress);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            availble = itemView.findViewById(R.id.availble);

        }
    }





}

