package nta.nguyenanh.code_application.adapter;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import nta.nguyenanh.code_application.fragment.PhuongXaFragment;
import nta.nguyenanh.code_application.fragment.Quan_HuyenFragment;
import nta.nguyenanh.code_application.fragment.Tinh_ThanhphoFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Tinh_ThanhphoFragment();
            case 1:
                return new Quan_HuyenFragment();
            case 2:
                return new PhuongXaFragment();
            default:
                return new Tinh_ThanhphoFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
