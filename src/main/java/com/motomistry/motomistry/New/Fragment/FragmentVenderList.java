package com.motomistry.motomistry.New.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.motomistry.motomistry.New.Adapter.AdapterVenderList;
import com.motomistry.motomistry.New.Model.ModelVenderList;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentVenderList extends Fragment {
    private View view;
    private ArrayList<ModelVenderList>venderLists=new ArrayList<>();
    private RequestQueue requestQueue;
    private String TAG="List";
    private myAleart aleart=new myAleart(getActivity());
    private ConnectionManager manager;
    private int SERVICE_ID=0;
    private ProgressBar loding;
    private ListView list;
    private ImageView back_press;

    @SuppressLint("ValidFragment")
    public FragmentVenderList(int SERVICE_ID) {
        this.SERVICE_ID = SERVICE_ID;
    }

    public FragmentVenderList() {
        // Required empty public constructor
    }
    public static FragmentVenderList newInstance() {
        FragmentVenderList fragment = new FragmentVenderList();
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
        view = inflater.inflate(R.layout.fragment_vender_list, container, false);
            list = (ListView)view.findViewById(R.id.vender_list);
             loding = (ProgressBar)view.findViewById(R.id.loding);
            manager=new ConnectionManager(getActivity());
            if (manager.isConnected()){
                GetResult(BaseClass.getGetVenderListUrl(SERVICE_ID));
            }
            back_press = (ImageView)view.findViewById(R.id.back_press);
            back_press.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity) getActivity()).bacPOP();
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
                JSONObject vendor_details = object.getJSONObject("vendor_details");
                int Vendor_id = vendor_details.getInt("id");
                String Vendor_name = vendor_details.getString("vendor_name");
                String Contact_Person = vendor_details.getString("contact_person");
                String City = vendor_details.getString("city");
                String image_path = vendor_details.getString("image");
                String vicinity = vendor_details.getString("locality");
                String Address = vendor_details.getString("Address");
                JSONArray retting = object.getJSONArray("rettting");
                String rating = retting.getJSONObject(0).getString("retting");
                Float double_rating= 0f;
                if(!(rating.equals("null"))){
                    double_rating = Float.valueOf(rating);
                }
                ModelVenderList venderList=new ModelVenderList(Vendor_id,Vendor_name,Contact_Person,City,image_path,vicinity,Address,double_rating);
                venderLists.add(venderList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list.setAdapter(new AdapterVenderList(getActivity(),venderLists));
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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.vendor_list_main));

    }
}
