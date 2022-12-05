package nta.nguyenanh.code_application.adapter;

import static nta.nguyenanh.code_application.PayActivity.listDistrict;
import static nta.nguyenanh.code_application.PayActivity.listDivision;
import static nta.nguyenanh.code_application.PayActivity.listWards;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import nta.nguyenanh.code_application.fragment.address.DistrictsFragment;
import nta.nguyenanh.code_application.fragment.address.DivisionFragment;
import nta.nguyenanh.code_application.fragment.address.WardsFragment;

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
                return new DivisionFragment().newInstance(listDivision);
            case 1:
                return new DistrictsFragment().newInstance(listDistrict);
            case 2:
                return new WardsFragment().newInstance(listWards);
            default:
                return new DivisionFragment().newInstance(listDivision);
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
