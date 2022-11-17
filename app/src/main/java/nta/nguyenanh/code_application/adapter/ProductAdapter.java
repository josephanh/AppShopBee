package nta.nguyenanh.code_application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.List;

import nta.nguyenanh.code_application.MainActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.interfaces.OnClickItemProduct;
import nta.nguyenanh.code_application.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<Product> list;
    Context context;

    public ProductAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getImage().get(0)).into(holder.image_product);
        holder.name_product.setText(list.get(position).getNameproduct());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.price_product.setText(formatter.format(list.get(position).getPrice())+"đ");
        holder.saleoff.setText(list.get(position).getSale().toString()+"%");
        if(list.get(position).getSale() != 0) {
            holder.freeship.setVisibility(View.VISIBLE);
            holder.saleoff.setVisibility(View.VISIBLE);
        } else {
            holder.freeship.setVisibility(View.GONE);
            holder.saleoff.setVisibility(View.GONE);
        }

        holder.item_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickItemProduct clickEvent = ((MainActivity)context);
                clickEvent.GoToActivity(list.get(position));
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image_product;
        ImageView freeship;
        TextView name_product, saleoff, sold, price_product;
        LinearLayout item_product;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            saleoff = itemView.findViewById(R.id.saleoff);
            item_product = itemView.findViewById(R.id.item_product);
            freeship = itemView.findViewById(R.id.freeship);
        }
    }
}
