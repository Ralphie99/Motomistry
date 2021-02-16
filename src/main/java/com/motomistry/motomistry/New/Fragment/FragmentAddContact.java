package com.motomistry.motomistry.New.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentAddContact extends Fragment {

    private View view;
    private Button submit_button;
    private EditText contact_1,contact_2,contact_3;
    private RequestQueue requestQueue;
String TAG="ADD_CONTACT";
    public FragmentAddContact() {
        // Required empty public constructor
    }
    public static FragmentAddContact newInstance(String param1, String param2) {
        FragmentAddContact fragment = new FragmentAddContact();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view==null){

            view= inflater.inflate(R.layout.fragment_add_contact, container, false);
            contact_1=(EditText)view.findViewById(R.id.contact_1);
            contact_2=(EditText)view.findViewById(R.id.contact_2);
            contact_3=(EditText)view.findViewById(R.id.contact_3);
            submit_button=(Button)view.findViewById(R.id.submit_button);
            contact_1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()==10){
                        GetResult("Url",1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            contact_2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()==10){
                        GetResult("Url",1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            contact_3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()==10){
                        GetResult("Url",1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contact_1.getText().toString().length()==10){
                        if (contact_2.getText().toString().length()==10){
                            if (contact_3.getText().toString().length()==10){

                            }else {
                                contact_3.setError("Invalid number");
                                contact_3.requestFocus();
                            }
                        }else {
                            contact_2.setError("Invalid number");
                            contact_2.requestFocus();
                        }
                    }else {
                        contact_1.setError("Invalid number");
                        contact_1.requestFocus();
                    }
                }
            });
        }
        return view;
    }
    public void GetResult(String mURL, final int type) {
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                if (response.toString() != null) {
                                    switch (type){
                                        case 1:
                                            ReadJson(response);
                                            break;
                                        case 2:
                                            break;
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

    private void ReadJson(JSONObject response) {

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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.add_contact_main));

    }
}
