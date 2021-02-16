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

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.R;


public class Order_Details extends Fragment {

    private View view;
    TextView label,order_id,item_label,order_product_type,order_container_type,order_package_type,order_price,quantity_count;
    ImageView hamburger,order_product_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order__details,container,false);

        init(view);



        return view;
    }

    private void init(View view){


        order_id = (TextView) view.findViewById(R.id.order_id);
        order_price = (TextView) view.findViewById(R.id.order_price);
        order_product_type = (TextView) view.findViewById(R.id.order_product_type);
        item_label = (TextView) view.findViewById(R.id.item_label);
        quantity_count = (TextView) view.findViewById(R.id.quantity_count);
        order_product_image = (ImageView) view.findViewById(R.id.order_product_image);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.order_details_main));

    }
}
