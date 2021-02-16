package com.motomistry.motomistry.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ConfirmBooking extends Fragment {

    private View view;
    private TextView vendor, vehicle_name, day1, day2, day3, date1, date2, date3, selected_date;
    private ImageView select_date, open_requirements;
    private LinearLayout daydate1, daydate2;
    private Switch pick_drop_switch;
    private EditText additional_edit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_confirm_booking, container, false);

        select_date = (ImageView) view.findViewById(R.id.select_date);
        open_requirements = (ImageView) view.findViewById(R.id.open_requirements);
        additional_edit = (EditText) view.findViewById(R.id.additional_edit);
        daydate1 = (LinearLayout) view.findViewById(R.id.daydate1);
        daydate2 = (LinearLayout) view.findViewById(R.id.daydate2);

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        selected_date.setText(date);

        open_requirements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additional_edit.setVisibility(View.VISIBLE);
            }
        });

        daydate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daydate1.setAnimation(new AnimationUtils().loadAnimation(getActivity(),R.anim.null_tofade));
            }
        });
        daydate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daydate2.setAnimation(new AnimationUtils().loadAnimation(getActivity(),R.anim.null_tofade));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.confirm_booking_main));

    }
}
