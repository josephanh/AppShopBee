package nta.nguyenanh.code_application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.fragment.pay.AllAddressFragment;
import nta.nguyenanh.code_application.interfaces.OnClickUpdateAddress;
import nta.nguyenanh.code_application.model.Address;

public class Adapter_AllAddress extends RecyclerView.Adapter<Adapter_AllAddress.ViewHolder> {

    Context context;
    ArrayList<Address> list;
    OnClickUpdateAddress onClickUpdateAddress;
    private int row_index = 0;

    public Adapter_AllAddress(Context context, ArrayList<Address> list, OnClickUpdateAddress onClickUpdateAddress) {
        this.context = context;
        this.list = list;
        this.onClickUpdateAddress = onClickUpdateAddress;

    }

    @NonNull
    @Override
    public Adapter_AllAddress.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_alladdress, parent, false);
        return new Adapter_AllAddress.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_AllAddress.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvUsernameAddress.setText(list.get(position).getNameReceiver());
        holder.tvNumberPhoneAddress.setText(list.get(position).getPhonenumber());
        holder.tvAddress.setText(list.get(position).getAddress());

        if (list.get(position).getAvailable() == 0) {
            holder.availble.setText("Nhà riêng");
        } else {
            holder.availble.setText("Văn phòng");
        }
        holder.btn_Update_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateAddress.OnClickUpdate(position);
                Log.d("TAG address", "onClick: " + list.get(position));
            }
        });

        holder.chkMarkAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    row_index = position;
                    Log.d("CHECK", "onBindViewHolder: "+isChecked);
                    notifyDataSetChanged();
                    onClickUpdateAddress.OnClickCheckBox(position);
                } else {

                }
            }
        });

        if (row_index == position) {
            holder.chkMarkAddress.setButtonDrawable(R.drawable.ic_checkbox_true);
            Log.d("CHECK", "onBindViewHolder: "+position);
        } else {
            holder.chkMarkAddress.setButtonDrawable(R.drawable.ic_checkbox_false);
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsernameAddress, tvNumberPhoneAddress, tvAddress, availble;
        Button btn_Update_Address;
        CheckBox chkMarkAddress;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvUsernameAddress = itemView.findViewById(R.id.tvUsernameAddress);
            tvNumberPhoneAddress = itemView.findViewById(R.id.tvNumberPhoneAddress);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            availble = itemView.findViewById(R.id.availble);
            btn_Update_Address = itemView.findViewById(R.id.btn_Update_Address);
            chkMarkAddress = itemView.findViewById(R.id.chkMarkAddress);

        }
    }

}

