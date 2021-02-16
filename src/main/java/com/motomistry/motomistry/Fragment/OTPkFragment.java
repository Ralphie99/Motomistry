package com.motomistry.motomistry.Fragment;

import android.content.Context;
import android.net.Uri;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.motomistry.motomistry.Fatch.Fatch;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.New.Fragment.DrivingSchoolHistory;
import com.motomistry.motomistry.New.Model.ModelDrivingHistory;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.RegistrationActivity;
import com.motomistry.motomistry.Utility.BaseClass;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

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

public class OTPkFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button BtnOtpSubmit,resend_otp;
    SmsVerifyCatcher smsVerifyCatcher;
    EditText et1,et2,et3,et4;
    String code="";
    String mobile="";
    TextView otp_sentence;
    private CustomDialog progressDialog;
    private RequestQueue requestQueue;
    InputMethodManager mgr;

    public OTPkFragment() {
        // Required empty public constructor
    }

    public static OTPkFragment newInstance() {
        OTPkFragment fragment = new OTPkFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view==null){
        view=inflater.inflate(R.layout.fragment_otpk, container, false);
            mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            FindView();
            progressDialog = new CustomDialog(getActivity());
            progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
            progressDialog.setCanceledOnTouchOutside(false);
            OtpVerify();

    }return view;}

    private void OtpVerify() {
        smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                code = parseCode(message);//Parse verification code
                et1.setText(code.substring(0,1));
                et2.setText(code.substring(1,2));
                et3.setText(code.substring(2,3));
                et4.setText(code.substring(3,4));//set code in edit text
                try {
                    Thread.sleep(2000);
                    new Fatch(getActivity(),"OTP_VERIFY",Constant.getOtpVerificationUrl(code));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }
    private void FindView(){
        BtnOtpSubmit=(Button)view.findViewById(R.id.btn_otpsubmit);
        resend_otp = (Button) view.findViewById(R.id.resend_otp);
        otp_sentence = (TextView) view.findViewById(R.id.otp_sentence);
            et1 = (EditText) view.findViewById(R.id.et1);
            et2 = (EditText) view.findViewById(R.id.et2);
            et3 = (EditText) view.findViewById(R.id.et3);
            et4 = (EditText) view.findViewById(R.id.et4);
        otp_sentence.setText("+91   "+Constant.MOBILE_NUMBER);
        BtnOtpSubmit.setOnClickListener(this);
        resend_otp.setOnClickListener(this);
        onTextChange();
    }

    private void onTextChange() {

        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    et1.setHint("");
                else
                    et1.setHint("0");
            }
        });

        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    et2.setHint("");
                else
                    et2.setHint("0");
            }
        });

        et3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    et3.setHint("");
                else
                    et3.setHint("0");
            }
        });

        et4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    et4.setHint("");
                else
                    et4.setHint("0");
            }
        });

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==1){
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==1){
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==1){
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private String parseCode(String message){
        String code = message.substring(message.length()-4);
        return code;
    }

    @Override
    public void onClick(View view) {
        if (view==BtnOtpSubmit){
            String otp=et1.getText().toString()+et2.getText().toString()+et3.getText().toString()+et4.getText().toString();
            if (otp.length()>0)
                mgr.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(et2.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(et3.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(et4.getWindowToken(), 0);
            new Fatch(getActivity(),"OTP_VERIFY",Constant.getOtpVerificationUrl(otp));

        }
        else if(view == resend_otp){

            GetResult(BaseClass.BaseUrl+"resend_otp?mobile_no="+Constant.MOBILE_NUMBER);
        }
    }


    public void ReadJson(JSONObject jsonObject) {

        try {
            String status = jsonObject.getString("status");
            if(status == "0"){
                Toast.makeText(getActivity(),"Unable to send OTP!",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                                if (response.toString() != null) {
                                    ReadJson(response);
                                }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.otp_main));

    }

}