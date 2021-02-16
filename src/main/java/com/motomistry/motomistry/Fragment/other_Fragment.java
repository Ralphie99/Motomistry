package com.motomistry.motomistry.Fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikhil on 23-Mar-18.
 */


public class other_Fragment extends Fragment {

    EditText ed1,ed2,ed3,ed4;
    private View view;
    private static String fuelType="";
    TextView petrol_text,diesel_text,add_vehicle_outside;
    private String rc_no="";
    private Session session;
    private String USER_ID;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.other_entries,container,false);

        ed1 = (EditText) view.findViewById(R.id.editText1);
        ed2 = (EditText) view.findViewById(R.id.editText2);
        ed3 = (EditText) view.findViewById(R.id.editText3);
        ed4 = (EditText) view.findViewById(R.id.editText4);
        petrol_text = (TextView) view.findViewById(R.id.petrol_text);
        diesel_text = (TextView) view.findViewById(R.id.diesel_text);
        add_vehicle_outside = (TextView) view.findViewById(R.id.add_vehicle_outside);

        GetRecordFromSession();

        final ColorStateList oldColor = petrol_text.getTextColors();

        petrol_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.null_tohalffade);
                petrol_text.setBackground(getResources().getDrawable(R.drawable.on_selected));
                petrol_text.setTextColor(getResources().getColor(R.color.White));
                diesel_text.setTextColor(oldColor);
                petrol_text.startAnimation(anim);
                diesel_text.setBackground(getResources().getDrawable(R.drawable.roundcorner_rectangle));
                fuelType = "Petrol";

            }
        });

        diesel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.null_tohalffade);
                diesel_text.setBackground(getResources().getDrawable(R.drawable.on_selected));
                diesel_text.setTextColor(getResources().getColor(R.color.White));
                petrol_text.setTextColor(oldColor);
                diesel_text.startAnimation(anim);
                petrol_text.setBackground(getResources().getDrawable(R.drawable.roundcorner_rectangle));
                fuelType = "Diesel";
            }
        });

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count==2)
                    ed2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if((ed2.getText().length()) == 2)
                    ed3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count==2)
                    ed4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final int v_type = Constant.IsTowVechile?0:1;

        add_vehicle_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rc_no=ed1.getText().toString()+ed2.getText().toString()+ed3.getText().toString()+ed4.getText().toString();
//                if(ed1.getText().toString().equals("")|| ed2.getText().toString().equals("")|| ed3.getText().toString().equals("")|| ed4.getText().toString().equals(""))
//                {
//                    Toast.makeText(getActivity())
//                }
                GetResult(BaseClass.getGetVehicalRegUrl(USER_ID,v_type,BaseClass.m_ID,fuelType,rc_no));
            }
        });

        return view;
    }


    private void GetRecordFromSession(){
        session=new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
    }
    public void GetResult(String mURL) {
        final String TAG="Result";
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());

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
    public void ReadJson(  JSONObject jsonObject) {
        try {
            String result=jsonObject.getString("Stetus");
            if (result.equals("1")){
                ((DashbordActivity)getActivity()).replace_Fragment(new MyVehicles());
            }else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.other_main));

    }


}
