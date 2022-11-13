package nta.nguyenanh.code_application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import nta.nguyenanh.code_application.R;

public class BannerAdapter extends PagerAdapter {
    private Context context;
    List<String> listPhoto;

    public BannerAdapter(Context context, List<String> listPhoto) {
        this.context = context;
        this.listPhoto = listPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner_home, container, false);
        ImageView imgPhoto = view.findViewById(R.id.imgPhoto);
        if(listPhoto.get(position) != null) {
            Glide.with(context)
                    .load(listPhoto.get(position))
                    .into(imgPhoto);
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(listPhoto != null) {
            return listPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
