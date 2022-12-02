package nta.nguyenanh.code_application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import nta.nguyenanh.code_application.DetailProductActivity;
import nta.nguyenanh.code_application.R;
import nta.nguyenanh.code_application.model.Product;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.ViewHolder> {

    List<Product> list;
    Context context;

    public SuggestAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_suggeet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getImage().get(0)).into(holder.image_product);
        holder.name_product.setText(list.get(position).getNameproduct());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.price_product.setText(formatter.format(list.get(position).getPrice()));
        holder.tv_total.setText("Kho " + formatter.format(list.get(position).getTotal()));
        holder.item_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("product", list.get(position));
                context.startActivity(intent);
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
        TextView name_product, price_product;
        LinearLayout item_product;
        TextView tv_total;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_total = itemView.findViewById(R.id.tv_total);
            image_product = itemView.findViewById(R.id.image_product);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            item_product = itemView.findViewById(R.id.item_product);
        }
    }
}
