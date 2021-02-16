package com.motomistry.motomistry.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;


public class Profile extends Fragment {

    private View view;
    LinearLayout my_vehicles,profile_data,log_out,about_us;
    Session session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((DashbordActivity)getActivity()).selectIcon(4);

        view = inflater.inflate(R.layout.fragment_profile,container,false);

        session = new Session(getActivity());

        my_vehicles = (LinearLayout) view.findViewById(R.id.my_vehicles);
        my_vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new MyVehicles());
            }
        });

        profile_data = (LinearLayout) view.findViewById(R.id.profile_data);
        profile_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new profile_data());
            }
        });

        log_out = (LinearLayout) view.findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                ((DashbordActivity)getActivity()).finish();
            }
        });

        about_us = (LinearLayout) view.findViewById(R.id.about_us);
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new About());
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.profile_main));

    }
}
