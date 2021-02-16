package com.motomistry.motomistry.New.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.New.Model.ModelInsurenceDetails;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("ValidFragment")
public class DrivingSchool_details extends Fragment {

    private View view;
    private ListView insurance_list;
    ArrayList<ModelInsurenceDetails> data;
    private String TAG = "inusrance_details";
    private ProgressBar loding;
    private RequestQueue requestQueue;
    private int COMPNY_ID;
    private TextView toolbar_title,no_record_tv;

    @SuppressLint("ValidFragment")
    public DrivingSchool_details(int COMPNY_ID) {
        this.COMPNY_ID = COMPNY_ID;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_inusrance, container, false);

        insurance_list = (ListView) view.findViewById(R.id.insurance_list);
        loding = (ProgressBar) view.findViewById(R.id.loading);
        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        no_record_tv = (TextView) view.findViewById(R.id.no_record_tv);
        data = new ArrayList<ModelInsurenceDetails>();
        insurance_list.setAdapter(new CustomAdapter(getActivity(), data));

        insurance_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        GetResult(BaseClass.getGetDrivingSchoolDetailsUrl(COMPNY_ID));


        return view;
    }

    private class CustomAdapter extends ArrayAdapter<ModelInsurenceDetails> {

        Context context;

        public CustomAdapter(@NonNull Context context, @NonNull List objects) {
            super(context, 0, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.driving_school_list_details, parent, false);
            }
            ModelInsurenceDetails current = getItem(position);
            TextView shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            TextView shop_locality = (TextView) convertView.findViewById(R.id.shop_locality);
            shop_name.setTextSize(20);
            shop_name.setText(current.getLIST_NAME());
            toolbar_title.setText(current.getLIST_NAME());
            shop_locality.setText(current.getContact_person()+", \n"+current.getMobile()+", \t"+current.getEmail()+", \n"+current.getCity());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.shop_image);
            imageView.setImageResource(R.drawable.insurance);

            return convertView;
        }
    }

        public void ReadJson(JSONObject jsonObject) {
        data.clear();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("Result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                int id = object.getInt("id");
                String vendor_name = object.getString("vendor_name");
                String contact_person = object.getString("contact_person");
                String city = object.getString("city");
                String email = object.getString("email");
                String mobile = object.getString("mobile");
                String LIST_NAME = object.getString("LIST_NAME");
                data.add(new ModelInsurenceDetails(id, vendor_name,contact_person,city,email,mobile, LIST_NAME));
            }
            insurance_list.setAdapter(new CustomAdapter(getActivity(), data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
            if (data.size()>0){
                no_record_tv.setVisibility(View.GONE);
            }else {
                Log.e("data", String.valueOf(data.size()));
                no_record_tv.setVisibility(View.VISIBLE);
            }
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
                                no_record_tv.setVisibility(View.VISIBLE);
                                if (response.toString() != null) {
                                    ReadJson(response);
                                    no_record_tv.setVisibility(View.GONE);
                                }else {
                                    no_record_tv.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loding.setVisibility(View.VISIBLE);
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
