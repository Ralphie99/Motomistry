package com.motomistry.motomistry.New.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.motomistry.motomistry.Model.ModelSubService;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentSubServicesList extends Fragment {
    private SparseBooleanArray mSparseBooleanArray;
    private  int Vehical_type;
    private  String VANDER_NAME;
    private View view;
    private ArrayList<ModelSubService>venderLists=new ArrayList<>();
    private ArrayList<ModelSubService>SelectedChageList=new ArrayList<>();
    private RequestQueue requestQueue;
    private String TAG="List";
    private myAleart aleart=new myAleart(getActivity());
    private ConnectionManager manager;
    private int VADER_ID=0;
    private ProgressBar loding;
    private ListView list;
    private TextView total_charge;
    private int TotalCharge=0;
    private Button btn_proseed;
    private Button btn_back;
    private TextView toolbar_vander_name;
    private CardView back_press;
    private ArrayList<ModelSubService> data;
    @SuppressLint("ValidFragment")
    public FragmentSubServicesList(int VADER_ID,String VANDER_NAME,int Vehical_type) {
        this.VADER_ID = VADER_ID;
        this.VANDER_NAME = VANDER_NAME;
        this.Vehical_type = Vehical_type;

    }

    public FragmentSubServicesList() {

    }
    public static FragmentSubServicesList newInstance() {
        FragmentSubServicesList fragment = new FragmentSubServicesList();
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
        view = inflater.inflate(R.layout.fragment_subservice_list, container, false);
            list = (ListView)view.findViewById(R.id.vender_list);
            loding = (ProgressBar)view.findViewById(R.id.loding);
            total_charge = (TextView)view.findViewById(R.id.total_charge);
            toolbar_vander_name = (TextView)view.findViewById(R.id.toolbar_vander_name);
            btn_proseed = (Button)view.findViewById(R.id.btn_proseed);
            btn_back = (Button) view.findViewById(R.id.btn_back);
            back_press = (CardView)view.findViewById(R.id.back_press);
            manager=new ConnectionManager(getActivity());
            toolbar_vander_name.setText(VANDER_NAME);
            if (manager.isConnected()){
                GetResult(BaseClass.getGetSubServiceUrl(VADER_ID,BaseClass.HOME_VEHICLE_TYPE,BaseClass.Service_ID_Selected));
            }
            btn_proseed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder builder=new StringBuilder();
                    if(getCheckedItems().size()==0){
                        Toast.makeText(getActivity(),"Please select any service to proceed!",Toast.LENGTH_LONG).show();
                    }else {
                        for (int i = 0; i < getCheckedItems().size(); i++) {
                            builder.append(getCheckedItems().get(i) + ",");
                        }
                        BaseClass.Sub_Service_ID = String.valueOf(builder);
                        Log.e("StringBuld", BaseClass.Sub_Service_ID);
                        ((DashbordActivity) getActivity()).replace_Fragment(new FragmentBookService(TotalCharge, VANDER_NAME, Vehical_type));
                    }
                }
            });

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity) getActivity()).bacPOP();
                }
            });
            back_press.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity) getActivity()).bacPOP();
                }
            });
    }return view;}
    public void GetResult(String mURL) {
        Log.e("SUB_SERVICE",mURL);
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
            JSONArray array=jsonObject.getJSONArray("Result");
            for (int i=0;i<array.length();i++){
                JSONObject object=array.getJSONObject(i);
                ModelSubService List=new ModelSubService(object.getInt("id"),object.getInt("vehicle_type"),
                        object.getString("service_name"),
                        object.getString("price"));
                venderLists.add(List);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list.setAdapter(new AdapterSubSurvice(getActivity(),venderLists));
    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }
    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for(int i=0;i<data.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(String.valueOf(data.get(i).getId()));
            }
        }
        return mTempArry;
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }
    public class AdapterSubSurvice extends BaseAdapter {
        private Context mContext;

        private Typeface iconFont_name = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);
        private Typeface iconFont_price = FontManager.getTypeface(getContext(), FontManager.MONTSERRAT_BOLD);
        public AdapterSubSurvice(Context c, ArrayList<ModelSubService> modelComment){
            mContext = c;
            data=modelComment;
            mSparseBooleanArray = new SparseBooleanArray();

        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.list_element, parent, false);
                TextView name=(TextView)convertView.findViewById(R.id.name);
                name.setTypeface(iconFont_name);
                TextView price=(TextView)convertView.findViewById(R.id.price);
                price.setTypeface(iconFont_price);
//                ImageView list_image=(ImageView)convertView.findViewById(R.id.list_image);
                final CheckBox chackbox=(CheckBox) convertView.findViewById(R.id.chackbox);
                name.setText(data.get(position).getService_name());
                price.setText(getString(R.string.Rs)+" "+data.get(position).getPrice());
//                if (Vehical_type==1){
//                    list_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.car));
//                }
                chackbox.setTag(position);
                chackbox.setChecked(mSparseBooleanArray.get(position));
                chackbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                        total_charge.setText(getString(R.string.Rs)+" "+SetTotalCharge(isChecked, Integer.parseInt(data.get(position).getPrice())));
                       /* data.get(position).setChecked(isChecked);
                        chackbox.setChecked(data.get(position).isChecked());
                        notifyDataSetChanged();
                        total_charge.setText(SetTotalCharge(isChecked, Integer.parseInt(data.get(position).getPrice())));
                        if (isChecked){
                            SelectedChageList.add(new ModelSubService(data.get(position).getId(),
                                    data.get(position).getVehicle_type(),
                                    data.get(position).getService_name(),data.get(position).getPrice()));
                        }else {
                            SelectedChageList.remove(new ModelSubService(data.get(position).getId(),
                                    data.get(position).getVehicle_type(),
                                    data.get(position).getService_name(),data.get(position).getPrice()));
                        }*/
                    }
                });
            }
            return convertView;
        }
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }


    }
    private String SetTotalCharge(boolean b,int charge){
        TotalCharge= b?TotalCharge+charge:TotalCharge-charge;
        return String.valueOf(TotalCharge);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.sub_services_main));

    }

}
