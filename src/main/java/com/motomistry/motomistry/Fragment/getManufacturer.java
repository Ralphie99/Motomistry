package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Adapter.Modellist_Adapter;
import com.motomistry.motomistry.Fatch.Fatch;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Modellist;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.RegistrationActivity;
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
import java.util.Map;


public class  getManufacturer extends Fragment {
    String TAG="Manufacturer";
    private View view;
    ListView manufacturer_list;
    ImageView list_image;
    ArrayList<Modellist> data = new ArrayList<Modellist>();
    private Modellist modellist;
    CustomDialog alertDialog;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private ProgressBar progressBar;
    AddVehicle parent1;

    public getManufacturer() {
    }

    @SuppressLint("ValidFragment")
    public getManufacturer(AddVehicle addVehicle) {

        parent1 = addVehicle;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_get_manufacturer,container,false);

        alertDialog = new CustomDialog(getActivity());
        manufacturer_list = (ListView) view.findViewById(R.id.manufacturer_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        GetResult(Constant.getVehiclesTypeUrl());
        if(Constant.fromOutside){
            manufacturer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getModel replace_fragment = new getModel();
                    Slide slide = new Slide(Gravity.RIGHT);
                    slide.setDuration(900);
                    Slide slide2 = new Slide(Gravity.LEFT);
                    slide.setDuration(900);
                    replace_fragment.setEnterTransition(slide);
                    replace_fragment.setExitTransition(slide2);
                    BaseClass.M_ID=data.get(position).getId();
                    ((AddVehicle_fromOutside)getActivity()).setBackground_null();
                    ((AddVehicle_fromOutside)getActivity()).replace_Fragment(replace_fragment);
                }
            });
        }
        else{
            manufacturer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    parent1.setManufacturer(data.get(position).getName());
                    BaseClass.M_ID=data.get(position).getId();
                    getFragmentManager().popBackStack();
                }
            });
        }

        return view;
    }
        public void ReadJson(  JSONObject jsonObject) {
            data.clear();
            try {
                JSONArray jsonArray =jsonObject.getJSONArray("response");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object= jsonArray.getJSONObject(i);
                    int id=object.getInt("id");
                    String manuf_name=object.getString("manufacturer_name");
                    String vehicle_type=object.getString("vehicle_type");
                        modellist=new Modellist(id,manuf_name,vehicle_type);
                        data.add(modellist);
                }
                manufacturer_list.setAdapter(new Modellist_Adapter(getActivity(),data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onResume() {
        super.onResume();
        new Modellist_Adapter(getActivity(),data).notifyDataSetChanged();
        Log.e("OnResume","BLOB");
        ((AddVehicle_fromOutside)getActivity()).setOne();
    }


    public void GetResult(String mURL) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                progressBar.setVisibility(View.GONE);
                                if (response.toString() != null) {
                                    try {
                                        jsonObject = new JSONObject(response.toString());
                                        ReadJson(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(error.toString())
                                        .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                GetResult(Constant.getVehiclesTypeUrl());
                                            }
                                        })
                                        .create()
                                        .show();
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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.choose_manuf_main));

    }


}
