package nta.nguyenanh.code_application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import nta.nguyenanh.code_application.CartActivity;
import nta.nguyenanh.code_application.R;

import nta.nguyenanh.code_application.interfaces.OnclickItemCart;
import nta.nguyenanh.code_application.model.ProductCart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<ProductCart> list;
    Context context;
    OnclickItemCart onclickItemCart;

    public CartAdapter(ArrayList<ProductCart> list, Context context, OnclickItemCart onclickItemCart) {
        this.list = list;
        this.context = context;
        this.onclickItemCart = onclickItemCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        onclickItemCart.hideCheck(holder.checkboxCart, holder.btn_delete_cart);

        holder.checkboxCart.setChecked(true);
        holder.checkboxCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onclickItemCart.onClickCheck(isChecked, Integer.parseInt(holder.total_product.getText().toString()) ,list.get(position).getPrice());
            }
        });
        Glide.with(context).load(list.get(position).getImage()).into(holder.image_product);
        holder.name_product.setText(list.get(position).getNameproduct());
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.price_product.setText(formatter.format(list.get(position).getPrice())+"đ");
        holder.total_product.setText(list.get(position).getTotal().toString());
        holder.btn_delete_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickItemCart.onClickDelete(list.get(position).getId(), position);
            }
        });
        holder.colorProduct.setBackgroundColor(Color.parseColor(list.get(position).getColor()));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(holder.total_product.getText().toString()) == 20) {
                    Toast.makeText(context, "Không được mua quá 20 sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    int total = Integer.parseInt(holder.total_product.getText().toString()) + 1;
                    onclickItemCart.onClickPlus(total, Integer.parseInt(holder.total_product.getText().toString()), list.get(position).getPrice());
                    holder.total_product.setText(total+"");
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.parseInt(holder.total_product.getText().toString());
                if(total == 1) {
                    Toast.makeText(context, "Số lượng phải ít nhất là 1", Toast.LENGTH_SHORT).show();
                } else  {
                    total = Integer.parseInt(holder.total_product.getText().toString()) - 1;
                    onclickItemCart.onClickMinus(total, Integer.parseInt(holder.total_product.getText().toString()) , list.get(position).getPrice());
                    holder.total_product.setText(total+"");
                }
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_product, price_product, total_product;
        ImageView minus, plus;
        ImageView image_product;
        CheckBox checkboxCart;
        ImageView btn_delete_cart;
        RoundedImageView colorProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_delete_cart = itemView.findViewById(R.id.btn_delete_cart);
            checkboxCart = itemView.findViewById(R.id.checkboxCart);
            name_product = itemView.findViewById(R.id.name_product);
            price_product = itemView.findViewById(R.id.price_product);
            total_product = itemView.findViewById(R.id.total_product);
            minus = itemView.findViewById(R.id.minus);
            plus = itemView.findViewById(R.id.plus);
            image_product = itemView.findViewById(R.id.image_product);
            colorProduct = itemView.findViewById(R.id.colorProduct);
        }
    }

    public ArrayList<ProductCart> getList() {
        notifyDataSetChanged();
        return list;
    }

    public void setList(ArrayList<ProductCart> list) {
        notifyDataSetChanged();
        this.list = list;
    }
}
