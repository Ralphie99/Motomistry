package com.motomistry.motomistry.New.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.motomistry.motomistry.New.Model.ModelDraivivgSchool;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.motomistry.motomistry.View.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DrivingPackages extends Fragment {

    private View view;
    private ListView insurance_list;
    ArrayList<ModelDraivivgSchool> data;
    private String TAG="inusrance";
    private RequestQueue requestQueue;
    private Session session;
    private boolean isFabOpen = true;
    private String USER_ID;
    private CustomDialog progressDialog;
    ImageView find_fab, track_contact, add_contact;
    Animation fab_open, fab_close, rotate_open, rotate_close;
    SwipeRefreshLayout swiperefresh;
    Boolean is_swipe = false;
    int CurrentVisibleItemCount,CurrentScrollState;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;
    LinearLayout bike,car;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_driving_package,container,false);

        insurance_list = (ListView) view.findViewById(R.id.insurance_list);
        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);
        find_fab = (ImageView) view.findViewById(R.id.find_fab);

        bike = (LinearLayout) view.findViewById(R.id.bike);
        car = (LinearLayout) view.findViewById(R.id.car);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.popup_scale_1));
        animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.popup_scale_2));
        find_fab.setAnimation(animationSet);
        track_contact = (ImageView) view.findViewById(R.id.track_contact);
        add_contact = (ImageView) view.findViewById(R.id.add_contact);

        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.null_tofade);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_tonull);
        rotate_open = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_fab_open);
        rotate_close = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_fab_close);



        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                is_swipe = true;
                GetResult(BaseClass.BaseUrl+"get_vendor_driving_School_package?Vehicle_type=1","PACKAGE");
            }
        });

        find_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        track_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new FragmentTrackContact());
            }
        });

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).replace_Fragment(new FragmentAddContact());
            }
        });

        data = new ArrayList<ModelDraivivgSchool>();


        insurance_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int btn_initPosY=find_fab.getScrollY();

                if (view.getId() == insurance_list.getId()) {
                    final int currentFirstVisibleItem = insurance_list.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        mIsScrollingUp = false;
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        mIsScrollingUp = true;
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }

                if (mIsScrollingUp) {

                    find_fab.animate().cancel();
                    find_fab.animate().translationY(btn_initPosY);
                } else {
                    find_fab.animate().cancel();
                    find_fab.animate().translationYBy(150);
//                    if(!isFabOpen) {
//                        if (track_contact.getAlpha() == 1) {
//                            track_contact.startAnimation(fab_close);
//                            add_contact.startAnimation(fab_close);
//                        } else if(track_contact.getAlpha() == 0) {
//                            track_contact.setVisibility(View.INVISIBLE);
//                            add_contact.setVisibility(View.INVISIBLE);
//                        }
//                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bike.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccentSelected));
                car.setBackgroundTintList(getResources().getColorStateList(R.color.White));
                GetResult(BaseClass.BaseUrl+"get_vendor_driving_School_package?Vehicle_type=0","PACKAGE");
            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccentSelected));
                bike.setBackgroundTintList(getResources().getColorStateList(R.color.White));
                GetResult(BaseClass.BaseUrl+"get_vendor_driving_School_package?Vehicle_type=1","PACKAGE");
            }
        });

        GetRecordFromSession();
        bike.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccentSelected));
        car.setBackgroundTintList(getResources().getColorStateList(R.color.White));
       GetResult(BaseClass.BaseUrl+"get_vendor_driving_School_package?Vehicle_type=0","PACKAGE");

        return view;
    }

    private void animateFab() {

        if (isFabOpen) {

            find_fab.startAnimation(rotate_open);
            track_contact.startAnimation(fab_open);
            add_contact.startAnimation(fab_open);
            track_contact.setClickable(true);
            add_contact.setClickable(true);
            isFabOpen = false;
        } else {
            find_fab.startAnimation(rotate_close);
            track_contact.startAnimation(fab_close);
            add_contact.startAnimation(fab_close);
            track_contact.setClickable(false);
            add_contact.setClickable(false);
            isFabOpen = true;
        }
    }

    private void GetRecordFromSession() {
        session = new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
        Log.e("USER_ID",USER_ID);
    }
    private class CustomAdapter extends ArrayAdapter<ModelDraivivgSchool> {

        Context context;
        private Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);

        public CustomAdapter(@NonNull Context context, @NonNull List objects) {
            super(context,0, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.draiving_school_list,parent,false);
            }
            ModelDraivivgSchool current = getItem(position);
            TextView package_name = (TextView) convertView.findViewById(R.id.package_name);
            Button book_now = (Button) convertView.findViewById(R.id.book_now);
            TextView available_vehicle = (TextView) convertView.findViewById(R.id.available_vehicle);
            TextView duration = (TextView) convertView.findViewById(R.id.duration);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            package_name.setText(current.getPackage_name());
            package_name.setTypeface(iconFont);
            available_vehicle.setText(current.getAvailable_vehicle());
            duration.setText(current.getDuration());
            price.setText(getString(R.string.Rs)+" "+String.valueOf(current.getPrice()));
            duration.setTypeface(iconFont);
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.driving_school_rating);
            ratingBar.setRating(current.getRating());
            book_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle("Are you sure?");
                    builder1.setMessage("You want to book package-"+data.get(position).getPackage_name());
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Yes! book it.",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    GetResult(BaseClass.BaseUrl+"book_driving_School_package?Vendor_id="+data.get(position).getVID()+"&package_id="+data.get(position).getId()+"&application_user_id="+USER_ID,"BOOKIBG");
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            });
            return convertView;
        }
    }

    public void ReadJson(JSONObject jsonObject) {
        data.clear();
        try {
            JSONArray jsonArray =jsonObject.getJSONArray("Result");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object1 = jsonArray.getJSONObject(i);
                JSONObject object= object1.getJSONObject("package_details");
                int id=object.getInt("id");
                int VID=object.getInt("VID");
                int price=object.getInt("price");
                String package_name=object.getString("package_name");
                String available_vehicle=object.getString("available_vehicle");
                String duration=object.getString("duration");
                JSONArray retting = object1.getJSONArray("Rating");
                String rating = retting.getJSONObject(0).getString("retting");
                Float double_rating= 0f;
                if(!(rating.equals("null"))){
                    double_rating = Float.valueOf(rating);
                }
                StringBuilder stringBuilder = new StringBuilder();
                JSONArray models = object1.getJSONArray("models");
                for (int j=0;j<models.length();j++){
                    JSONObject jsonObject1 = models.getJSONObject(i);
                    if(j==(models.length()-1)){
                        stringBuilder.append(jsonObject1.getString("Model_name"));
                    }else{
                        stringBuilder.append(jsonObject1.getString("Model_name")+"\n"+"\n");
                    }
                }
                data.add(new ModelDraivivgSchool(id,VID,price,package_name,stringBuilder,duration,double_rating));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        insurance_list.setAdapter(new CustomAdapter(getActivity(),data));
    }
    public void GetResult(String mURL, final String type) {
        if(!is_swipe){
        progressDialog.show();}
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                if(is_swipe){
                                    swiperefresh.setRefreshing(false);
                                }
                                else{
                                    progressDialog.dismiss();
                                }

                                if (response.toString() != null) {
                                    switch (type){
                                        case "BOOKIBG":
                                            try {
                                                String result=response.getString("Result");
                                                if (result.equalsIgnoreCase("done")){
                                                    Toast.makeText(getActivity(), "Booking Successful ", Toast.LENGTH_SHORT).show();
                                                    new Notification(getActivity()).ShowNotification("DRIVING_HISTORY","Driving school package book successful.","our team will contact with you as soon as possible");
                                                }else {
                                                    Toast.makeText(getActivity(), "Booking failed please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "PACKAGE":
                                            ReadJson(response);
                                            break;
                                    }
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
