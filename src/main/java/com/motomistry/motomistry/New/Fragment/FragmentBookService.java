package com.motomistry.motomistry.New.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
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
import com.motomistry.motomistry.Aleart.myAleart;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Fragment.MyVehicles;
import com.motomistry.motomistry.Model.ModelSubService;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.motomistry.motomistry.View.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentBookService extends Fragment {
    private View view;
    private ArrayList<ModelSubService>venderLists=new ArrayList<>();
    private RequestQueue requestQueue;
    private String TAG="List";
    private myAleart aleart=new myAleart(getActivity());
    private ConnectionManager manager;
    private int VADER_ID=0;
    private ProgressBar loding;
    private TextView shop_name,vehicle_name;
    private ImageView select_date;
    private Switch pick_drop_switch;
    private EditText additional_edit;
    private LinearLayout daydate1,daydate2;
    private ImageView open_requirements;
    private TextView total_charge;
    private CardView select_vehical;
    private EditText date;
    private EditText month;
    private Button proceed;
    private Button back;
    private Session session;
    private String USER_ID;
    private String SELECTED_DATE;
    int SERVICE_TYPE=1;
    int click_count = 0;
    int flag = 0;

    @SuppressLint("ValidFragment")
    public FragmentBookService(int TotalCharege,String Vender_name,int vehical_type) {
        BaseClass.TOTAL_CHARGE=TotalCharege;
        BaseClass.VANDER_NAME=Vender_name;
        BaseClass.VEHICAL_TYPE=vehical_type;

    }

    public FragmentBookService() {
        // Required empty public constructor
    }
    public static FragmentBookService newInstance() {
        FragmentBookService fragment = new FragmentBookService();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view==null){
        view = inflater.inflate(R.layout.fragment_confirm_booking, container, false);
            GetRecordFromSession();
            shop_name = (TextView)view.findViewById(R.id.shop_name);
            total_charge = (TextView)view.findViewById(R.id.total_charge);
            vehicle_name = (TextView)view.findViewById(R.id.vehicle_name);
            select_vehical = (CardView)view.findViewById(R.id.select_vehical);
            date = (EditText) view.findViewById(R.id.date);
            month = (EditText) view.findViewById(R.id.month);
            select_date = (ImageView)view.findViewById(R.id.select_date);
            pick_drop_switch = (Switch)view.findViewById(R.id.pick_drop_switch);
            additional_edit = (EditText)view.findViewById(R.id.additional_edit);
             loding = (ProgressBar)view.findViewById(R.id.loding);
            additional_edit = (EditText) view.findViewById(R.id.additional_edit);
            open_requirements = (ImageView) view.findViewById(R.id.open_requirements);
            daydate1 = (LinearLayout) view.findViewById(R.id.daydate1);
            daydate2 = (LinearLayout) view.findViewById(R.id.daydate2);
            proceed = (Button) view.findViewById(R.id.proceed);
            back = (Button) view.findViewById(R.id.back);
            shop_name.setText(BaseClass.VANDER_NAME);
            vehicle_name.setText(BaseClass.MY_VEHICAL_NAME);
            total_charge.setText(String.valueOf(BaseClass.TOTAL_CHARGE));
            manager=new ConnectionManager(getActivity());
            open_requirements.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click_count++;
                    if(click_count%2==1)
                    additional_edit.setVisibility(View.VISIBLE);
                    else
                        additional_edit.setVisibility(View.GONE);
                }
            });
            daydate1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    daydate1.setAnimation(new AnimationUtils().loadAnimation(getActivity(),R.anim.null_tofade));
                }
            });
            daydate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    daydate2.setAnimation(new AnimationUtils().loadAnimation(getActivity(),R.anim.null_tofade));
                }
            });
            select_vehical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity)getActivity()).replace_Fragment(new MyVehicles("booking"));
                }
            });
            select_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment dialogfragment = new DatePickerDialogTheme();
                    dialogfragment.show(getActivity().getFragmentManager(), "Theme");
                }
            });

            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SERVICE_TYPE=pick_drop_switch.isChecked()?0:1;
                    if(flag == 1){
                        SELECTED_DATE=date.getText().toString()+"-"+month.getText().toString();
                    }
                    if(vehicle_name.getText().toString().equals("")){
                        vehicle_name.setText("Select your vehicle first!");
                        vehicle_name.setTextColor(getResources().getColor(R.color.flatRed));
                    }
                    if(date.getText().toString().equals("") || month.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"Select your booking date!",Toast.LENGTH_LONG).show();
                    }
                   GetResult(BaseClass.getGetBookServiceUrl(USER_ID,SELECTED_DATE, BaseClass.encodeMsg(additional_edit.getText().toString()),SERVICE_TYPE));
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity)getActivity()).bacPOP();
                }
            });

            final Calendar calendar = Calendar.getInstance();

            date.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try {
                        if (Integer.parseInt(s.toString()) > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){

                            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            date.setText(String.valueOf(max));
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            month.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try{
                        if(Integer.parseInt(s.toString())> 12)
                        month.setText("12");
                    }
                    catch(NumberFormatException e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            month.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    flag = 1;
                }
            });

    }return view;}


    public void GetResult(String mURL) {
        loding.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                loding.setVisibility(View.GONE);
                                if (response.toString() != null) {
                                    ReadJson(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                aleart.Error(error.toString());
                                loding.setVisibility(View.GONE);
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

    private void ReadJson(JSONObject jsonObject) {
        try {
            String result=jsonObject.getString("Result");
            if (result.equals("1")){
              new Notification(getActivity()).sendNotification();
                ((DashbordActivity)getActivity()).finish();
                startActivity(new Intent(getActivity(),DashbordActivity.class));
                Toast.makeText(getActivity(), "Save service booking", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Unable to book your service!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }
    @SuppressLint("ValidFragment")
    public class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date nextMonthLastDay = calendar.getTime();

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    R.style.datepicker,this,year,month,day);

            datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datepickerdialog.getDatePicker().setMaxDate(nextMonthLastDay.getTime());

            datepickerdialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);



            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int yer, int moth, int day){

            date.setText(String.valueOf(day));
            month.setText(String.valueOf(moth+1));
            flag = 0;
            SELECTED_DATE=String.valueOf(yer)+"-"+String.valueOf(moth+1)+"-"+String.valueOf(day);

        }
    }
    private void GetRecordFromSession(){
        session=new Session(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.confirm_booking_main));

    }
}
