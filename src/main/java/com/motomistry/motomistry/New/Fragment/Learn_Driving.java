package com.motomistry.motomistry.New.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.R;


public class Learn_Driving extends Fragment {

    private View view;
    public static ViewPager driving_school_pager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_learn__driving,container,false);
        driving_school_pager = (ViewPager) view.findViewById(R.id.driving_school_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(driving_school_pager);
        driving_school_pager.setAdapter(new CustomPagerAdapter(getChildFragmentManager()));

        return view;
    }

    private class CustomPagerAdapter extends FragmentPagerAdapter {

        CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    return new DrivingPackages();

                case 1:
                    return new DrivingSchoolHistory();

                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){

                case 0:
                    return "Our Drving Packages";

                case 1:
                    return "Your Booking History";

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.learn_driving_main));

    }
}
