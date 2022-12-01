package nta.nguyenanh.code_application.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.Product;

public class FlashSaleHomeAdapter extends RecyclerView.Adapter<FlashSaleHomeAdapter.ViewHoler> {

    Context context;
    List<Product> list;

    public FlashSaleHomeAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flash_sale_home, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        Double Count= holder.bar_tong.getLayoutParams().width*Math.pow(list.get(position).getTotal(),-1) ;
        Glide.with(context).load(list.get(position).getImage().get(0)).into(holder.image_product);


        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.price_product.setText("đ"+" "+formatter.format(list.get(position).getPrice())+"");
        holder.sold.setText(list.get(position).getSold()+" ĐÃ BÁN");

        ViewGroup.LayoutParams parm = holder.bar_daban.getLayoutParams();
        parm.width = Integer.parseInt(Math.round(Count*list.get(position).getSold())+"");
        holder.bar_daban.setLayoutParams(parm);
    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    protected class ViewHoler extends RecyclerView.ViewHolder {
        ImageView image_product;
        TextView price_product,bar_tong,bar_daban,sold;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            price_product = itemView.findViewById(R.id.price_product);
            bar_tong = itemView.findViewById(R.id.bar_tong);
            sold = itemView.findViewById(R.id.sold);
            bar_daban = itemView.findViewById(R.id.bar_daban);
        }
    }
}
