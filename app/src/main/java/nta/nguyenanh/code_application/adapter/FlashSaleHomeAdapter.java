package nta.nguyenanh.code_application.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import nta.nguyenanh.code_application.FlashsaleAdapter;
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
        Glide.with(context).load(list.get(position).getImage().get(0)).into(holder.image_product);
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
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
        }
    }
}
