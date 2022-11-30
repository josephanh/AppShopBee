package nta.nguyenanh.code_application.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import nta.nguyenanh.code_application.fragment.onboarding.Onboarding_Fragment_1;
import nta.nguyenanh.code_application.fragment.onboarding.Onboarding_Fragment_2;
import nta.nguyenanh.code_application.fragment.onboarding.Onboarding_Fragment_3;

public class ViewOnBoardPaperAdapter extends FragmentStatePagerAdapter {

    public ViewOnBoardPaperAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Onboarding_Fragment_1();
            case 1:
                return new Onboarding_Fragment_2();
            case 2:
                return new Onboarding_Fragment_3();
            default:
                return new Onboarding_Fragment_1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
