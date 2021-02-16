package com.motomistry.motomistry.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.ModelBookingServiceHistory;
import com.motomistry.motomistry.New.Fragment.FragmentBookingServiceHistry;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class otp_employee_verify extends Fragment {

    private View view;
    EditText et1,et2,et3,et4;
    TextView otp_incorrect;
    InputMethodManager mgr;
    Button submit;
    private RequestQueue requestQueue;
    private CustomDialog progressDialog;
    private int employee_id;
    private int vendor_id;
    private String emp_name;
    private String emp_code;
    private String emp_mobile;
    private String emp_image_path;
    AlertDialog alertDialog;
    TextView wrong_otp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_otp_employee_verify,container,false);
        et1 = (EditText) view.findViewById(R.id.et1);
        et2 = (EditText) view.findViewById(R.id.et2);
        et3 = (EditText) view.findViewById(R.id.et3);
        et4 = (EditText) view.findViewById(R.id.et4);
        submit = (Button) view.findViewById(R.id.submit_employee);
        wrong_otp = (TextView) view.findViewById(R.id.wrong_otp);

        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);

        mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        otp_incorrect = (TextView) view.findViewById(R.id.otp_incorrect);


        onTextChange();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=et1.getText().toString()+et2.getText().toString()+et3.getText().toString()+et4.getText().toString();
                if (otp.length()>0)
                    mgr.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(et2.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(et3.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(et4.getWindowToken(), 0);

                progressDialog.show();
                GetResult(BaseClass.BaseUrl+"verifay_employe?otp="+otp);

            }
        });
        return view;
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

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==1){
                    String otp=et1.getText().toString()+et2.getText().toString()+et3.getText().toString()+et4.getText().toString();
                    progressDialog.show();
                    GetResult(BaseClass.BaseUrl+"verifay_employe?otp="+otp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    private void showAlert(){

        Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);

        alertDialog = new AlertDialog.Builder(getActivity()).create();
        View alertLayout = LayoutInflater.from(getActivity()).inflate(R.layout.employe_info_dialog, null);
        TextView d_emp_name = (TextView) alertLayout.findViewById(R.id.emp_name);
        TextView d_emp_phone = (TextView) alertLayout.findViewById(R.id.emp_phone);
        TextView ok_dismiss = (TextView) alertLayout.findViewById(R.id.ok_dismiss);

        d_emp_name.setTypeface(iconFont);

        ImageView d_emp_image = (ImageView) alertLayout.findViewById(R.id.emp_image);

        d_emp_name.setText(emp_name);
        d_emp_phone.setText(emp_mobile);

        ok_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Picasso.get().load(emp_image_path).placeholder(R.drawable.employee_placeholder).into(d_emp_image);

        alertDialog.setView(alertLayout);

        alertDialog.show();
    }

    public void ReadJson(JSONObject jsonObject) {

        try {

            String status = jsonObject.getString("status");

            if(status.equals("0")){
                wrong_otp.setVisibility(View.VISIBLE);
                wrong_otp.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.jiggle_animation));
            }
            else {
                wrong_otp.setVisibility(View.GONE);
                JSONArray jsonArray = jsonObject.getJSONArray("employee_details");
                JSONObject object = jsonArray.getJSONObject(0);
                employee_id = object.getInt("id");
                vendor_id = object.getInt("vid");
                emp_name = object.getString("emp_name");
                emp_code = object.getString("emp_code");
                emp_mobile = object.getString("mobile");
                emp_image_path = object.getString("emp_image");
                showAlert();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void GetResult(String mURL) {

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("OTP_EMPLOYEE", response.toString());
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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.otp_employee_main));

    }
}
