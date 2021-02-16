package com.motomistry.motomistry.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;

public class profile_data extends Fragment {

    private View view;
    Session session;
    TextView name,city,phone,email,view_address,my_orders;
    ImageView my_cart,toolbar_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_data,container,false);

        session = new Session(getActivity());

        name = (TextView)view.findViewById(R.id.user_name);
        city = (TextView)view.findViewById(R.id.city);
        phone = (TextView)view.findViewById(R.id.phone);
        email = (TextView)view.findViewById(R.id.email);
        view_address = (TextView) view.findViewById(R.id.view_address);
        my_orders = (TextView) view.findViewById(R.id.view_orders);

        my_cart = (ImageView) view.findViewById(R.id.my_cart);
        toolbar_back = (ImageView) view.findViewById(R.id.toolbar_back);

        name.setText(session.getUserDetails().get(Session.NAME_KEY));
        phone.setText(session.getUserDetails().get(Session.MOBILE_KEY));
        email.setText(session.getUserDetails().get(Session.EMAIL_KEY));

        view_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.view_from_profile = true;
                ((DashbordActivity)getActivity()).replace_Fragment(new Select_Address());
            }
        });

        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new My_Orders());
            }
        });

        my_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new My_Cart());
            }
        });

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).bacPOP();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.profile_data_main));

    }
}
