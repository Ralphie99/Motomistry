package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Accessory_item_model;
import com.motomistry.motomistry.Model.Address_Model;
import com.motomistry.motomistry.Model.My_Cart_Model;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Select_Address extends Fragment {


    private View view;
    ListView address_list;
    private CustomDialog progressDialog;
    ArrayList<Address_Model> address_data;
    private RequestQueue requestQueue;
    FloatingActionButton add_address;
    String USER_ID;
    private Session session;
    LinearLayout proceed_to_payment,continue_bottom;
    ConnectionManager connectionManager;
    int radio_checked = 0;
    SelectAddress_Adapter select_address;
    My_Cart my_cart;
    TextView toolbar_title;
    ImageView toolbar_back;

    public Select_Address() {
    }

    @SuppressLint("ValidFragment")
    public Select_Address(My_Cart my_cart) {

        this.my_cart = my_cart;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        } else {
            GetAddresses(BaseClass.BaseUrl + "get_user_address?user_id=" + USER_ID, 1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_select__address, container, false);

        address_data = new ArrayList<Address_Model>();
        address_list = (ListView) view.findViewById(R.id.address_list);
        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);
        add_address = (FloatingActionButton) view.findViewById(R.id.add_address);
        proceed_to_payment = (LinearLayout) view.findViewById(R.id.proceed_to_payment);
        continue_bottom = (LinearLayout) view.findViewById(R.id.continue_bottom);
        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        toolbar_back = (ImageView) view.findViewById(R.id.back_button);

        if(Constant.view_from_profile){
            continue_bottom.setVisibility(View.GONE);
            toolbar_title.setText("Your Addresses");
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 20,80);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            add_address.setLayoutParams(params);
        }else {
            continue_bottom.setVisibility(View.VISIBLE);
            toolbar_title.setText("Select Address");
        }

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).bacPOP();
            }
        });

        Get_User_ID();

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity) getActivity()).replace_Fragment(new EnterAddress());
            }
        });

        connectionManager = new ConnectionManager(getActivity());
        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        } else {
            GetAddresses(BaseClass.BaseUrl + "get_user_address?user_id=" + USER_ID, 1);
        }

        proceed_to_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray jsonArray = new JSONArray();
                ArrayList<My_Cart_Model> data = my_cart.getCartData();
                for (int i = 0; i < data.size(); i++) {
                    JSONObject User = new JSONObject();
                    try {
                        User.put("user_id", USER_ID);
                        User.put("product_id", data.get(i).getAccessory_id());
                        User.put("qty", My_Cart.qty.get(i));
                        User.put("vendor_id", data.get(i).getVendor_id());
                        User.put("address_id", select_address.getSelectedItem().getAddress_id());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(User);

                }


                Log.e("JSON_STRING_TO_SEND", jsonArray.toString());
                GetAddresses(BaseClass.BaseUrl + "book_order?order_data=" + jsonArray.toString(), 2);


            }
        });

        return view;
    }

    private void Get_User_ID() {
        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
    }


    private class SelectAddress_Adapter extends BaseAdapter {

        ArrayList data;
        private int selectedPosition = -1;

        public SelectAddress_Adapter(ArrayList data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            final int position = i;
            if (view == null) {

                view = LayoutInflater.from(getActivity()).inflate(R.layout.address_list_item, viewGroup, false);
            }

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView address = (TextView) view.findViewById(R.id.address);
            TextView phone = (TextView) view.findViewById(R.id.phone);
            ImageView edit_address = (ImageView) view.findViewById(R.id.edit_address);
            final RadioButton selector = (RadioButton) view.findViewById(R.id.selector);

            if(Constant.view_from_profile){
                selector.setVisibility(View.GONE);
            }
            else{
                selector.setVisibility(View.VISIBLE);
            }

            name.setText(address_data.get(i).getName());
            address.setText(address_data.get(i).getAddress2() + " " + address_data.get(i).getAddress() + " - " + address_data.get(i).getPincode());
            phone.setText(address_data.get(i).getPhone());

            edit_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCheckChanged(selector);
                }
            });

            selector.setTag(position);
            selector.setChecked(position == selectedPosition);
            selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCheckChanged(v);
                }
            });

            return view;
        }

        private void itemCheckChanged(View v) {
            selectedPosition = (Integer) v.getTag();
            notifyDataSetChanged();
        }

        public Address_Model getSelectedItem() {
            if (selectedPosition != -1) {
                return address_data.get(selectedPosition);
            }
            return null;
        }
    }

    public void GetAddresses(String mURL, final int flag) {
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                progressDialog.dismiss();

                                if (flag == 1)
                                    ReadJson(response);
                                else if (flag == 2)
                                    ReadJson2(response);

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


    private void ReadJson(JSONObject json_String) {
        Log.e("ADDRESS_DATA", json_String.toString());
        address_data.clear();
        try {

            JSONArray jsonArray = json_String.getJSONArray("user_address");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentJSONObject = jsonArray.getJSONObject(i);
                int address_id = currentJSONObject.getInt("id");
                String name = currentJSONObject.getString("name");
                String phone = currentJSONObject.getString("contact");
                String address = currentJSONObject.getString("address");
                String address2 = currentJSONObject.getString("address2");
                String pincode = currentJSONObject.getString("zip");

                address_data.add(new Address_Model(address_id, name, phone, address, address2, pincode));
                Log.e("Data Added", String.valueOf(address_data.size()));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
        select_address = new SelectAddress_Adapter(address_data);
        address_list.setAdapter(select_address);
    }

    private void ReadJson2(JSONObject jsonObject) {


        try {
            String status = jsonObject.getString("status");
            if (status.equals("1"))
                Snackbar.make(view, "Order Created!", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(view, "Unable to create order!", Snackbar.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.select_address_main));

    }

}
