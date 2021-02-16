package com.motomistry.motomistry.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Accessory_item_model;
import com.motomistry.motomistry.Model.My_Cart_Model;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class My_Cart extends Fragment {

    private View view;
    ListView orders_list;
    static ArrayList<Accessory_item_model> data = new ArrayList<>();
    ArrayList<My_Cart_Model> data2 = new ArrayList<>();
    static ArrayList<String> qty = new ArrayList<>();
    List<String> qty_list;
    LinearLayout no_items,continue_to_payment,continue_bottom;
    ImageView back_button,go_home;
    TextView final_price;
    int net_price;
    Session session;
    ConnectionManager connectionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my__cart,container,false);
        session = new Session(getActivity());

        orders_list = (ListView) view.findViewById(R.id.order_list);
        no_items = (LinearLayout) view.findViewById(R.id.no_items);
        back_button = (ImageView) view.findViewById(R.id.back_button);
        go_home = (ImageView) view.findViewById(R.id.go_home);
        continue_to_payment = (LinearLayout) view.findViewById(R.id.continue_to_payment);
        continue_bottom = (LinearLayout) view.findViewById(R.id.continue_bottom);
        final_price = (TextView) view.findViewById(R.id.final_price);

        View no_internet = view.findViewById(R.id.no_internet);
        connectionManager = new ConnectionManager(getActivity());
        if (!connectionManager.isConnected()) {
            orders_list.setVisibility(View.GONE);
            continue_bottom.setVisibility(View.GONE);
            no_internet.setVisibility(View.VISIBLE);
        } else {
            no_internet.setVisibility(View.GONE);
            orders_list.setVisibility(View.VISIBLE);
            readFromSession();
            if(data2.size() == 0){
                no_items.setVisibility(View.VISIBLE);
                continue_bottom.setVisibility(View.GONE);
            }else {
                no_items.setVisibility(View.GONE);
                continue_bottom.setVisibility(View.VISIBLE);
            }
        }

        if(Constant.clickedFromHome) {
            back_button.setVisibility(View.VISIBLE);
            go_home.setVisibility(View.GONE);
            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity) getActivity()).bacPOP();
                }
            });
        }
        else{
            go_home.setVisibility(View.VISIBLE);
            back_button.setVisibility(View.GONE);
            go_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<4;i++){
                        ((DashbordActivity)getActivity()).bacPOP();
                    }
                }
            });
        }

        continue_to_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.view_from_profile = false;
                ((DashbordActivity)getActivity()).replace_Fragment(new Select_Address(My_Cart.this));
            }
        });



        qty_list = new ArrayList<String>();
        qty_list.add("1");
        qty_list.add("2");
        qty_list.add("3");
        qty_list.add("4");
        qty_list.add("5");

        orders_list.setAdapter(new OrderList_Adapter(data2));

        setPrice();
//        data.notify();

        orders_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((DashbordActivity)getActivity()).replace_Fragment(new Order_Details());
            }
        });

        return view;
    }

    private void setPrice(){
        net_price = 0;
        for(int i=0;i<data2.size();i++){
            net_price += data2.get(i).getPrice();
        }
        final_price.setText(getString(R.string.Rs)+" "+net_price);
    }

    private class OrderList_Adapter  extends BaseAdapter {

        ArrayList<My_Cart_Model> data;

        public OrderList_Adapter(ArrayList<My_Cart_Model> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            final int position = i;
            if(view == null){

                view = LayoutInflater.from(getActivity()).inflate(R.layout.order_list_item,viewGroup,false);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,qty_list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ImageView imageView = view.findViewById(R.id.cart_order_image);
            final TextView cart_order_name = view.findViewById(R.id.cart_order_name);
            TextView cart_order_price = view.findViewById(R.id.cart_order_price);
            TextView vendor_name = view.findViewById(R.id.vendor_name);
            final Spinner qty_count = (Spinner) view.findViewById(R.id.qty_count);
            ImageView remove_item = (ImageView) view.findViewById(R.id.remove_item);

            qty_count.setAdapter(arrayAdapter);
            qty_count.setSelection(0);

            qty_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position_n, long id) {
                    qty.add(position,qty_list.get(position_n));
                    Log.e("QTY",data.get(position).getAccessory_name()+" : "+qty.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

                Picasso.get().load(BaseClass.BaseUrl + data.get(i).getImage()).placeholder(R.drawable.no_image_available).into(imageView);

            cart_order_name.setText(data.get(i).getAccessory_name());
            cart_order_price.setText(getString(R.string.Rs)+" "+data.get(i).getPrice());
            vendor_name.setText(data.get(i).getVendor_name());

            remove_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    session.setCartData(createJSON_onRemove());
                    notifyDataSetChanged();
                    setPrice();
                }
            });



            return view;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.my_cart_main));

    }

    public static String createJSON(){

        JSONArray jsonArray = new JSONArray();

        for(int i=0;i<data.size();i++){
            JSONObject jsonObject = new JSONObject();
            try {
                if (data.get(i).getImages().length > 0) {
                    jsonObject.put("cart_image", data.get(i).getImages()[0]);
                }else{
                    jsonObject.put("cart_image","");
                }
                jsonObject.put("accessory_name",data.get(i).getAccessory_name());
                jsonObject.put("vendor_name",data.get(i).getVendor_name());
                jsonObject.put("price",data.get(i).getPrice());
                jsonObject.put("product_id",data.get(i).getAccessory_id());
                jsonObject.put("vendor_id",data.get(i).getVendor_id());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        Log.e("JSON_CART",jsonArray.toString());

        return jsonArray.toString();
    }


    private String createJSON_onRemove(){

        JSONArray jsonArray = new JSONArray();

        for(int i=0;i<data2.size();i++){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cart_image", data2.get(i).getImage());
                jsonObject.put("accessory_name",data2.get(i).getAccessory_name());
                jsonObject.put("vendor_name",data2.get(i).getVendor_name());
                jsonObject.put("price",data2.get(i).getPrice());
                jsonObject.put("product_id",data2.get(i).getAccessory_id());
                jsonObject.put("vendor_id",data2.get(i).getVendor_id());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        Log.e("JSON_CART",jsonArray.toString());

        return jsonArray.toString();
    }

    private void readFromSession(){
        data2.clear();
        JSONArray jsonArray =  session.getCartData();
        Log.e("SESSION_DATA",jsonArray.toString());
        for (int i=0;i<jsonArray.length();i++){
            try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);


                String image = jsonObject.getString("cart_image");
                String name = jsonObject.getString("accessory_name");
                String vendor = jsonObject.getString("vendor_name");
                int price = jsonObject.getInt("price");
                int accessory_id = jsonObject.getInt("product_id");
                int vendor_id = jsonObject.getInt("vendor_id");

                data2.add(new My_Cart_Model(image,name,vendor,price,accessory_id,vendor_id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public ArrayList<My_Cart_Model> getCartData(){

        return data2;
    }


}
