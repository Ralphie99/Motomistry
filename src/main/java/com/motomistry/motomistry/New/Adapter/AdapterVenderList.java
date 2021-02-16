package com.motomistry.motomistry.New.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Model.ModelSubService;
import com.motomistry.motomistry.New.Fragment.FragmentSubServicesList;
import com.motomistry.motomistry.New.Fragment.FragmentVenderList;
import com.motomistry.motomistry.New.Model.ModelVenderList;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amit on 1/24/2018.
 */

public class AdapterVenderList extends BaseAdapter {
    private Context mContext;
    private ArrayList<ModelVenderList> data;

    public AdapterVenderList(Context c, ArrayList<ModelVenderList> modelComment){
        mContext = c;
        data=modelComment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.layout_vender_list, parent, false);
            TextView name=(TextView)convertView.findViewById(R.id.vender_name);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.shop_image);
            TextView locality = (TextView) convertView.findViewById(R.id.shop_locality);
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.shop_rating);
            name.setText(data.get(position).getVendor_name());
            if(data.get(position).getImage()!=null){
                Picasso.get().load(BaseClass.BaseUrl+data.get(position).getImage()).placeholder(R.drawable.shop).into(imageView);
            }
            locality.setText(data.get(position).getVicinity());
            if(data.get(position).getRating()!= 0.0){
                ratingBar.setRating((float)data.get(position).getRating());
            }

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseClass.VENDER_ID=data.get(position).getId();
                ((DashbordActivity) mContext).replace_Fragment(new FragmentSubServicesList(data.get(position).getId(),data.get(position).getVendor_name(),1));
            }
        });
        return convertView;
    }


    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }


}
