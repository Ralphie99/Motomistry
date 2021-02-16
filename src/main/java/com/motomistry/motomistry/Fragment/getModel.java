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
import android.transition.Slide;
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
import com.motomistry.motomistry.Adapter.Modellist_Adapter2;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Modellist2;
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
import java.util.Map;


public class getModel extends Fragment {
    String TAG="Model";
    private View view;
    ListView model_list;
    ArrayList<Modellist2> data = new ArrayList<Modellist2>();
    CustomDialog alertDialog;
    String id;
    private RequestQueue requestQueue;
    private int v_id;
    AddVehicle parent1;
    ImageView model_image;
    private ProgressBar progressBar;

    public getModel() {
    }

    @SuppressLint("ValidFragment")
    public getModel(AddVehicle addVehicle) {
        parent1 = addVehicle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_get_model, container, false);

        model_list = (ListView) view.findViewById(R.id.model_list);
        model_image = (ImageView) view.findViewById(R.id.model_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        alertDialog = new CustomDialog(getActivity());
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
         v_id=Constant.IsTowVechile?0:1;

         if(v_id==0){
             model_image.setImageResource(R.drawable.two_wheeler);
         }else {
             model_image.setImageResource(R.drawable.four_wheeler);
         }

        if (Constant.fromOutside) {
            model_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    other_Fragment others = new other_Fragment();
                    Slide slide = new Slide(Gravity.RIGHT);
                    slide.setDuration(900);
                    Slide slide2 = new Slide(Gravity.LEFT);
                    slide.setDuration(900);
                    others.setEnterTransition(slide);
                    others.setExitTransition(slide2);
                    BaseClass.m_ID=data.get(position).getId();
                    ((AddVehicle_fromOutside)getActivity()).replace_Fragment(others);

                }
            });
        } else {
            model_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    parent1.setModel(data.get(position).getModel_name());
                    BaseClass.m_ID=data.get(position).getId();
                    parent1.setIDs(data.get(position).getMID(),data.get(position).getId());
                    getFragmentManager().popBackStack();
                }

            });
        }
            GetResult(Constant.getGetModelListUrl(BaseClass.M_ID, v_id));
        return view;
    }


    public void ReadJson(JSONObject jsonObject) {
        data.clear();
        try {
            JSONArray jsonArray =jsonObject.getJSONArray("Vehicle_response");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object= jsonArray.getJSONObject(i);
                int id=object.getInt("id");
                int mid=object.getInt("MID");
                int vehicle_type=object.getInt("vehicle_type");
                String value =object.getString("Model_name");
                Modellist2 modellist2=new Modellist2(id,vehicle_type,mid,value);
                data.add(modellist2);
                Log.e("MODEL_DATA",String.valueOf(data.size()));

            }
            model_list.setAdapter(new Modellist_Adapter2(getActivity(),data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetResult(String mURL) {
        progressBar.setVisibility(View.VISIBLE);
        Log.e("Model_Url",mURL);
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
                                        JSONObject jsonObject = new JSONObject(response.toString());
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(error.toString())
                                        .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                GetResult(Constant.getGetModelListUrl(0,v_id));
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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.choose_model_main));

    }

}
