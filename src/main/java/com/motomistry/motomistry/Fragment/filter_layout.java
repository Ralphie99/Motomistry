package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.motomistry.motomistry.Model.Filter_Models;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class filter_layout extends Fragment {

    private View view;
    LinearLayout brand,model;

    private RequestQueue requestQueue;
    private CustomDialog progressDialog;
    ArrayList<Filter_Models> data;
    ArrayList<Filter_Models> data2;
    ListView brand_list;

    StringBuilder s_model_checked_ids,s_brand_checked_ids;
    int v_type;
    TextView brand_count,model_count,clear;;
    LinearLayout done;
    accessories_items_list Parent;
    ImageView shown_filter,shown_filter2;
    CustomAdapter customAdapter;
    public static ArrayList<Integer> brand_checked_ids;
    public static ArrayList<Integer> model_checked_ids;

    public filter_layout() {
    }

    @SuppressLint("ValidFragment")
    public filter_layout(accessories_items_list accessories_items_list) {
        Parent = accessories_items_list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.filter_layout,container,false);

        brand = (LinearLayout) view.findViewById(R.id.brand_linear);
        model = (LinearLayout) view.findViewById(R.id.model_linear);
        brand_checked_ids = new ArrayList<>();
        model_checked_ids = new ArrayList<>();
        data = new ArrayList<>();
        data2 = new ArrayList<>();



        brand_list = (ListView) view.findViewById(R.id.list);
        brand_count = (TextView) view.findViewById(R.id.brand_count);
        model_count = (TextView) view.findViewById(R.id.model_count);
        done = (LinearLayout) view.findViewById(R.id.done);
        clear = (TextView) view.findViewById(R.id.clear);
        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);
        v_type = Constant.isBike?0:1;
        shown_filter = (ImageView) view.findViewById(R.id.shown_filter);
        shown_filter2 = (ImageView) view.findViewById(R.id.shown_filter2);

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shown_filter.setVisibility(View.VISIBLE);
                shown_filter2.setVisibility(View.INVISIBLE);
                brand.setBackgroundColor(getResources().getColor(R.color.GrayLight));
                model.setBackgroundColor(getResources().getColor(R.color.White));
                if(!(data.size()>0))
                GetBrand(BaseClass.BaseUrl+"get_brand_acording_to_catgory?vehicle_type="+v_type+"&cat_id="+Constant.Category_id,1);
                else {
                    customAdapter = new CustomAdapter(getActivity(),data,1);
                            brand_list.setAdapter(customAdapter);
                }
            }
        });

        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shown_filter.setVisibility(View.INVISIBLE);
                shown_filter2.setVisibility(View.VISIBLE);
                model.setBackgroundColor(getResources().getColor(R.color.GrayLight));
                brand.setBackgroundColor(getResources().getColor(R.color.White));
                if(!(data2.size()>0))
                GetBrand(BaseClass.BaseUrl+"get_model_according_to_vehicle_type?vehicle_type="+v_type,2);
                else {
                    customAdapter = new CustomAdapter(getActivity(),data2,2);
                    brand_list.setAdapter(customAdapter);
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_brand_checked_ids = new StringBuilder();
                s_model_checked_ids = new StringBuilder();
                if(model_checked_ids.size()>0){
                    for(int i=0;i<model_checked_ids.size();i++){
                        if(i!=(model_checked_ids.size()-1))
                        s_model_checked_ids.append(String.valueOf(model_checked_ids.get(i))+",");
                        else
                            s_model_checked_ids.append(String.valueOf(model_checked_ids.get(i)));
                    }
                }
                if(brand_checked_ids.size()>0){
                    for(int i=0;i<brand_checked_ids.size();i++){
                        if(i!=(brand_checked_ids.size()-1))
                            s_brand_checked_ids.append(String.valueOf(brand_checked_ids.get(i))+",");
                        else
                            s_brand_checked_ids.append(String.valueOf(brand_checked_ids.get(i)));
                    }
                }
                String brand_URL="";
                String model_URL="";
                if(s_brand_checked_ids.length()>0)
                     brand_URL = BaseClass.BaseUrl + "get_prioduct_according_to_brand_id?brand_id=" + s_brand_checked_ids + "&cat_id=" + Constant.Category_id;

                if ((s_model_checked_ids.length()>0))
                    model_URL = BaseClass.BaseUrl+"get_prioduct_according_to_vehicle_model_id?vehicle_model_id="+s_model_checked_ids+"&cat_id="+Constant.Category_id;

                Log.e("BRAND_URL",brand_URL);
                Log.e("MODEL_URL",model_URL);
                Parent.setURL(brand_URL,model_URL);
                ((DashbordActivity)getActivity()).bacPOP();

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brand_checked_ids.size()>0)
                    brand_checked_ids.clear();
                if(model_checked_ids.size()>0)
                model_checked_ids.clear();

                for(int i=0;i<data.size();i++)
                    data.get(i).setChecked(false);

                for(int i=0;i<data2.size();i++)
                    data2.get(i).setChecked(false);

                customAdapter.notifyDataSetChanged();

                Constant.isFiltered = false;
            }
        });

        brand_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
                checkBox.setChecked(true);
            }
        });

        shown_filter.setVisibility(View.VISIBLE);
        shown_filter2.setVisibility(View.INVISIBLE);
        brand.setBackgroundColor(getResources().getColor(R.color.GrayLight));
        model.setBackgroundColor(getResources().getColor(R.color.White));
        if(!(data.size()>0))
            GetBrand(BaseClass.BaseUrl+"get_brand_acording_to_catgory?vehicle_type="+v_type+"&cat_id="+Constant.Category_id,1);


        return view;
    }

    private class CustomAdapter extends ArrayAdapter<Filter_Models> {

        Context context;
        int flag;
        Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);

        public CustomAdapter(@NonNull Context context, @NonNull List objects,int flag) {
            super(context, 0, objects);
            this.context = context;
            this.flag = flag;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.filter_list_layout, parent, false);
            }

            final Filter_Models current = getItem(position);
            TextView Brand_name = (TextView) convertView.findViewById(R.id.name);
            Brand_name.setTypeface(iconFont);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check);
            checkBox.setChecked(current.isChecked());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        if(flag==1) {
                            brand_checked_ids.add(data.get(position).getId());
                            data.get(position).setChecked(true);
                            brand_count.setVisibility(View.VISIBLE);
                            brand_count.setText("+" + String.valueOf(brand_checked_ids.size()));
                        }
                        else {
                            model_checked_ids.add(data2.get(position).getId());
                            data2.get(position).setChecked(true);
                            model_count.setVisibility(View.VISIBLE);
                            model_count.setText("+"+String.valueOf(model_checked_ids.size()));
                        }

                    }
                    else{
                        if(flag==1) {
                            brand_checked_ids.remove((Object)data.get(position).getId());
                            data.get(position).setChecked(false);
                            if(brand_checked_ids.size()>0) {
                                brand_count.setVisibility(View.VISIBLE);
                                brand_count.setText("+" + String.valueOf(brand_checked_ids.size()));
                            }else{
                                brand_count.setVisibility(View.INVISIBLE);
                            }
                            brand_count.setText("+"+String.valueOf(brand_checked_ids.size()));
                        }
                        else {
                            model_checked_ids.remove((Object)data2.get(position).getId());
                            data2.get(position).setChecked(false);
                            if(brand_checked_ids.size()>0) {
                                model_count.setVisibility(View.VISIBLE);
                                model_count.setText("+"+String.valueOf(model_checked_ids.size()));
                            }else{
                                model_count.setVisibility(View.INVISIBLE);
                            }
                            model_count.setText("+"+String.valueOf(model_checked_ids.size()));
                        }
                    }
                }
            });
            Brand_name.setText(current.getName());

            return convertView;
        }

    }

    public void GetBrand(String mURL, final int flag) {
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                progressDialog.dismiss();

                                if(flag==1)
                                ReadJson(response);
                                else
                                    ReadJson2(response);

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
                            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
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
        Log.e("ADDRESS_DATA", json_String.toString());
        data.clear();

        try {

            JSONArray jsonArray = json_String.getJSONArray("Brand_details");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentJSONObject = jsonArray.getJSONObject(i);
                int brand_id = currentJSONObject.getInt("brand_id");
                String brand_name = currentJSONObject.getString("brand_name");

                data.add(new Filter_Models(brand_id,brand_name));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        if(getActivity()!=null) {
            customAdapter = new CustomAdapter(getActivity(),data,1);
            brand_list.setAdapter(customAdapter);
        }
    }

    private void ReadJson2(JSONObject json_String) {
        Log.e("ADDRESS_DATA", json_String.toString());
        data2.clear();

        try {

            JSONArray jsonArray = json_String.getJSONArray("Model_details");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentJSONObject = jsonArray.getJSONObject(i);
                int brand_id = currentJSONObject.getInt("id");
                String brand_name = currentJSONObject.getString("Model_name");

                data2.add(new Filter_Models(brand_id,brand_name));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        if(getActivity()!=null) {
            customAdapter = new CustomAdapter(getActivity(),data2,2);
            brand_list.setAdapter(customAdapter);
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.filter_main));

    }
}
