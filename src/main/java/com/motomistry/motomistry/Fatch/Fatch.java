package com.motomistry.motomistry.Fatch;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Fragment.AddVehicle_fromOutside;
import com.motomistry.motomistry.Fragment.OTPkFragment;
import com.motomistry.motomistry.Fragment.UserDetailsSenterFragment;
import com.motomistry.motomistry.Fragment.getManufacturer;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.RegistrationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amit on 5/4/2018.
 */


public class Fatch {
    private final ProgressDialog progressDialog;
    private Context mConstext;
    private String mURL;
    private String TAG;
    private JSONObject jsonObject;
    private RequestQueue requestQueue;

    public Fatch(Context context, String tag, String url) {
        this.mURL = url;
        this.mConstext = context;
        this.TAG = tag;
        progressDialog = ProgressDialog.show(mConstext,
                "Loading",
                "Please wait..");
        GetResult();
    }
    public void GetResult() {
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                progressDialog.dismiss();
                                if (response.toString() != null) {
                                    try {
                                        jsonObject = new JSONObject(response.toString());
                                        setResult(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(mConstext);
                                builder.setMessage(error.toString())
                                        .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                GetResult();
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
            requestQueue = Volley.newRequestQueue(mConstext);
        return requestQueue;
    }
    public void setResult(JSONObject object) {
        Log.e("Response",object.toString());
        switch (TAG) {
            case "REGISTRATION":
                try {
                    if (object.getString("Stetus").equals("1")){
                        ((RegistrationActivity)mConstext).FrgmentTrans(new OTPkFragment());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "OTP_VERIFY":
                try {
                    switch (object.getString("status")){
                        case "0":
                            Toast.makeText(mConstext, "wrong otp", Toast.LENGTH_SHORT).show();
                            break;
                        case "1":
                            int city_id= 0;
                            try {
                                city_id = object.getJSONObject("User").getInt("city_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new Session(mConstext).createLoginSession(String.valueOf(object.getJSONObject("User").getInt("id")),
                                    object.getJSONObject("User").getString("mobile"),
                                    object.getJSONObject("User").getString("full_name"),
                                    object.getJSONObject("User").getString("emailid"),
                                    String.valueOf(city_id),
                                    object.getJSONObject("User").getString("profile_pic"));

                            if(object.getJSONObject("User").getString("full_name") == "null"){

                                ((RegistrationActivity) mConstext).FrgmentTrans(new UserDetailsSenterFragment());
                            }
                            else {
                                mConstext.startActivity(new Intent(mConstext,DashbordActivity.class));
                            }


                            break;
                    }

                } catch (JSONException e) {
                   e.printStackTrace();
                }
                break;
            case "PROFILE_SUBMIT":
                mConstext.startActivity(new Intent(mConstext,AddVehicle_fromOutside.class));
                break;
            case "MANUFACTURE_LIST":
                new getManufacturer().ReadJson(jsonObject);
                break;
        }
    }
}

