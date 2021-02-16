package com.motomistry.motomistry.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Activity.MapActivity;
import com.motomistry.motomistry.Adapter.Modellist_Adapter;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.ModelInsurance;
import com.motomistry.motomistry.Model.Modellist;
import com.motomistry.motomistry.Model.ShopListModel;
import com.motomistry.motomistry.New.Fragment.Inusrance_details;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class inusrance extends Fragment {

    private View view;
    private ListView insurance_list;
    ArrayList<ModelInsurance> data;
private String TAG="inusrance";
    private ProgressBar loding;
    private RequestQueue requestQueue;
    private TextView toolbar_title;
    ImageView back_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_inusrance,container,false);

        insurance_list = (ListView) view.findViewById(R.id.insurance_list);
        loding = (ProgressBar) view.findViewById(R.id.loading);
        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        data = new ArrayList<ModelInsurance>();
        back_button = (ImageView) view.findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).bacPOP();
            }
        });

        insurance_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((DashbordActivity)getActivity()).replace_Fragment(new Inusrance_details(data.get(i).getID(),data.get(i).getRating()));
            }
        });
       GetResult(BaseClass.BaseUrl+"insurance_companey");


        return view;
    }

    private class CustomAdapter extends ArrayAdapter<ModelInsurance> {

        Context context;
        public CustomAdapter(@NonNull Context context, @NonNull List objects) {
            super(context,0, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.select_insurance_list_layout,parent,false);
            }
            ModelInsurance current = getItem(position);
            TextView shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            shop_name.setTextSize(20);
            shop_name.setText(current.getLIST_NAME());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.shop_image);
            imageView.setImageResource(R.drawable.insurance);
            TextView locality = (TextView) convertView.findViewById(R.id.shop_locality);
            locality.setText(current.getLocality());
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.insurance_rating);

            if(data.get(position).getRating()!= 0.0){
                ratingBar.setRating((float)data.get(position).getRating());
            }

            return convertView;
        }
    }

    public void ReadJson(JSONObject jsonObject) {
        data.clear();
        try {
            JSONArray jsonArray =jsonObject.getJSONArray("Result");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object1= jsonArray.getJSONObject(i);
                JSONObject object = object1.getJSONObject("company");
                int id=object.getInt("id");
                String city=object.getString("city");
                String insur_name=object.getString("LIST_NAME");
                String locality = object.getString("locality");
                JSONArray retting = object1.getJSONArray("Rating");
                String rating = retting.getJSONObject(0).getString("retting");
                Float double_rating= 0f;
                if(!(rating.equals("null"))){
                    double_rating = Float.valueOf(rating);
                }
                data.add(new ModelInsurance(id,city,insur_name,locality,double_rating));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        insurance_list.setAdapter(new CustomAdapter(getActivity(),data));
    }
    public void GetResult(String mURL) {
        loding.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                loding.setVisibility(View.GONE);
                                if (response.toString() != null) {
                                    ReadJson(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loding.setVisibility(View.GONE);
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
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.insurance_main));

    }
}
