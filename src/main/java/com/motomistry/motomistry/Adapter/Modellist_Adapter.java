package com.motomistry.motomistry.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.motomistry.motomistry.Fragment.getModel;
import com.motomistry.motomistry.Model.Modellist;
import com.motomistry.motomistry.R;

import java.util.ArrayList;

/**
 * Created by Amit on 23-Mar-18.
 */

public class Modellist_Adapter extends BaseAdapter {

    ArrayList<Modellist> data;
    Context context;

    public Modellist_Adapter(Context context, ArrayList<Modellist> data){

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

            convertView = LayoutInflater.from(context).inflate(R.layout.list_element_2,parent,false);
        }
        Modellist current_item = (Modellist) getItem(position);
        TextView text = (TextView) convertView.findViewById(R.id.name);
            text.setText(current_item.getName());
        return convertView;
    }

}
