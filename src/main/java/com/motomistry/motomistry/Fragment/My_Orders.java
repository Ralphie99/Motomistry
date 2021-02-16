package com.motomistry.motomistry.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Order_History_Model;
import com.motomistry.motomistry.Model.Order_history_item_model;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class My_Orders extends Fragment {

    private View view;
    ListView orders_list;
    private CustomDialog progressDialog;
    private RequestQueue requestQueue;
    ArrayList<Order_History_Model> data;
    Session session;
    private String USER_ID;
    ConnectionManager connectionManager;
    ImageView back_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_orders,container,false);

        orders_list = (ListView) view.findViewById(R.id.order_list);
        back_button = (ImageView) view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).bacPOP();
            }
        });

        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);


        data = new ArrayList<>();

        GetRecordFromSession();

        connectionManager = new ConnectionManager(getActivity());
        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        } else {
            GetResult(BaseClass.BaseUrl+"book_order_history?user_id="+USER_ID);
        }



//        orders_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ((DashbordActivity)getActivity()).replace_Fragment(new Order_Details());
//            }
//        });


        return view;
    }

    private void GetRecordFromSession() {
        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
        Log.e("USER_ID",USER_ID);
    }

    private class OrderList_Adapter  extends BaseAdapter {

        Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);

        ArrayList<Order_History_Model> data;

        public OrderList_Adapter(ArrayList data) {
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
            if(view == null){

                view = LayoutInflater.from(getActivity()).inflate(R.layout.order_history_item,viewGroup,false);
            }

            TextView order_id = (TextView) view.findViewById(R.id.order_id);
            order_id.setTypeface(iconFont);
            TextView order_date = (TextView) view.findViewById(R.id.order_date);
            order_date.setTypeface(iconFont);
            TextView order_total = (TextView) view.findViewById(R.id.order_total);
            LinearLayout item_container = (LinearLayout) view.findViewById(R.id.item_container);
            TextView order_pending = (TextView) view.findViewById(R.id.order_pending);
            TextView order_completed = (TextView) view.findViewById(R.id.order_completed);

            item_container.removeAllViews();

            for(int j=0;j<data.get(i).getItems().size();j++){
                View item_layout = LayoutInflater.from(getActivity()).inflate(R.layout.order_item_layout,viewGroup,false);
                Order_history_item_model current_item = data.get(i).getItems().get(j);

                TextView item_name = (TextView) item_layout.findViewById(R.id.item_order_name);
                item_name.setTypeface(iconFont);
                ImageView item_image = (ImageView) item_layout.findViewById(R.id.item_order_image);
                TextView seller_name = (TextView) item_layout.findViewById(R.id.seller_name);
                seller_name.setTypeface(iconFont);
                TextView item_price = (TextView) item_layout.findViewById(R.id.item_price);

                item_name.setText(current_item.getName());
                item_price.setText(getString(R.string.Rs)+" "+current_item.getPrice()+"(x"+current_item.getQty()+")");
                seller_name.setText("Seller: "+current_item.getVendor_name());

                item_container.addView(item_layout);
            }

            order_id.setText(String.valueOf("Order ID: "+data.get(i).getOrder_id()));
            order_date.setText(data.get(i).getOrder_date());
            order_total.setText(getString(R.string.Rs)+" "+data.get(i).getItems().get(0).getTotal());

            if(data.get(i).getStatus() == 0){
                order_completed.setVisibility(View.GONE);
                order_pending.setVisibility(View.VISIBLE);
            }
            else{
                order_completed.setVisibility(View.VISIBLE);
                order_pending.setVisibility(View.GONE);
            }

            return view;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.my_orders_main));

    }

    public void ReadJson(JSONObject jsonObject) {

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("order_history");

            for(int j=0;j<jsonArray.length();j++) {

                JSONObject object = jsonArray.getJSONObject(j);
                int order_id = object.getInt("order_id");
                String order_code = object.getString("order_code");
                String order_date = object.getString("order_date");
                int status = object.getInt("status");
                JSONArray items = object.getJSONArray("items");
                ArrayList<Order_history_item_model> item_list = new ArrayList<>();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject current_item = items.getJSONObject(i);
                    String item_name = current_item.getString("AName");
                    int item_id = current_item.getInt("Item_id");
                    int qty = current_item.getInt("qty");
                    String unit_price = current_item.getString("unit_price");
//                    String image_path = current_item.getString("item_image");
                    String total = current_item.getString("total");
                    String vendor_name = current_item.getString("vendor_name");

                    item_list.add(new Order_history_item_model(item_name,qty,item_id,unit_price,total,vendor_name));
                }
                String enquiry_number = object.getString("enquiry_number");

                data.add(new Order_History_Model(order_id,status,order_code,order_date,enquiry_number,item_list));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        orders_list.setAdapter(new OrderList_Adapter(data));
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

}
