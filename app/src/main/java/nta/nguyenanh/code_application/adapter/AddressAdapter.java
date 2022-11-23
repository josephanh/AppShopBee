package nta.nguyenanh.code_application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.Address;

public class AddressAdapter extends BaseAdapter{

    Context context;
    ArrayList<Address> list;
    onClickItemAddress onClick;


    public AddressAdapter(Context context, ArrayList<Address> list, onClickItemAddress onClick) {
        this.context = context;
        this.list = list;
        this.onClick =  onClick;
    }

    @Override
    public int getCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    public ArrayList<Address> getList() {
        return list;
    }

    public void setList(ArrayList<Address> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView content = view.findViewById(R.id.content);
        content.setText(list.get(position).getName());

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(position);
            }
        });
        return view;
    }
    public interface onClickItemAddress{
        void onClick(int pos);
    }
}
