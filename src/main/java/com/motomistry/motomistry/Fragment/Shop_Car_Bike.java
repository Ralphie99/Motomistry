package com.motomistry.motomistry.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Model_Shop_Acessory;
import com.motomistry.motomistry.OtherClass.Circular_reveal;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shop_Car_Bike extends Fragment {

    private View view;
    private ListView listView;
    private ArrayList<Model_Shop_Acessory> data;
    private boolean isCar = false;
    private CustomDialog progressDialog;
    Cache.Entry cacheEntry;
    TextView shop_head;
    private RequestQueue requestQueue;
    ImageView my_cart,toolbar_back;

    @Override
    public void onStart() {
        super.onStart();
        cacheEntry = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_shop__car__bike, container, false);

        my_cart = (ImageView) view.findViewById(R.id.my_cart);
        toolbar_back = (ImageView) view.findViewById(R.id.toolbar_back);

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

        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setDuration(500);
        setExitTransition(slide);

        Circular_reveal.registerCircularRevealAnimation(getActivity(), view, getArguments().getDouble("circle_center_x"),
                getArguments().getDouble("circle_center_y"),
                getArguments().getFloat("container_width"),
                getArguments().getFloat("container_height"),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.White));



        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);

        listView = (ListView) view.findViewById(R.id.acessories_list);
        shop_head = (TextView) view.findViewById(R.id.shop_head);
        data = new ArrayList<Model_Shop_Acessory>();

//        bike_data.add(new Model_Shop_Acessory("Boots & Luggage"));
//        bike_data.add(new Model_Shop_Acessory("Drive & Gears"));
//        bike_data.add(new Model_Shop_Acessory("Engines & Engine Parts"));
//        bike_data.add(new Model_Shop_Acessory("Exhaust & Exhaust Systems"));
//        bike_data.add(new Model_Shop_Acessory("Filters"));
//        bike_data.add(new Model_Shop_Acessory("Frames & Fittings"));
//        bike_data.add(new Model_Shop_Acessory("Helmets"));
//        bike_data.add(new Model_Shop_Acessory("Horns"));
//        bike_data.add(new Model_Shop_Acessory("Lighting"));
//        bike_data.add(new Model_Shop_Acessory("Protective Gear & Clothing"));
//        bike_data.add(new Model_Shop_Acessory("Seat Covers"));
//        bike_data.add(new Model_Shop_Acessory("Tyres & Rims"));
//
//        car_data.add(new Model_Shop_Acessory("Air Fresheners"));
//        car_data.add(new Model_Shop_Acessory("Air Purifiers & Ionizers"));
//        car_data.add(new Model_Shop_Acessory("Antitheft Locking Devices"));
//        car_data.add(new Model_Shop_Acessory("Dashboard Figurines & Idols"));
//        car_data.add(new Model_Shop_Acessory("Breakdown Assistance"));
//        car_data.add(new Model_Shop_Acessory("Power Inverters"));
//        car_data.add(new Model_Shop_Acessory("Car Covers"));
//        car_data.add(new Model_Shop_Acessory("Key Shells"));
//        car_data.add(new Model_Shop_Acessory("Mats & Carpets"));
//        car_data.add(new Model_Shop_Acessory("Tyre Inflators"));
//        car_data.add(new Model_Shop_Acessory("Wind Deflectors"));
//        car_data.add(new Model_Shop_Acessory("Steering Wheel Covers"));
//        car_data.add(new Model_Shop_Acessory("Sun Shades"));
//        car_data.add(new Model_Shop_Acessory("Wraps, Decals & Stickers"));

        if (Constant.isBike) {
            GetAccessories(BaseClass.BaseUrl+"get_product_catogory?Vehicle_type="+"0");

        } else if (Constant.isCar) {
            GetAccessories(BaseClass.BaseUrl+"get_product_catogory?Vehicle_type="+"1");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putInt("Category_ID",data.get(position).getId());
                Constant.Category_id = data.get(position).getId();
                accessories_items_list fragment = new accessories_items_list(data.get(position).getText());
                fragment.setArguments(bundle);
                Explode explode = new Explode();
                explode.setDuration(500);
                fragment.setEnterTransition(explode);
                ((DashbordActivity)getActivity()).replace_Fragment(fragment);
            }
        });




        return view;
    }


    private class Shop_list_adapter extends BaseAdapter{

        ArrayList<Model_Shop_Acessory> data;
        private Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);

        public Shop_list_adapter(ArrayList<Model_Shop_Acessory> data) {
            this.data = data;
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.shop_accessory_item,parent,false);
            }

            Model_Shop_Acessory currentItem = data.get(position);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.acessory_image);
            TextView textView = (TextView) convertView.findViewById(R.id.acessory_name);

            Picasso.get().load(BaseClass.BaseUrl+currentItem.getImage()).placeholder(R.drawable.gears).into(imageView);
            textView.setText(currentItem.getText());
            textView.setTypeface(iconFont);

            return convertView;
        }
    }

    public void GetAccessories(String mURL) {
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

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        try {
                            cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                            if (cacheEntry == null) {
                                cacheEntry = new Cache.Entry();
                            }
                            final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                            final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                            long now = System.currentTimeMillis();
                            final long softExpire = now + cacheHitButRefreshed;
                            final long ttl = now + cacheExpired;
                            cacheEntry.data = response.data;
                            cacheEntry.softTtl = softExpire;
                            cacheEntry.ttl = ttl;
                            String headerValue;
                            headerValue = response.headers.get("Date");
                            if (headerValue != null) {
                                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                            }
                            headerValue = response.headers.get("Last-Modified");
                            if (headerValue != null) {
                                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                            }
                            cacheEntry.responseHeaders = response.headers;
                            final String jsonString = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            return Response.success(new JSONObject(jsonString), cacheEntry);
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException e) {
                            return Response.error(new ParseError(e));
                        }
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
            Log.e("DATA",json_String.toString());
            data.clear();

            try {

                JSONArray jsonArray = json_String.getJSONArray("product_details");

                for(int i=0;i<jsonArray.length();i++){


                    JSONObject currentObject = jsonArray.getJSONObject(i);
                    int id = Integer.parseInt(currentObject.getString("id"));
                    String category_name = currentObject.getString("part_category_name");
                    int vehicle_type = currentObject.getInt("vehicle_type");
                    String image_path = currentObject.getString("cat_image");

                       data.add(new Model_Shop_Acessory(id,image_path,category_name));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
                progressDialog.dismiss();
                listView.setAdapter(new Shop_list_adapter(data));

        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.shop_car_bike_main));

    }

}
