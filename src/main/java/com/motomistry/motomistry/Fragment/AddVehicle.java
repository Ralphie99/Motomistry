package com.motomistry.motomistry.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.Session;
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
import java.util.HashMap;
import java.util.Map;


public class AddVehicle extends Fragment {

    private View view;
    private RelativeLayout manufacturer_selector,model_selector;
    private LinearLayout fueltypeSelector,registration_number;
    private EditText ed1,ed2,ed3,ed4;
    private TextView petrol,diesel,manufaturer_name,model_name,add_vehicle_inside;
    private RadioGroup vehicle_group;
    private static String manufacturer = "",model = "",fuelType="",vehicleType="";
    private Boolean radio_isChecked = false;
    private int mid,mod_id;
    private Session session;
    private String USER_ID;
    private String rc_no="";
    private RequestQueue requestQueue;
    LinearLayout add_vehicle_with_internet;
    ConnectionManager connectionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_vehicle,container,false);
        manufacturer_selector = (RelativeLayout) view.findViewById(R.id.manufacturer_selector);
        model_selector = (RelativeLayout) view.findViewById(R.id.model_selector);
        fueltypeSelector = (LinearLayout) view.findViewById(R.id.fueltypeSelector);
        registration_number = (LinearLayout) view.findViewById(R.id.registration_number);
        ed1 = (EditText) view.findViewById(R.id.editText1);
        ed2 = (EditText) view.findViewById(R.id.editText2);
        ed3 = (EditText) view.findViewById(R.id.editText3);
        ed4 = (EditText) view.findViewById(R.id.editText4);
        petrol = (TextView) view.findViewById(R.id.petrol);
        diesel = (TextView) view.findViewById(R.id.diesel);
        manufaturer_name = (TextView) view.findViewById(R.id.manufacturer_name);
        model_name = (TextView) view.findViewById(R.id.model_name);
        add_vehicle_inside = (TextView) view.findViewById(R.id.add_vehicle_inside);
        vehicle_group = (RadioGroup) view.findViewById(R.id.vehicle_group);
        GetRecordFromSession();
        add_vehicle_with_internet = (LinearLayout) view.findViewById(R.id.add_vehicle_with_internet);

        connectionManager = new ConnectionManager(getActivity());
        View no_internet = view.findViewById(R.id.no_internet);
        if (!connectionManager.isConnected()) {
            no_internet.setVisibility(View.VISIBLE);
            add_vehicle_with_internet.setVisibility(View.GONE);
        } else {
            add_vehicle_with_internet.setVisibility(View.VISIBLE);
            no_internet.setVisibility(View.GONE);
        }



        vehicle_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.two_wheeler:
                        Constant.IsTowVechile = true;
                        Constant.IsFourVechile = false;
                        radio_isChecked = true;
                        vehicleType = "Two Wheeler";
                        manufacturer_selector.setVisibility(View.VISIBLE);
                        break;

                    case R.id.four_wheeler:
                        Constant.IsFourVechile = true;
                        Constant.IsTowVechile = false;
                        radio_isChecked = true;
                        vehicleType = "Four Wheeler";
                        manufacturer_selector.setVisibility(View.VISIBLE);
                        break;
                        default:
                            radio_isChecked = false;
                            break;
                }
            }
        });

        final ColorStateList oldColor = petrol.getTextColors();

        manufacturer_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radio_isChecked){
                    Constant.fromOutside = false;
                ((DashbordActivity)getActivity()).replace_Fragment(new getManufacturer(AddVehicle.this));
                    model_selector.setVisibility(View.VISIBLE);
                }
                else
                    Toast.makeText(getActivity(),"Vehicle Type Not Selected!", Toast.LENGTH_SHORT).show();
            }
        });

        model_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manufaturer_name.getText().toString() == "")
                    Toast.makeText(getActivity(),"Select Manufacturer!", Toast.LENGTH_SHORT).show();
                else{
                ((DashbordActivity)getActivity()).replace_Fragment(new getModel(AddVehicle.this));
                    fueltypeSelector.setVisibility(View.VISIBLE);}
            }
        });

            petrol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.null_tohalffade);
                    petrol.setBackground(getResources().getDrawable(R.drawable.on_selected));
                    petrol.setTextColor(getResources().getColor(R.color.White));
                    diesel.setTextColor(oldColor);
                    petrol.startAnimation(anim);
                    diesel.setBackground(getResources().getDrawable(R.drawable.roundcorner_rectangle));
                    fuelType = "Petrol";
                    registration_number.setVisibility(View.VISIBLE);
                    add_vehicle_inside.setVisibility(View.VISIBLE);
                }
            });

            diesel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.null_tohalffade);
                    diesel.setBackground(getResources().getDrawable(R.drawable.on_selected));
                    diesel.setTextColor(getResources().getColor(R.color.White));
                    petrol.setTextColor(oldColor);
                    diesel.startAnimation(anim);
                    petrol.setBackground(getResources().getDrawable(R.drawable.roundcorner_rectangle));
                    fuelType = "Diesel";
                    registration_number.setVisibility(View.VISIBLE);
                    add_vehicle_inside.setVisibility(View.VISIBLE);
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


        add_vehicle_inside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rc_no=ed1.getText().toString()+ed2.getText().toString()+ed3.getText().toString()+ed4.getText().toString();
//                if(ed1.getText().toString().equals("")|| ed2.getText().toString().equals("")|| ed3.getText().toString().equals("")|| ed4.getText().toString().equals(""))
                GetResult(BaseClass.getGetVehicalRegUrl(USER_ID,BaseClass.VEHICAL_TYPE,BaseClass.m_ID,fuelType,rc_no));
            }
        });


        return view;
    }

    public void setManufacturer(String manufacturer){

        this.manufacturer = manufacturer;
        manufaturer_name.setText(manufacturer);
    }

    public void setModel(String model){

        this.model = model;
        model_name.setText(model);
    }

    public void setIDs(int mid,int mod_id){

        this.mid = mid;
        this.mod_id = mod_id;
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




    @Override
    public void onResume() {
        super.onResume();
        manufaturer_name.setText(manufacturer);
        model_name.setText(model);
        manufacturer = "";
        model = "";
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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.add_vehicle_main));

    }
}
