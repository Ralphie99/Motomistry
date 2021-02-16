package com.motomistry.motomistry.New.Fragment;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.motomistry.motomistry.Fragment.otp_employee_verify;
import com.motomistry.motomistry.Model.ModelBookingServiceHistory;
import com.motomistry.motomistry.New.Model.ModelDraivivgSchool;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentBookingServiceHistry extends Fragment {

    private View view;
    private ListView insurance_list;
    ArrayList<ModelBookingServiceHistory> data;
    private String TAG = "BookingHistory";
    private CustomDialog progressDialog;
    private RequestQueue requestQueue;
    private TextView toolbar_title;
    private Session session;
    private String USER_ID;
    ConnectionManager connectionManager;
    AlertDialog alertDialog;
    private String s_comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_booking_history, container, false);

        ((DashbordActivity) getActivity()).selectIcon(3);
        insurance_list = (ListView) view.findViewById(R.id.insurance_list);
        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        data = new ArrayList<ModelBookingServiceHistory>();
        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);
        toolbar_title.setText("Booking History");
        GetRecordFromSession();
        insurance_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // ((DashbordActivity)getActivity()).replace_Fragment(new DrivingSchool_details(data.get(i).getBookin_id()));
            }
        });

        connectionManager = new ConnectionManager(getActivity());
        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        } else {
            GetResult(BaseClass.getGetVehicleBookingHistoryUrl(USER_ID),1);
        }



        return view;
    }

    private void GetRecordFromSession() {
        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
    }

    private class CustomAdapter extends ArrayAdapter<ModelBookingServiceHistory> {

        Context context;

        public CustomAdapter(@NonNull Context context, @NonNull List objects) {
            super(context, 0, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.screen_vehicle_booking_history, parent, false);
            }

            final ModelBookingServiceHistory current = getItem(position);
            TextView Model_name = (TextView) convertView.findViewById(R.id.Model_name);
            TextView manufacturer_name = (TextView) convertView.findViewById(R.id.manufacturer_name);
            TextView vendor_name = (TextView) convertView.findViewById(R.id.vendor_name);
            TextView bookdate = (TextView) convertView.findViewById(R.id.bookdate);
            TextView booking_status = (TextView) convertView.findViewById(R.id.booking_status);
            TextView verify_employee = (TextView) convertView.findViewById(R.id.verify_employee);
            TextView rate_us = (TextView) convertView.findViewById(R.id.rate_us);
            rate_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert(current.getVID());
                }
            });

            if(current.getService_type() == 0) {
                verify_employee.setVisibility(View.GONE);
            }
            else{
                verify_employee.setVisibility(View.VISIBLE);
            }

            verify_employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity)getActivity()).replace_Fragment(new otp_employee_verify());
                }
            });

            Model_name.setTextSize(20);
            Model_name.setText(current.getModel_name());
            manufacturer_name.setText(current.getManufacturer_name());
            vendor_name.setText(current.getVendor_name());
            bookdate.setText(current.getBookdate());
            ImageView model_image = (ImageView) convertView.findViewById(R.id.model_image);
            if (current.getVehicle_type() == 1) {
                model_image.setImageResource(R.drawable.car);
            } else {
                model_image.setImageResource(R.drawable.motorbike);
            }
            if (current.getWork_done()==0){
                booking_status.setVisibility(View.VISIBLE);
                booking_status.setText("Pending");
                booking_status.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                rate_us.setVisibility(View.GONE);
            }else {
                booking_status.setVisibility(View.GONE);
                booking_status.setText("Done");
                booking_status.setBackgroundColor(getResources().getColor(R.color.flatGreen));
                rate_us.setVisibility(View.VISIBLE);
                verify_employee.setVisibility(View.VISIBLE);
                if(verify_employee.getVisibility()==View.VISIBLE){
                    verify_employee.setVisibility(View.GONE);
                }
            }

            return convertView;
        }
    }

    private void showAlert(final int VID){

        Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);

        alertDialog = new AlertDialog.Builder(getActivity()).create();
        View alertLayout = LayoutInflater.from(getActivity()).inflate(R.layout.rate_us, null);
        final TextView comment = (TextView) alertLayout.findViewById(R.id.commment);
        RatingBar rating = (RatingBar) alertLayout.findViewById(R.id.rating);
        final ImageView emoji = (ImageView) alertLayout.findViewById(R.id.emoji);
        TextView ok_dismiss = (TextView) alertLayout.findViewById(R.id.ok_dismiss);

        comment.setTypeface(iconFont);
        ok_dismiss.setTypeface(iconFont);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating > 0 && rating <= 1){
                    emoji.setImageResource(R.drawable.angry);
                    comment.setText("Probably never gonna try again!");
                    s_comment = "Probably never gonna try again!";
                }
                else if(rating > 1 && rating <= 2)
                {
                    emoji.setImageResource(R.drawable.sad);
                    comment.setText("Wasn't that good!");
                    s_comment = "Probably never gonna try again!";
                }
                else if(rating > 2 && rating <= 3)
                {
                    emoji.setImageResource(R.drawable.ok);
                    comment.setText("It was OK!");
                    s_comment = "It was OK!";
                }
                else if(rating > 3 && rating <= 4)
                {
                    emoji.setImageResource(R.drawable.smiling);
                    comment.setText("Will give it another try!");
                    s_comment = "Will give it another try!";
                }
                else if(rating > 4 && rating <= 5)
                {
                    emoji.setImageResource(R.drawable.happy);
                    comment.setText("Best Experience Ever!");
                    s_comment = "Best Experience Ever!";
                }
            }
        });

        ok_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetResult(BaseClass.BaseUrl+"insert_reting_on_vendor?VID="+VID+"&user_id="+USER_ID+"&reting="+2.6+"&review="+s_comment,2);
            }
        });

        alertDialog.setView(alertLayout);

        alertDialog.show();
    }

    public void ReadJson(JSONObject jsonObject) {
        data.clear();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("Result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String Model_name = object.getString("Model_name");
                String manufacturer_name = object.getString("manufacturer_name");
                String bookdate = object.getString("bookdate");
                int vehicle_type = object.getInt("vehicle_type");
                int service_type = object.getInt("service_type");
                String extra_requerment = object.getString("extra_requerment");
                String SSID = object.getString("SSID");
                int work_done = object.getInt("work_done");
                int VID = object.getInt("VID");
                int bookin_id = object.getInt("bookin_id");
                String vendor_name = object.getString("vendor_name");

                data.add(new ModelBookingServiceHistory(Model_name, manufacturer_name, bookdate, extra_requerment, SSID, vendor_name, service_type, work_done, bookin_id,vehicle_type,VID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        insurance_list.setAdapter(new CustomAdapter(getActivity(), data));
    }

    public void ReadJson2(JSONObject jsonObject) {
        try {
            String status =  jsonObject.getString("status");
            if(status.equals("1")){
                alertDialog.dismiss();
            }
            else {
                alertDialog.dismiss();
                Toast.makeText(getActivity(),"Try Again!",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetResult(String mURL, final int flag) {
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e(TAG, response.toString());
                                progressDialog.dismiss();
                                if (response.toString() != null) {
                                    if(flag == 1)
                                    ReadJson(response);
                                    else
                                        ReadJson2(response);
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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.booking_service_main));

    }
}
