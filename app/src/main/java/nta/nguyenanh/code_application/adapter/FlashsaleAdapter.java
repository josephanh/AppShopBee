package nta.nguyenanh.code_application.adapter;

import android.content.Context;
import android.text.Html;
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

public class FlashsaleAdapter extends RecyclerView.Adapter<FlashsaleAdapter.ViewHolder> {
    public static Context context;
    public  List<Product> FsList;
    public FlashsaleAdapter(Context context,List<Product> FsList){

        this.context = context;
        this.FsList = FsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_flashsale,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Double Count= holder.txt_Flashsale_lineTong.getLayoutParams().width*Math.pow(FsList.get(position).getTotal(),-1) ;
        Glide.with(context).load(FsList.get(position).getImage().get(0)).into(holder.imgFlashsale);
        holder.saleFlashsale.setText("-"+FsList.get(position).getSale()+"%");
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.oldpriceFlashsale.setText(Html.fromHtml("<del>"+formatter.format(FsList.get(position).getPrice())+"đ"+"</del>"), TextView.BufferType.SPANNABLE);
        holder.newpriceFlashsale.setText(formatter.format(FsList.get(position).getPrice()*(1-FsList.get(position).getSale()*0.01))+"đ");
        holder.nameFlashsale.setText(FsList.get(position).getNameproduct());
        holder.txt_Flashsale_mount.setText(FsList.get(position).getSold()+" ĐÃ BÁN");
        ViewGroup.LayoutParams parm = holder.txt_Flashsale_line.getLayoutParams();
        parm.width = Integer.parseInt(Math.round(Count*FsList.get(position).getSold())+"");
        Log.d("TAG Count", "onBindViewHolder: "+ holder.txt_Flashsale_lineTong.getLayoutParams().width);
        Log.d("TAG Count", "onBindViewHolder: "+FsList.get(position).getTotal()*Math.pow(holder.txt_Flashsale_lineTong.getLayoutParams().width,-1));
        Log.d("TAG Count", "onBindViewHolder: "+Integer.parseInt(Math.round(Count*FsList.get(position).getSold())+""));
        holder.txt_Flashsale_line.setLayoutParams(parm);




    }

    @Override
    public int getItemCount() {
        if (FsList!=null){
            return FsList.size();
        }
        return 0;

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFlashsale;
        TextView nameFlashsale,oldpriceFlashsale,newpriceFlashsale,saleFlashsale, txt_Flashsale_mount,txt_Flashsale_line,txt_Flashsale_lineTong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlashsale = itemView.findViewById(R.id.imgFlashsale);
            nameFlashsale = itemView.findViewById(R.id.nameFlashsale);
            oldpriceFlashsale = itemView.findViewById(R.id.oldpriceFlashsale);
            newpriceFlashsale = itemView.findViewById(R.id.newpriceFlashsale);
            saleFlashsale = itemView.findViewById(R.id.saleFlashsale);
            txt_Flashsale_mount = itemView.findViewById(R.id.txt_Flashsale_mount);
            txt_Flashsale_line= itemView.findViewById(R.id.txt_Flashsale_line);
            txt_Flashsale_lineTong = itemView.findViewById(R.id.txt_Flashsale_lineTong);
        }
    }
}
