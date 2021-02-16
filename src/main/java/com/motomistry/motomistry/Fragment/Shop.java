package com.motomistry.motomistry.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Accessory_item_model;
import com.motomistry.motomistry.Model.Accessory_item_model_2;
import com.motomistry.motomistry.Model.Accessory_item_model_frequent;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.RoundedTransformation;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.motomistry.motomistry.View.CurveImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Shop extends Fragment {

    private View view,reveal_view;
    private LinearLayout bike,car;
    private ConnectionManager connectionManager;
    private View container_view;
    private CustomDialog progressDialog;
    private RequestQueue requestQueue;
    ArrayList<Accessory_item_model_frequent> data;
    ListView frequent_list;
    ImageView my_cart,toolbar_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((DashbordActivity)getActivity()).selectIcon(2);

        view = inflater.inflate(R.layout.fragment_shop,container,false);

        my_cart = (ImageView) view.findViewById(R.id.my_cart);
        toolbar_back = (ImageView) view.findViewById(R.id.toolbar_back);

        my_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new My_Cart());
            }
        });
        toolbar_back.setVisibility(View.INVISIBLE);

        reveal_view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shop__car__bike,container,false);
        frequent_list = (ListView) view.findViewById(R.id.frequent_list);

        data = new ArrayList<Accessory_item_model_frequent>();

        bike = (LinearLayout) view.findViewById(R.id.bike);
        car = (LinearLayout) view.findViewById(R.id.car);
        container_view = getActivity().findViewById(R.id.container_view);

        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);

        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constant.isFromFrequent = false;

                Constant.isBike = true;
                Constant.isCar = false;
                int coordinates[] = new int[2];
                bike.getLocationOnScreen(coordinates);
                Shop_Car_Bike shop_car_bike = new Shop_Car_Bike();
                Bundle bundle = new Bundle();
                bundle.putDouble("circle_center_x",320);
                bundle.putDouble("circle_center_y",450);
                bundle.putFloat("container_width",container_view.getWidth());
                bundle.putFloat("container_height",container_view.getHeight());
                shop_car_bike.setArguments(bundle);
                ((DashbordActivity)getActivity()).replace_Fragment(shop_car_bike);
            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constant.isFromFrequent = false;

                Constant.isCar = true;
                Constant.isBike = false;
                int coordinates[] = new int[2];
                car.getLocationOnScreen(coordinates);
                Shop_Car_Bike shop_car_bike = new Shop_Car_Bike();
                Bundle bundle = new Bundle();
                bundle.putDouble("circle_center_x",320);
                bundle.putDouble("circle_center_y",450);
                bundle.putFloat("container_width",container_view.getWidth());
                bundle.putFloat("container_height",container_view.getHeight());
                shop_car_bike.setArguments(bundle);
                ((DashbordActivity)getActivity()).replace_Fragment(shop_car_bike);
            }
        });

        connectionManager = new ConnectionManager(getActivity());

        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
        }

        GetFrequent_Items(BaseClass.BaseUrl+"get_frequent_products");

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.shop_main));

    }

    private class Frequent_Adpater extends BaseAdapter {

        ArrayList<Accessory_item_model_frequent> data;


        public Frequent_Adpater(ArrayList<Accessory_item_model_frequent> data) {
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


            if(view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.frequent_list_item,viewGroup,false);
            }

            Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.null_tofade);
            animation.setDuration(500);
            view.setAnimation(animation);

            ImageView accessory_image1,accessory_image2,accessory_image3,accessory_image4;
            TextView accessory_name1,accessory_price1,accessory_name2,accessory_price2,accessory_name3,accessory_price3,accessory_name4,accessory_price4;
            CardView card1,card2,card3,card4;

            accessory_image1 = (ImageView) view.findViewById(R.id.accessory_image1);
            accessory_name1 = (TextView) view.findViewById(R.id.accessory_name1);
            accessory_price1 = (TextView) view.findViewById(R.id.accessory_price1);
            accessory_image2= (ImageView) view.findViewById(R.id.accessory_image2);
            accessory_name2 = (TextView) view.findViewById(R.id.accessory_name2);
            accessory_price2 = (TextView) view.findViewById(R.id.accessory_price2);
            accessory_image3 = (ImageView) view.findViewById(R.id.accessory_image3);
            accessory_name3 = (TextView) view.findViewById(R.id.accessory_name3);
            accessory_price3 = (TextView) view.findViewById(R.id.accessory_price3);
            accessory_image4 = (ImageView) view.findViewById(R.id.accessory_image4);
            accessory_name4 = (TextView) view.findViewById(R.id.accessory_name4);
            accessory_price4= (TextView) view.findViewById(R.id.accessory_price4);
            card1 = (CardView) view.findViewById(R.id.card1);
            card2 = (CardView) view.findViewById(R.id.card2);
            card3 = (CardView) view.findViewById(R.id.card3);
            card4 = (CardView) view.findViewById(R.id.card4);

            final Accessory_item_model_frequent current_item = data.get(i);

                if (current_item.getImages(0).length > 0)
                    Picasso.get().load(BaseClass.BaseUrl + current_item.getImages(0)[0]).placeholder(R.drawable.dummy_rectangle_image).into(accessory_image1);

                accessory_name1.setText(current_item.getAccessory_name(0));
                accessory_price1.setText(getString(R.string.Rs) + " " + current_item.getPrice(0));

                if (current_item.getImages(1).length > 0)
                    Picasso.get().load(BaseClass.BaseUrl + current_item.getImages(1)[0]).placeholder(R.drawable.dummy_rectangle_image).into(accessory_image2);

                accessory_name2.setText(current_item.getAccessory_name(1));
                accessory_price2.setText(getString(R.string.Rs) + " " + current_item.getPrice(1));

                if (current_item.getImages(2).length > 0)
                    Picasso.get().load(BaseClass.BaseUrl + current_item.getImages(2)[0]).placeholder(R.drawable.dummy_rectangle_image).into(accessory_image3);

                accessory_name3.setText(current_item.getAccessory_name(2));
                accessory_price3.setText(getString(R.string.Rs) + " " + current_item.getPrice(2));

                if (current_item.getImages(3).length > 0)
                    Picasso.get().load(BaseClass.BaseUrl + current_item.getImages(3)[0]).placeholder(R.drawable.dummy_rectangle_image).into(accessory_image4);

                accessory_name4.setText(current_item.getAccessory_name(3));
                accessory_price4.setText(getString(R.string.Rs) + " " + current_item.getPrice(3));


                card1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constant.isFromFrequent = true;
                        ((DashbordActivity)getActivity()).replace_Fragment(new Accessory(current_item.getItem(0)));
                    }
                });

                card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.isFromFrequent = true;
                    ((DashbordActivity)getActivity()).replace_Fragment(new Accessory(current_item.getItem(1)));
                }
                });

                card3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.isFromFrequent = true;
                    ((DashbordActivity)getActivity()).replace_Fragment(new Accessory(current_item.getItem(2)));
                }
                });

                card4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.isFromFrequent = true;
                    ((DashbordActivity)getActivity()).replace_Fragment(new Accessory(current_item.getItem(3)));
                }
                });


            return view;
        }
    }

    public void GetFrequent_Items(String mURL) {
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                progressDialog.dismiss();
                                ReadJson(response);


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        return super.getHeaders();
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }

                };
        addToRequestQueue(jsonObjectRequest);
    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }



    private void ReadJson(JSONObject json_String) {
        Log.e("FREQUENT_DATA",json_String.toString());

        try {

            JSONArray jsonArray = json_String.getJSONArray("product_details");

            for(int i=0;i<jsonArray.length();i+=4){

//                Accessory_item_model_2 item = new Accessory_item_model_2();
                Accessory_item_model_frequent item = new Accessory_item_model_frequent();

                for(int p=i;p<i+4;p++) {

                    JSONObject currentObject = jsonArray.getJSONObject(p);
                    int id = Integer.parseInt(currentObject.getString("product_id"));
                    String product_name = currentObject.getString("AName");
                    int vehicle_type = currentObject.getInt("vehicle_Type");
                    String description = currentObject.getString("description");
                    int brand_id = currentObject.getInt("brand_id");
                    JSONArray images = currentObject.getJSONArray("images");
                    int images_length = images.length();
                    String image_paths[] = new String[images_length];
                    if (images_length > 0) {
                        for (int j = 0; j < images_length; j++) {
                            image_paths[j] = images.getJSONObject(j).getString("image_path");
                        }
                    }

                    int price = currentObject.getInt("price");
                    int vendor_id = currentObject.getInt("vendor_id");
                    String vendor_name = currentObject.getString("vendor_name");
                    int stock = currentObject.getInt("stock");

                    switch (p % 4) {
                        case 0:
                            item.setData(0, image_paths, product_name, price, description, id, vehicle_type, brand_id, vendor_id, vendor_name, stock);
                            break;
                        case 1:
                            item.setData(1, image_paths, product_name, price, description, id, vehicle_type, brand_id, vendor_id, vendor_name, stock);
                            break;
                        case 2:
                            item.setData(2, image_paths, product_name, price, description, id, vehicle_type, brand_id, vendor_id, vendor_name, stock);
                            break;
                        case 3:
                            item.setData(3, image_paths, product_name, price, description, id, vehicle_type, brand_id, vendor_id, vendor_name, stock);
                            break;

                        default:
                            break;
                    }

                }

                data.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        frequent_list.setAdapter(new Frequent_Adpater(data));
    }
}
