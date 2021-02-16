package com.motomistry.motomistry.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.motomistry.motomistry.Fragment.getModel;
import com.motomistry.motomistry.Model.Modellist2;
import com.motomistry.motomistry.R;

import java.util.ArrayList;

/**
 * Created by Amit on 24-Mar-18.
 */

public class Modellist_Adapter2 extends BaseAdapter {

    ArrayList<Modellist2> data;
    Context context;

    public Modellist_Adapter2(Context context, ArrayList<Modellist2> data){

        this.data = data;
        this.context = context;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(R.layout.layout_model_list,parent,false);
        }

        Modellist2 current_item = (Modellist2) getItem(position);

        TextView text = (TextView) convertView.findViewById(R.id.name);
        text.setText(current_item.getModel_name());
        return convertView;
    }

}
