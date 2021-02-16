package com.motomistry.motomistry.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.motomistry.motomistry.OtherClass.CustomDialog;
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

public class EnterAddress extends Fragment {


    private View view;
    EditText city,locality,house_no,pincode,name,phone;
    Button save_address;
    private CustomDialog progressDialog;
    private Session session;
    String USER_ID;
    String slocality,shouse_no,spincode,sname,sphone;
    View city_border,locality_border,house_border,pincode_border,name_border,phone_border;
    Animation scale_up_border,scale_down_border;
    int focus = 6;
    private RequestQueue requestQueue;
    LinearLayout city_layout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_enter_address,container,false);

        findView();
        Get_User_ID();

        save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getDatafromEdit()) {
                    GetResult(BaseClass.BaseUrl + "add_address_for_product?user_id=" + USER_ID
                            + "&name=" + sname
                            + "&contect_no=" + sphone
                            + "&Main_address=" + slocality
                            + "&secondry_address=" + shouse_no
                            + "&zip=" + spincode);
                }
            }
        });

        scale_up_border  = AnimationUtils.loadAnimation(getActivity(),R.anim.scale_up_border);
        scale_down_border = AnimationUtils.loadAnimation(getActivity(),R.anim.scale_down_border);

        city.setText("Jabalpur");
        city.setFocusable(false);
        city.setClickable(false);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"For now, we only operate in Jabalpur",Toast.LENGTH_LONG).show();
            }
        });

        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                findVoid(city_border,locality_border,house_border,pincode_border,name_border,phone_border);
                focus = 0;
            }
        });

        locality.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                findVoid(locality_border,city_border,house_border,pincode_border,name_border,phone_border);
                focus = 1;
            }
        });

        house_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                findVoid(house_border,city_border,locality_border,pincode_border,name_border,phone_border);
                focus = 2;
            }
        });

        pincode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                findVoid(pincode_border,city_border,locality_border,house_border,name_border,phone_border);
                focus = 3;
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                findVoid(name_border,city_border,locality_border,house_border,pincode_border,phone_border);
                focus = 4;
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                findVoid(phone_border,city_border,locality_border,house_border,pincode_border,name_border);
                focus = 5;
            }
        });

        return view;
    }


    private void findVoid(View view,View view_1,View view_2,View view_3,View view_4,View view_5){

        view.setVisibility(View.VISIBLE);
        view_1.setVisibility(View.INVISIBLE);
        view_2.setVisibility(View.INVISIBLE);
        view_3.setVisibility(View.INVISIBLE);
        view_4.setVisibility(View.INVISIBLE);
        view_5.setVisibility(View.INVISIBLE);
        view.startAnimation(scale_up_border);


        switch (focus){

            case 0:
                city_border.startAnimation(scale_down_border);
                break;

            case 1:
                locality_border.startAnimation(scale_down_border);
                break;

            case 2:
                house_border.startAnimation(scale_down_border);
                break;

            case 3:
                pincode_border.startAnimation(scale_down_border);
                break;

            case 4:
                name_border.startAnimation(scale_down_border);
                break;

            case 5:
                phone_border.startAnimation(scale_down_border);
                break;

                default:
                    break;

        }
    }

    private void findView(){

        city = (EditText) view.findViewById(R.id.city);
        locality = (EditText) view.findViewById(R.id.locality);
        house_no = (EditText) view.findViewById(R.id.house_no);
        pincode = (EditText) view.findViewById(R.id.pincode);
        name = (EditText) view.findViewById(R.id.name);
        phone = (EditText) view.findViewById(R.id.phone);
        save_address = (Button) view.findViewById(R.id.save_address);
        city_border = view.findViewById(R.id.city_border);
        locality_border = view.findViewById(R.id.locality_border);
        house_border = view.findViewById(R.id.house_border);
        pincode_border = view.findViewById(R.id.pincode_border);
        name_border = view.findViewById(R.id.name_border);
        phone_border = view.findViewById(R.id.phone_border);

        city_layout = (LinearLayout) view.findViewById(R.id.city_layout);

        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void Get_User_ID(){
        session=new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
    }

    private boolean getDatafromEdit(){
        slocality = locality.getText().toString();
        shouse_no = house_no.getText().toString();
        spincode = pincode.getText().toString();
        sname = name.getText().toString();
        sphone = phone.getText().toString();

        if(slocality.equals("")){
            locality.setError("Enter Locality!");
            return false;
        }

        else if(shouse_no.equals("")){
            house_no.setError("Enter House No./Name!");
            return false;
        }

        else if(spincode.equals("")){
            pincode.setError("Enter Pincode!");
            return false;
        }

        else if(sname.equals("")){
            name.setError("Enter Name!");
            return false;
        }

        else if(sphone.equals("")){
            phone.setError("Enter Phone Number!");
            return false;
        }
        else
            return true;

    }



        private void ReadJson(JSONObject jsonObject) {

            progressDialog.dismiss();
            try {
                String status = jsonObject.getString("status");

                if(status.equals("1")){
                    Toast.makeText(getActivity(),"Address Saved",Toast.LENGTH_LONG).show();
                    ((DashbordActivity)getActivity()).bacPOP();
                }
                else if(status.equals("2")){
                    Toast.makeText(getActivity(),"Address Already Saved!",Toast.LENGTH_LONG).show();
                    ((DashbordActivity)getActivity()).bacPOP();
                }
                else{
                    Toast.makeText(getActivity(),"Error Saving Address.Try Again!",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.enter_address_main));

    }

    public void GetResult(String mURL) {
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

}
