package com.motomistry.motomistry.New.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.motomistry.motomistry.New.Model.ModelDrivingHistory;
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
import java.util.List;
import java.util.Map;


public class DrivingSchoolHistory extends Fragment {

    private View view;
    private ListView insurance_list;
    ArrayList<ModelDrivingHistory> data;
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
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;
    LinearLayout no_driving_booking;
    Button go_booking;
    AlertDialog alertDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_driving_history,container,false);
        insurance_list = (ListView) view.findViewById(R.id.insurance_list);
        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);

        no_driving_booking = (LinearLayout) view.findViewById(R.id.no_driving_bookings);

        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        find_fab = (ImageView) view.findViewById(R.id.find_fab);

        go_booking = (Button) view.findViewById(R.id.go_booking);

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

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                is_swipe = true;
                GetResult(BaseClass.BaseUrl+"get_user_driving_history?user_id="+USER_ID,1);
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

        data = new ArrayList<ModelDrivingHistory>();
        GetRecordFromSession();
           GetResult(BaseClass.BaseUrl+"get_user_driving_history?user_id="+USER_ID,1);

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
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        if(no_driving_booking.getVisibility() == View.VISIBLE){
            go_booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Learn_Driving.driving_school_pager.setCurrentItem(0);
                }
            });
        }


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
    }
    private class CustomAdapter extends ArrayAdapter<ModelDrivingHistory> {

        private Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);

        Context context;
        public CustomAdapter(@NonNull Context context, @NonNull List objects) {
            super(context,0, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.draiving_history_list,parent,false);
            }
            final ModelDrivingHistory current = getItem(position);
            TextView package_name = (TextView) convertView.findViewById(R.id.package_name);
            TextView emp_name = (TextView) convertView.findViewById(R.id.emp_name);
            TextView duration = (TextView) convertView.findViewById(R.id.duration);
            TextView vendor_name = (TextView) convertView.findViewById(R.id.vendor_name);
            Button rate_us= (Button) convertView.findViewById(R.id.rate_us);
            TextView school_pending = (TextView) convertView.findViewById(R.id.school_pending);
            TextView school_completed = (TextView) convertView.findViewById(R.id.school_completed);
            if(current.getWork_done() == 0){
                school_pending.setVisibility(View.VISIBLE);
                school_completed.setVisibility(View.GONE);
                rate_us.setVisibility(View.GONE);
            }
            else {
                school_pending.setVisibility(View.GONE);
                school_completed.setVisibility(View.VISIBLE);
                rate_us.setVisibility(View.VISIBLE);
            }

            rate_us.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlert(current.getPackage_id());
                }
            });
            package_name.setText(current.getPackage_name());
            package_name.setTypeface(iconFont);
            emp_name.setText(current.getEmp_name());
            emp_name.setTypeface(iconFont);
            duration.setText(current.getDuration());
            duration.setTypeface(iconFont);
            vendor_name.setText(current.getVendor_name());
            vendor_name.setTypeface(iconFont);
            return convertView;
        }
    }
    private String s_comment;
    private void showAlert(final int package_id){

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
                GetResult(BaseClass.BaseUrl+"insert_reting_on_driving_school_package?package_id="+package_id+"&user_id="+USER_ID+"&reting="+2.6+"&review="+s_comment,2);
            }
        });

        alertDialog.setView(alertLayout);

        alertDialog.show();
    }

    public void ReadJson(JSONObject jsonObject) {
        data.clear();
        try {
            JSONArray jsonArray =jsonObject.getJSONArray("Result");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object= jsonArray.getJSONObject(i);
                String emp_name=object.getString("emp_name");
                String vendor_name=object.getString("vendor_name");
                int work_done = object.getInt("work_done");
                String package_name=object.getString("package_name");
                String duration=object.getString("duration");
                int package_id = object.getInt("package_id");
                data.add(new ModelDrivingHistory(emp_name,vendor_name,package_name,duration,work_done,package_id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(data.size() == 0){
            no_driving_booking.setVisibility(View.VISIBLE);
        }
        else {
            no_driving_booking.setVisibility(View.GONE);
        }
        insurance_list.setAdapter(new CustomAdapter(getActivity(),data));
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
        Log.e("URL",mURL);
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
                                else {
                                    progressDialog.dismiss();
                                }
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
}
