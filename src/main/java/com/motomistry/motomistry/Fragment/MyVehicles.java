package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.ModelMyVehical;
import com.motomistry.motomistry.New.Fragment.FragmentBookService;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyVehicles extends Fragment {

    private View view;
    TextView AddVehicle;
    private ConnectionManager connectionManager;
    String BOOKING;
    ArrayList<ModelMyVehical> data;
    private ListView vehical_list;
    private ProgressBar loding;
    String TAG="My_VEHICAL";
    private RequestQueue requestQueue;
    private String USER_ID;
    private Session session;

    @SuppressLint("ValidFragment")
    public MyVehicles(String BOOKING) {
        this.BOOKING = BOOKING;
    }
    public MyVehicles() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_vehicles, container, false);

        connectionManager = new ConnectionManager(getActivity());
        AddVehicle = (TextView) view.findViewById(R.id.add_vehicle);
        vehical_list = (ListView) view.findViewById(R.id.vehicles_list);
        loding = (ProgressBar) view.findViewById(R.id.loading);
        data = new ArrayList<ModelMyVehical>();
        GetRecordFromSession();
        AddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionManager.isConnected())
                ((DashbordActivity)getActivity()).replace_Fragment(new AddVehicle());
                else {
                    final Snackbar snackbar = Snackbar.make(view,"No Internet Connection!", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        });

        vehical_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BaseClass.MY_VEHICAL_NAME=data.get(i).getModel_name();
                BaseClass.MY_VEHICAL_ID=data.get(i).getId();
                ((DashbordActivity)getActivity()).replace_Fragment(new FragmentBookService());
            }
        });
        GetResult(BaseClass.getGet_vehicle_details_according_to_user_URL(USER_ID));

        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
        }


        return view;
    }
    private class CustomAdapter extends ArrayAdapter<ModelMyVehical> {

        Context context;
        public CustomAdapter(@NonNull Context context, @NonNull List objects) {
            super(context,0, objects);
            this.context = getActivity();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.vehicle_list,parent,false);
            }
            ModelMyVehical current = getItem(position);
            TextView shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            TextView shop_locality = (TextView) convertView.findViewById(R.id.shop_locality);
            RatingBar rating = (RatingBar) convertView.findViewById(R.id.rating);
            rating.setVisibility(View.GONE);
            shop_name.setTextSize(20);
            shop_name.setText(current.getModel_name());
            shop_locality.setText(current.getRCNo());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.shop_image);
            if (current.getVehicle_type()==1) {
                imageView.setImageResource(R.drawable.car);
            }else {
                imageView.setImageResource(R.drawable.motorbike);
            }
            return convertView;
        }
    }

    public void ReadJson(JSONObject jsonObject) {
        data.clear();
        try {
            JSONArray jsonArray =jsonObject.getJSONArray("Result");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object= jsonArray.getJSONObject(i);
                int id=object.getInt("vehicle_id");
                int vehicle_type=object.getInt("vehicle_type");
                String Model_name=object.getString("Model_name");
                String manufacturer_name=object.getString("manufacturer_name");
                String RCNo=object.getString("RCNo");
                data.add(new ModelMyVehical(Model_name,manufacturer_name,RCNo,vehicle_type,id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(getActivity()!=null)
        vehical_list.setAdapter(new CustomAdapter(getActivity(),data));
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
    private void GetRecordFromSession(){
        session=new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.parent));

    }
}
